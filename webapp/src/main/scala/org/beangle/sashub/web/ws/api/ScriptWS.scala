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

import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.sashub.model.config.{Host, PlatformFeature, Profile}
import org.beangle.sashub.service.ProfileService
import org.beangle.web.action.annotation.{mapping, param}
import org.beangle.web.action.support.{ActionSupport, ParamSupport, ServletSupport}
import org.beangle.web.action.view.{Status, View}

import scala.collection.mutable

class ScriptWS extends ActionSupport with ParamSupport with ServletSupport {

  var profileService: ProfileService = _
  var entityDao: EntityDao = _

  @mapping("{host}")
  def index(@param("profile") name: String, @param("host") host: String): View = {
    getProfile(name) match {
      case Some(profile) =>
        val query = OqlBuilder.from(classOf[Host], "host")
        query.where("host.profile=:profile", profile)
        query.where("host.ip=:host",host)
        entityDao.search(query).headOption match {
          case None => Status.NotFound
          case Some(h) =>
            val features = dependencySort(h.features)
            val scripts = features.map(f => f.scripts.find(s => s.platform == h.platform)).flatten
            put("platform",h.platform)
            put("scripts", scripts)
            forward()
        }
      case None => Status.NotFound
    }
  }

  private def dependencySort(features: Iterable[PlatformFeature]): List[PlatformFeature] = {
    val priorities = Collections.newMap[PlatformFeature, Int]
    features.iterator foreach { f =>
      priorities.put(f, 1)
    }
    features.iterator foreach { f =>
      cascadeUpdatePriority(f, priorities, features.size)
    }
    priorities.toBuffer.sortBy(_._2).reverse.map(_._1).toList
  }

  private def cascadeUpdatePriority(feature: PlatformFeature, priorities: mutable.Map[PlatformFeature, Int], maxValue: Int): Unit = {
    feature.dependencies foreach { dependency =>
      val oldValue = priorities.getOrElseUpdate(dependency,1)
      if oldValue < maxValue then
        priorities.update(dependency, oldValue + 1)
        cascadeUpdatePriority(dependency, priorities, maxValue)
    }
  }

  private def getProfile(name: String): Option[Profile] = {
    profileService.getProfile(name).headOption
  }
}
