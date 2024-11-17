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

package org.beangle.sashub.web.action.ems

import org.beangle.sashub.model.ems.{App, AppGroup, Menu}
import org.beangle.webmvc.view.View
import org.beangle.webmvc.support.action.RestfulAction

class AppAction extends RestfulAction[App] {

  override protected def indexSetting(): Unit = {
    put("groups", entityDao.getAll(classOf[AppGroup]))
  }

  override protected def editSetting(entity: App): Unit = {
    put("groups", entityDao.getAll(classOf[AppGroup]))
    super.editSetting(entity)
  }
}
