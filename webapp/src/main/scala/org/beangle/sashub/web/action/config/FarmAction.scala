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
import org.beangle.sashub.model.config.{Engine, Farm, Host, Profile}
import org.beangle.sashub.service.ProfileService
import org.beangle.sashub.web.action.helper.ProfileHelper
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction

/** 群集管理
 */
class FarmAction extends RestfulAction[Farm] {
  var profileService: ProfileService = _

  override def search(): View = {
    ProfileHelper.remember("farm.profile.id")
    super.search()
  }

  override protected def indexSetting(): Unit = {
    ProfileHelper.setRememberedProfile(entityDao)
    put("profiles", profileService.getAll())
  }

  override protected def editSetting(entity: Farm): Unit = {
    val profile = entityDao.get(classOf[Profile], getLongId("farm.profile"))
    val query = OqlBuilder.from(classOf[Engine], "engine")
    query.where("engine.profile is null or engine.profile=:profile", profile)
    put("engines", entityDao.search(query))
    put("profile", profile)
  }

  override protected def saveAndRedirect(farm: Farm): View = {
    val profile = entityDao.get(classOf[Profile], getLongId("farm.profile"))
    farm.profile = profile
    super.saveAndRedirect(farm)
  }
}
