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

package org.beangle.sashub.model.config

import org.beangle.data.model.pojo.Named
import org.beangle.data.model.{Component, LongId}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Farm extends LongId with Named {

  var profile: Profile = _

  var engine: Engine = _

  var serverOptions: Option[String] = None

  var proxyOptions: Option[String] = None

  var enableAccessLog: Boolean = _

  var hosts: mutable.Buffer[Host] = new ArrayBuffer[Host]

  var servers: mutable.Buffer[Server] = new mutable.ArrayBuffer[Server]

  var maxHeapSize: Int = _

  var http: Connector = _

  def newServer(name: String, http: Int): Server = {
    val server = new Server()
    server.update(name, http)
    server.farm = this
    server
  }
}

class Connector extends Component {
  var maxThreads: Int = 200
}
