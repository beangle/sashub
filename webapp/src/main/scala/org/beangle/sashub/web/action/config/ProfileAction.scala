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

import _root_.org.beangle.sashub.model.config.{AssetGroup, Organization, Profile}
import _root_.org.beangle.web.action.view.View
import _root_.org.beangle.webmvc.support.action.RestfulAction

class ProfileAction extends RestfulAction[Profile] {

  override protected def editSetting(entity: Profile): Unit = {
    put("orgs", entityDao.getAll(classOf[Organization]))
    put("assetGroups", entityDao.getAll(classOf[AssetGroup]))
    if !entity.persisted then
      entity.localRepo = "~/.m2/repository"
      entity.remoteRepo = "https://maven.aliyun.com/nexus/content/groups/public"
      entity.enableHttps = false
      entity.forceHttps = false
    super.editSetting(entity)
  }

  override protected def saveAndRedirect(profile: Profile): View = {
    profile.assetGroups.clear()
    profile.assetGroups ++= entityDao.find(classOf[AssetGroup], intIds("assetGroup"))
    super.saveAndRedirect(profile)
  }
}
