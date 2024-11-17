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

import org.beangle.sashub.model.ems.{App, Menu, Resource}
import org.beangle.webmvc.view.View
import org.beangle.webmvc.support.action.RestfulAction
import jakarta.servlet.http.Part
import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.util.Hierarchicals
import org.beangle.sashub.service.MenuService
import org.beangle.sashub.web.action.helper.AppHelper
import org.beangle.webmvc.annotation.ignore

class MenuAction extends RestfulAction[Menu] {
  var menuService: MenuService = _

  protected override def indexSetting(): Unit = {
    val apps = entityDao.getAll(classOf[App])
    AppHelper.putApps(apps, "menu.app.id", entityDao)
  }

  override def search(): View = {
    AppHelper.remember("menu.app.id")
    super.search()
    forward()
  }

  protected override def editSetting(menu: Menu): Unit = {
    //search profile in app scope
    val app = entityDao.get(classOf[App], menu.app.id)

    val folders = Collections.newBuffer[Menu]
    // 查找可以作为父节点的菜单
    val folderBuilder = OqlBuilder.from(classOf[Menu], "m")
    folderBuilder.where("m.entry is null and m.app=:app", app)
    folderBuilder.orderBy("m.indexno")
    val rs = entityDao.search(folderBuilder)
    folders ++= rs
    menu.parent foreach { p =>
      if (!folders.contains(p)) folders += p
    }
    folders --= Hierarchicals.getFamily(menu)
    put("parents", folders)

    val alternatives = Collections.newBuffer[Resource]
    val resources = Collections.newBuffer[Resource]
    val funcBuilder = OqlBuilder.from(classOf[Resource], "r").where("r.app=:app", app)
    alternatives ++= entityDao.search(funcBuilder)
    resources ++= alternatives
    alternatives --= menu.resources
    put("alternatives", alternatives)
    put("resources", resources)

    if (!menu.persisted) {
      menu.enabled = true
    }
  }

  @ignore
  protected override def removeAndRedirect(entities: Seq[Menu]): View = {
    val parents = Collections.newBuffer[Menu]
    for (menu <- entities) {
      menu.parent foreach { p =>
        p.children -= menu
        parents += p
      }
    }
    entityDao.saveOrUpdate(parents)
    super.removeAndRedirect(entities)
  }

  @ignore
  protected override def saveAndRedirect(menu: Menu): View = {
    val resources = entityDao.find(classOf[Resource], getIntIds("resource"))
    menu.resources.clear()
    menu.resources ++= resources
    //检查入口资源是否在使用资源列表中
    menu.entry.foreach { entry =>
      if (!resources.contains(entry)) {
        menu.resources += entry
      }
    }

    val newParentId = getInt("parent.id")
    val indexno = getInt("indexno", 0)
    var parent: Menu = null
    if (newParentId.isDefined) parent = entityDao.get(classOf[Menu], newParentId.get)

    menuService.move(menu, parent, indexno)
    if (!menu.enabled) {
      val family = Hierarchicals.getFamily(menu)
      for (one <- family) one.enabled = false
      entityDao.saveOrUpdate(family)
    }
    entityDao.evict(menu)
    if (null != parent) {
      entityDao.evict(parent)
    }
    redirect("search", "info.save.success")
  }

  def exportToXml(): View = {
    val query = getQueryBuilder
    query.limit(null)
    query.where("menu.parent is null")
    val menus = entityDao.search(query)
    put("menus", menus)

    if menus.nonEmpty then
      val app = menus.head.app
      put("resources", entityDao.findBy(classOf[Resource], "app", app))
    else
      put("resources",List.empty[Resource])
    forward()
  }

  def importFromXml(): View = {
    val parts = getAll("menufile", classOf[Part])
    if (parts.isEmpty) {
      forward()
    } else {
      val app = entityDao.get(classOf[App], getInt("menu.app.id").get)
      menuService.importFrom(app, scala.xml.XML.load(parts.head.getInputStream))
      redirect("search", "info.save.success")
    }
  }

}
