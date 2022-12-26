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

import _root_.org.beangle.sashub.model.config.AssetGroup
import _root_.org.beangle.sashub.service.ProfileService
import _root_.org.beangle.webmvc.support.action.RestfulAction

class AssetGroupAction extends RestfulAction[AssetGroup] {

  var profileService: ProfileService = _

  override protected def editSetting(context: AssetGroup): Unit = {
    put("profiles", profileService.getAll())
    super.editSetting(context)
  }

  override protected def simpleEntityName: String = {
    "group"
  }
}
