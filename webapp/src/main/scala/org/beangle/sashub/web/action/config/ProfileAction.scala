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

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.sashub.model.config.{Organization, Profile}
import org.beangle.sashub.model.micdn.AssetGroup
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction

class ProfileAction extends RestfulAction[Profile] {

  override protected def editSetting(profile: Profile): Unit = {
    put("orgs", entityDao.getAll(classOf[Organization]))
    put("assetGroups", entityDao.getAll(classOf[AssetGroup]))
    put("profileAssetGroups", getProfileAssetGroups(profile))
    if !profile.persisted then
      profile.localRepo = "~/.m2/repository"
      profile.remoteRepo = "https://maven.aliyun.com/nexus/content/groups/public"
      profile.enableHttps = false
      profile.forceHttps = false
    super.editSetting(profile)
  }

  override protected def saveAndRedirect(profile: Profile): View = {
    val newGroups = entityDao.find(classOf[AssetGroup], getIntIds("assetGroup"))
    var oldGroups = Seq.empty[AssetGroup]
    if (profile.persisted) {
      oldGroups = getProfileAssetGroups(profile)
      Collections.subtract(oldGroups, newGroups) foreach { g => g.profiles -= profile }
    }
    Collections.subtract(newGroups, oldGroups) foreach { g => g.profiles += profile }
    entityDao.saveOrUpdate(oldGroups, newGroups)

    super.saveAndRedirect(profile)
  }

  private def getProfileAssetGroups(profile: Profile): Seq[AssetGroup] = {
    val gQuery = OqlBuilder.from(classOf[AssetGroup], "g")
    entityDao.search(gQuery)
  }
}
