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
import _root_.org.beangle.sashub.model.config.{Artifact, Profile, Server, Webapp}
import _root_.org.beangle.sashub.service.ProfileService
import _root_.org.beangle.sashub.web.action.helper.ProfileHelper
import _root_.org.beangle.web.action.view.View
import _root_.org.beangle.webmvc.support.action.RestfulAction

/** 应用部署管理
 */
class WebappAction extends RestfulAction[Webapp] {
  var profileService: ProfileService = _
  def upgrade(): View = {
    val webapps = entityDao.find(classOf[Webapp], longIds("webapp"))
    webapps.foreach { webapp =>
      webapp.version = webapp.artifact.latestVersion
    }
    entityDao.saveOrUpdate(webapps)
    redirect("search", "info.save.success")
  }

  override def search(): View = {
    ProfileHelper.remember("webapp.profile.id")
    super.search()
  }

  override protected def indexSetting(): Unit = {
    ProfileHelper.setRememberedProfile(entityDao)
    put("profiles", profileService.getAll())
  }

  override protected def editSetting(webapp: Webapp): Unit = {
    val profile = entityDao.get(classOf[Profile], longId("webapp.profile"))
    put("profile", profile)
    val query = OqlBuilder.from(classOf[Server], "server")
    query.where("server.farm.profile =:profile", profile)
    put("servers", entityDao.search(query))

    val aquery = OqlBuilder.from(classOf[Artifact], "artifact")
    aquery.where("not exists(from " + classOf[Webapp].getName +
      " app where app.profile=:profile and app.artifact=artifact)", profile)
    aquery.where("artifact.profile is null or artifact.profile=:profile", profile)
    val artifacts = entityDao.search(aquery).toBuffer
    if (webapp.persisted) artifacts.addOne(webapp.artifact)
    put("artifacts", artifacts)
    super.editSetting(webapp)
  }

  override protected def saveAndRedirect(webapp: Webapp): View = {
    val profile = entityDao.get(classOf[Profile], longId("webapp.profile"))
    webapp.profile = profile
    webapp.runAt.clear()
    webapp.runAt ++= entityDao.find(classOf[Server], getAll("server.id", classOf[Long]))
    super.saveAndRedirect(webapp)
  }
}
