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

package org.beangle.sasadmin.web.action.helper

import org.beangle.commons.lang.Strings
import org.beangle.data.dao.EntityDao
import org.beangle.sasadmin.model.config.Profile
import org.beangle.web.action.context.{ActionContext, Params}
import org.beangle.web.servlet.util.CookieUtils

object ProfileHelper {

  def remember(paramName: String): Unit = {
    Params.get(paramName) foreach { id =>
      val c = ActionContext.current
      if Strings.isNotBlank(id) then
        CookieUtils.addCookie(c.request, c.response, "current_profile_id", id.toString, -1)
    }
  }

  def setRememberedProfile(entityDao: EntityDao): Option[Profile] = {
    val c = ActionContext.current
    val profileId = CookieUtils.getCookieValue(c.request, "current_profile_id")
    if Strings.isNotBlank(profileId) then
      val profile=entityDao.get(classOf[Profile], profileId.toLong)
      c.request.setAttribute("profile",profile)
      Some(profile)
    else None
  }
}
