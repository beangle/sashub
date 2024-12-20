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

import org.beangle.data.dao.OqlBuilder
import org.beangle.sashub.model.config.*
import org.beangle.sashub.service.ProfileService
import org.beangle.sashub.web.action.helper.ProfileHelper
import org.beangle.webmvc.annotation.mapping
import org.beangle.webmvc.context.{ActionContext, Params}
import org.beangle.webmvc.view.View
import org.beangle.web.servlet.util.CookieUtils
import org.beangle.webmvc.support.action.RestfulAction

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
      put("profile", entityDao.get(classOf[Profile], getLongId("host.profile")))
    }
  }

  override protected def saveAndRedirect(host: Host): View = {
    val dependencyIds = getAll("featureId", classOf[Long])
    val newFeatures = entityDao.find(classOf[PlatformFeature], dependencyIds);
    val removed = host.features.filter(x => !newFeatures.contains(x))
    val added = newFeatures.filter(x => !host.features.contains(x))
    host.features.subtractAll(removed)
    host.features.addAll(added)
    super.saveAndRedirect(host)
  }

  @mapping(value = "{id}")
  override def info(id: String): View = {
    val host = entityDao.get(classOf[Host], id.toLong)
    val query = OqlBuilder.from(classOf[Webapp], "webapp")
    query.join("webapp.targets", "server")
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
