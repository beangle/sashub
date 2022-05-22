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

package org.beangle.sasadmin.model.config

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{Named, Remark}
import org.beangle.sasadmin.model.config.Engine.jee

import scala.collection.mutable

object Engine {
  val jee = Seq("tomcat", "undertow", "jetty")
  val types = jee ++ Seq("vibed", "any")
}

class Engine extends IntId with Named with Remark {

  var profile: Option[Profile] = None

  var typ: String = _

  var version: String = _

  var jspSupport: Boolean = false

  var websocketSupport: Boolean = false

  var listeners: mutable.Buffer[String] = new mutable.ArrayBuffer[String]

  var jars: mutable.Buffer[String] = new mutable.ArrayBuffer[String]

  def isJavaEngine: Boolean = {
    jee.contains(this.typ)
  }
}