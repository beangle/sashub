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

import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.model.util.Hierarchicals
import org.beangle.sashub.model.ems.{App, Menu, Resource}
import org.beangle.sashub.service.MenuService
import org.beangle.security.authz.Scope

class MenuServiceImpl extends MenuService {
  var entityDao: EntityDao = _

  def move(menu: Menu, location: Menu, index: Int): Unit = {
    menu.parent foreach { p =>
      if (null == location || p != location) {
        menu.parent = None
        entityDao.saveOrUpdate(menu)
        entityDao.refresh(p)
      }
    }

    val nodes =
      if (null != location) {
        Hierarchicals.move(menu, location, index)
      } else {
        val builder = OqlBuilder.from(classOf[Menu], "m")
          .where("m.app = :app and m.parent is null", menu.app)
          .orderBy("m.indexno")
        Hierarchicals.move(menu, entityDao.search(builder).toBuffer, index)
      }
    entityDao.saveOrUpdate(nodes)

    if (null != location) {
      entityDao.refresh(location)
    }
  }

  def importFrom(app: App, xml: scala.xml.Node): Unit = {
    parseMenu(app, None, xml)
  }

  private def parseMenu(app: App, parent: Option[Menu], xml: scala.xml.Node): Unit = {
    (xml \ "resources" \ "resource") foreach { r =>
      val name = (r \ "@name").text.trim
      val title = (r \ "@title").text.trim
      val scope = (r \ "@scope").text.trim
      val enabled = (r \ "@enabled").text.trim
      val fr = findOrCreateResource(app, name, title, scope, enabled)
      entityDao.saveOrUpdate(fr)
    }

    (xml \ "menu") foreach { m =>
      val indexno = (m \ "@indexno").text.trim
      val name = (m \ "@name").text.trim
      val menu = findMenu(app, indexno, name).getOrElse(new Menu)
      menu.name = name
      menu.indexno = indexno
      menu.app = app

      val enName = m \ "@enName"
      menu.enName = if enName.isEmpty then menu.name else enName.text.trim
      val params = m \ "@params"
      if (params.isEmpty || Strings.isBlank(params.text)) {
        menu.params = None
      } else {
        menu.params = Some(params.text.trim)
      }
      val enabled = m \ "@enabled"
      if (enabled.isEmpty) {
        menu.enabled = true
      } else {
        menu.enabled = enabled.text.trim.toBoolean
      }
      (m \ "@fonticon") foreach { fi =>
        menu.fonticon = Some(fi.text.trim)
      }

      val entry = findResource(app, (m \ "@entry").text.trim)
      val resources = m \ "@resources"
      if resources.nonEmpty then
        Strings.split(resources.text) foreach { n =>
          findResource(app, n) foreach { r => menu.resources += r }
        }
      entry foreach { r => menu.resources += r }
      menu.entry = entry
      menu.parent = parent
      entityDao.saveOrUpdate(menu)
      val children = m \ "children"
      if (children.nonEmpty) {
        parseMenu(app, Some(menu), children.head)
      }
    }
  }

  private def findMenu(app: App, indexno: String, name: String): Option[Menu] = {
    val builder = OqlBuilder.from(classOf[Menu], "m").where("m.app=:app", app)
    builder.where("m.indexno=:indexno and m.name=:name", indexno, name)
    entityDao.search(builder).headOption
  }

  private def findResource(app: App, name: String): Option[Resource] = {
    val builder = OqlBuilder.from(classOf[Resource], "fr").where("fr.app=:app", app)
    builder.where("fr.name=:name", name)
    entityDao.search(builder).headOption
  }

  private def findOrCreateResource(app: App, name: String, title: String, scope: String, enabled: String): Resource = {
    findResource(app, name) match {
      case None =>
        val resource = new Resource()
        resource.app = app
        resource.name = name
        resource.title = title
        resource.scope = if Strings.isEmpty(scope) then Scope.Private else Scope.valueOf(scope)
        if (Strings.isEmpty(enabled)) {
          resource.enabled = true
        } else {
          resource.enabled = enabled.toBoolean
        }
        entityDao.saveOrUpdate(resource)
        resource
      case Some(r) => r
    }
  }
}
