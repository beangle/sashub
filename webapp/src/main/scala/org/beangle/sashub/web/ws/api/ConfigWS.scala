/*
 * Copyright (C) 2005, The Beangle Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.beangle.sashub.web.ws.api

import _root_.org.beangle.commons.lang.Strings
import _root_.org.beangle.data.dao.{EntityDao, OqlBuilder}
import _root_.org.beangle.sashub.model.config.*
import _root_.org.beangle.sashub.service.ProfileService
import _root_.org.beangle.web.action.annotation.{action, mapping, param}
import _root_.org.beangle.web.action.support.{ActionSupport, ParamSupport, ServletSupport}
import _root_.org.beangle.web.action.view.{Status, View}

import scala.collection.mutable

/**
 * 获取配置信息
 */
class ConfigWS extends ActionSupport with ParamSupport with ServletSupport {

  var profileService: ProfileService = _
  var entityDao: EntityDao = _

  def server(@param("profile") name: String): View = {
    val profiles = getProfile(name)
    if (profiles.size != 1) return Status.NotFound

    val profile = profiles.head
    val farms = entityDao.findBy(classOf[Farm], "profile", profile)
    val webapps = entityDao.findBy(classOf[Webapp], "profile", profile)
    val hosts = entityDao.findBy(classOf[Host], "profile", profile)

    put("profile", profile)
    put("hosts", hosts.sortBy(_.ip))
    put("farms", farms.sortBy(_.name))
    put("webapps", webapps.sortBy(_.gav))
    put("engines", farms.map(_.engine).toSet)
    forward()
  }

  private def getProfile(name: String): Option[Profile] = {
    val profiles = profileService.getProfile(name)
    profiles match {
      case Some(p) =>
        val ips = request.getHeader("ip")
        if Strings.isBlank(ips) then None
        else
          val ipSets = Strings.split(ips, " ").toSet
          if ipSets.contains(p.ip) then profiles
          else
            val hosts = entityDao.findBy(classOf[Host], "profile", p)
            if hosts.exists(h => ipSets.contains(h.ip)) then profiles else None
      case None => None
    }
  }

  def proxy(@param("profile") name: String): View = {
    val profiles = getProfile(name)
    if (profiles.size != 1) return Status.NotFound
    val profile = profiles.head
    prepareProxyData(profile)
    forward(profile.proxyEngine)
  }

  private def prepareProxyData(profile: Profile): Unit = {
    val query = OqlBuilder.from(classOf[Webapp], "webapp")
    query.where("webapp.profile=:profile and size(webapp.targets) >0", profile)
    val webappList = entityDao.search(query)

    val webapps = webappList.sortBy(w => w.contextPath).toBuffer
    val roots = webapps.filter(_.contextPath.length < 2)
    webapps.subtractAll(roots)
    webapps.addAll(roots)

    val backendServers = new mutable.HashMap[Farm, mutable.HashSet[Set[Server]]]
    val backendMapping = new mutable.HashMap[Set[Server], mutable.HashSet[Webapp]]
    webapps foreach { webapp =>
      if webapp.targets.nonEmpty then
        val farm = webapp.targets.head.farm
        val servers = webapp.targets.toSet
        val farmBackends = backendServers.getOrElseUpdate(farm, new mutable.HashSet[Set[Server]])
        farmBackends.addOne(servers)
        backendMapping.getOrElseUpdate(servers, new mutable.HashSet[Webapp]).addOne(webapp)
    }
    val backends = new mutable.ArrayBuffer[Backend]
    val entryPoints = new mutable.HashMap[Webapp, Backend]
    backendServers foreach { case (farm, serverLists) =>
      var i = 1
      val farmBackends = serverLists map { servers =>
        val backendName =
          if servers.size == 1 then
            servers.head.qualifiedName.replace('.', '_')
          else
            i = i + 1
            s"${farm.name}$i"
        val backend = new Backend(backendName)
        backend.servers ++= servers
        backend.options = farm.proxyOptions
        backendMapping(servers) foreach { webapp => entryPoints.put(webapp, backend) }
        backend
      }
      if farmBackends.size == 1 then farmBackends.head.name = farm.name
      backends ++= farmBackends
    }

    put("profile", profile)
    put("backends", backends)
    put("webapps", webapps)
    put("entryPoints", entryPoints)
  }

}
