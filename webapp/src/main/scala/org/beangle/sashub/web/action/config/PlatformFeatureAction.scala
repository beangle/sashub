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

import _root_.org.beangle.sashub.model.config.{Platform, PlatformFeature, PlatformFeatureScript}
import _root_.org.beangle.web.action.view.View
import _root_.org.beangle.webmvc.support.action.RestfulAction
import _root_.org.beangle.commons.lang.Strings

class PlatformFeatureAction extends RestfulAction[PlatformFeature] {

  override protected def saveAndRedirect(feature: PlatformFeature): View = {
    val dependencyIds = getAll("dependencyId", classOf[Long])
    feature.dependencies.clear()
    feature.dependencies ++= entityDao.find(classOf[PlatformFeature], dependencyIds)
    super.saveAndRedirect(feature)
  }

  override protected def editSetting(feature: PlatformFeature): Unit = {
    val dependencies = entityDao.getAll(classOf[PlatformFeature]).toBuffer
    if (feature.persisted) dependencies -= feature
    put("dependencies", dependencies)
    super.editSetting(feature)
  }

  def addScript(): View = {
    put("platforms", entityDao.getAll(classOf[Platform]))
    val feature = entityDao.get(classOf[PlatformFeature], longId("feature"))
    val script = new PlatformFeatureScript
    script.feature = feature
    put("script", script)
    forward("scriptForm")
  }

  def saveScript(): View = {
    val script = populateEntity(classOf[PlatformFeatureScript], "script")
    script.scripts = Strings.replace(script.scripts.trim(), "\r", "")
    entityDao.saveOrUpdate(script)
    redirect("info", "id=" + script.feature.id, "info.save.success")
  }

  def removeScript(): View = {
    val script = entityDao.get(classOf[PlatformFeatureScript], longId("script"))
    entityDao.remove(script)
    redirect("info", "id=" + script.feature.id, "info.remove.success")
  }

  def editScript(): View = {
    put("platforms", entityDao.getAll(classOf[Platform]))
    val script = entityDao.get(classOf[PlatformFeatureScript], longId("script"))
    put("script", script)
    forward("scriptForm")
  }
}
