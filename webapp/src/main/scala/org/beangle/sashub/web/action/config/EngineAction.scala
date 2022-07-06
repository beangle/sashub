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

import _root_.org.beangle.commons.lang.Strings
import _root_.org.beangle.sashub.model.config.{Engine, Profile}
import _root_.org.beangle.sashub.service.ProfileService
import _root_.org.beangle.web.action.view.View
import _root_.org.beangle.webmvc.support.action.RestfulAction

/** Engine管理
 */
class EngineAction extends RestfulAction[Engine] {

  var profileService: ProfileService = _

  override protected def editSetting(entity: Engine): Unit = {
    put("profiles", profileService.getAll())
    put("types", Engine.types)
    super.editSetting(entity)
  }

  override protected def saveAndRedirect(entity: Engine): View = {
    entity.listeners.clear()
    entity.jars.clear()

    get("listeners") foreach { listeners =>
      Strings.split(listeners) foreach { listener =>
        entity.listeners += listener.trim()
      }
    }
    get("jars") foreach { jars =>
      Strings.split(jars) foreach { jar =>
        entity.jars += jar.trim()
      }
    }
    super.saveAndRedirect(entity)
  }
}
