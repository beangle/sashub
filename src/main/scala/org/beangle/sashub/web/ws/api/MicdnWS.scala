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

package org.beangle.sashub.web.ws.api

import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.sashub.model.config.*
import org.beangle.sashub.model.micdn.{Asset, AssetGroup}
import org.beangle.sashub.service.ProfileService
import org.beangle.webmvc.annotation.{action, mapping, param}
import org.beangle.webmvc.support.{ActionSupport, ParamSupport, ServletSupport}
import org.beangle.webmvc.view.{Status, View}

import scala.collection.mutable

/**
 * 获取配置信息
 */
class MicdnWS extends ActionSupport with ParamSupport with ServletSupport {

  var profileService: ProfileService = _
  var entityDao: EntityDao = _

  def asset(@param("profile") name: String): View = {
    val profiles = profileService.getProfile(name)
    if (profiles.size != 1) return Status.NotFound

    val profile = profiles.head
    put("profile", profile)
    val gQuery = OqlBuilder.from(classOf[AssetGroup], "g")
    gQuery.where(":profile in elements(g.profiles)", profile)
    val groups = entityDao.search(gQuery)
    if (groups.isEmpty) {
      put("assets", List.empty[Asset])
    } else {
      val query = OqlBuilder.from(classOf[Asset], "asset")
      query.where("asset.group in(:groups)", groups)
      query.orderBy("asset.base")
      put("assets", entityDao.search(query))
    }
    forward()
  }
}
