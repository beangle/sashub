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

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.sashub.model.config.Webapp
import org.beangle.sashub.model.ems.{App, Menu, Resource}
import org.beangle.sashub.service.ProfileService
import org.beangle.web.action.annotation.{mapping, param}
import org.beangle.web.action.support.{ActionSupport, ParamSupport, ServletSupport}
import org.beangle.web.action.view.{Status, View}

class MenuWS extends ActionSupport with ParamSupport with ServletSupport {

  var profileService: ProfileService = _
  var entityDao: EntityDao = _

  @mapping("{appName}")
  def index(@param("profile") profileName: String, @param("appName") appName: String): View = {
    profileService.getProfile(profileName) match {
      case None => Status.NotFound
      case Some(profile) =>
        val query = OqlBuilder.from[App](classOf[Webapp].getName + " webapp," + classOf[App].getName + " app")
        query.where("webapp.profile=:profile", profile)
        query.where("webapp.artifact.groupId=app.group.name")
        query.where("webapp.artifact.artifactId=app.artifactId")
        query.where("app.name=:appName", appName)
        query.select("app").cacheable()
        val apps = entityDao.search(query)
        apps.headOption match {
          case None => Status.NotFound
          case Some(app) =>
            val query = OqlBuilder.from(classOf[Menu], "menu")
            query.where("menu.app=:app", app)
            query.limit(null)
            query.where("menu.parent is null")
            val menus = entityDao.search(query)
            put("menus", menus)
            put("resources", entityDao.findBy(classOf[Resource], "app", app))
            forward()
        }
    }
  }
}
