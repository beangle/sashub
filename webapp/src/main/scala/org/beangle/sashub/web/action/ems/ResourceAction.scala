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

import _root_.org.beangle.sashub.model.ems.{App, Menu, Resource}
import _root_.org.beangle.web.action.view.View
import _root_.org.beangle.webmvc.support.action.RestfulAction
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.Entity
import org.beangle.sashub.web.action.helper.AppHelper
import org.beangle.security.authz.Scope

class ResourceAction extends RestfulAction[Resource] {
  protected override def indexSetting(): Unit = {
    AppHelper.putApps(entityDao.getAll(classOf[App]), "resource.app.id", entityDao)
  }

  override def info(id: String): View = {
    val entity: Resource = getModel(id.toLong)
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.join("menu.resources", "r").where("r.id=:resourceId", entity.id)
      .orderBy("menu.app.id,menu.indexno")

    put(simpleEntityName, entity)
    put("menus", entityDao.search(query))
    forward()
  }

  override def search(): View = {
    AppHelper.remember("resource.app.id")
    super.search()
    forward()
  }

  protected override def editSetting(resource: Resource): Unit = {
    put("apps", entityDao.getAll(classOf[App]))
    if (!resource.persisted) {
      resource.scope = Scope.Private
      resource.enabled = true
    }
  }
}
