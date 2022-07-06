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

package org.beangle.sashub.service.impl

import _root_.org.beangle.data.dao.{EntityDao, OqlBuilder}
import _root_.org.beangle.sashub.model.config.{Organization, Profile}
import _root_.org.beangle.sashub.service.ProfileService

class DefaultProfileService extends ProfileService {
  var entityDao: EntityDao = _

  def getProfile(name: String): Option[Profile] = {
    val dotIdx = name.indexOf('.')
    if dotIdx == -1 then None
    else
      getOrg(name.substring(0, dotIdx)) match {
        case Some(org) =>
          val profileName = name.substring(dotIdx + 1)
          val query = OqlBuilder.from(classOf[Profile], "profile")
          query.where("profile.org=:org and profile.name=:name", org, profileName)
          query.cacheable()
          entityDao.search(query).headOption
        case None => None
      }
  }

  private def getOrg(name: String): Option[Organization] = {
    val query = OqlBuilder.from(classOf[Organization], "org")
    query.where("org.name=:name", name).cacheable()
    entityDao.search(query).headOption
  }

  override def getAll(): Seq[Profile] = {
    val query = OqlBuilder.from(classOf[Profile], "p")
    query.cacheable()
    val profiles = entityDao.search(query)
    profiles.sortBy(_.qualifiedName)
  }

}
