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

package org.beangle.sashub.web.action.config

import _root_.org.beangle.data.dao.OqlBuilder
import _root_.org.beangle.sashub.model.config.*
import _root_.org.beangle.sashub.service.ProfileService
import _root_.org.beangle.sashub.web.action.helper.ProfileHelper
import _root_.org.beangle.web.action.annotation.mapping
import _root_.org.beangle.web.action.context.{ActionContext, Params}
import _root_.org.beangle.web.action.view.View
import _root_.org.beangle.web.servlet.util.CookieUtils
import _root_.org.beangle.webmvc.support.action.RestfulAction

class HostAction extends RestfulAction[Host] {
  var profileService: ProfileService = _

  override protected def indexSetting(): Unit = {
    ProfileHelper.setRememberedProfile(entityDao)
    put("profiles", profileService.getAll())
  }

  override protected def editSetting(host: Host): Unit = {
    put("platforms", entityDao.getAll(classOf[Platform]))
    val features = entityDao.getAll(classOf[PlatformFeature]).toBuffer
    put("features", features)
    if (host.persisted) {
      put("profile", host.profile)
    } else {
      put("profile", entityDao.get(classOf[Profile], longId("host.profile")))
    }
  }

  override protected def saveAndRedirect(host: Host): View = {
    val dependencyIds = getAll("featureId", classOf[Long])
    host.features.clear()
    host.features ++= entityDao.find(classOf[PlatformFeature], dependencyIds)
    super.saveAndRedirect(host)
  }

  @mapping(value = "{id}")
  override def info(id: String): View = {
    val host = entityDao.get(classOf[Host], id.toLong)
    val query = OqlBuilder.from(classOf[Webapp], "webapp")
    query.join("webapp.runAt", "server")
    query.where("server.host=:host", host)
    query.select("distinct webapp")
    put("webapps", entityDao.search(query))
    put("servers", entityDao.findBy(classOf[Server], "host", host))
    put("host", host)
    forward()
  }

  override def search(): View = {
    ProfileHelper.remember("host.profile.id")
    super.search()
  }
}
