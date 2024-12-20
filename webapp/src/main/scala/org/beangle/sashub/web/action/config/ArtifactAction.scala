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

import org.beangle.sashub.model.config.{Artifact, Webapp}
import org.beangle.sashub.service.ProfileService
import org.beangle.sashub.service.config.ArtifactVersionRefresher
import org.beangle.webmvc.annotation.mapping
import org.beangle.webmvc.view.View
import org.beangle.webmvc.support.action.RestfulAction

class ArtifactAction extends RestfulAction[Artifact] {

  var profileService: ProfileService = _

  var artifactVersionRefresher: ArtifactVersionRefresher = _

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

  def updateVersion(): View = {
    val artifacts = entityDao.find(classOf[Artifact], getLongIds("artifact"))
    artifactVersionRefresher.refresh(artifacts)
    redirect("search", "info.save.success")
  }
}
