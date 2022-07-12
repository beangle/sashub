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

import _root_.org.beangle.sashub.model.config.{Artifact, Profile, Webapp}
import _root_.org.beangle.sashub.service.ProfileService
import _root_.org.beangle.web.action.annotation.mapping
import _root_.org.beangle.web.action.view.View
import _root_.org.beangle.webmvc.support.action.RestfulAction

class ArtifactAction extends RestfulAction[Artifact] {

  var profileService: ProfileService = _

  @mapping(value = "{id}")
  override def info(id: String): View = {
    val artifact = entityDao.get(classOf[Artifact], id.toLong)
    put("webapps", entityDao.findBy(classOf[Webapp], "artifact", artifact))
    super.info(id)
  }

  override protected def editSetting(artifact: Artifact): Unit = {
    if (!artifact.persisted) artifact.resolveSupport = true
    put("profiles", profileService.getAll())
    super.editSetting(artifact)
  }
}