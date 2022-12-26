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

package org.beangle.sashub.model.ems

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{Enabled, Hierarchical, Named, Remark}

import scala.collection.mutable

class Menu extends IntId with Named with Enabled with Hierarchical[Menu] with Remark {
  var app: App = _
  var enName: String = _
  var entry: Option[Resource] = None
  var params: Option[String] = None
  var resources: mutable.Set[Resource] = new mutable.HashSet[Resource]
  var fonticon: Option[String] = None

  def description: String = {
    indexno + " " + name
  }

  override def compare(m: Menu): Int = {
    indexno.compareTo(m.indexno)
  }
}
