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

import org.beangle.commons.lang.Strings
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Named

/** 服务器
 */
class Server extends LongId with Named {
  var farm: Farm = _

  var host: Host = _

  var maxHeapSize: Int = _

  var httpPort: Int = _

  var enableAccessLog: Boolean = _

  var proxyOptions: Option[String] = None

  var proxyHttpPort: Option[Int] = None

  def update(name: String, http: Int): Unit = {
    this.name = name
    this.httpPort = http
  }

  def qualifiedName: String = {
    if (Strings.isNotBlank(farm.name)) farm.name + "." + name
    else name
  }

}
