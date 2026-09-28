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

import org.beangle.commons.bean.Properties
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Named
import org.beangle.commons.collection.Collections
import org.beangle.sashub.model.micdn.AssetGroup

import scala.collection.mutable
import scala.compiletime.uninitialized

class Profile extends LongId with Named {

  var org: Organization = uninitialized

  var title: String = uninitialized

  var key: String = uninitialized

  var sasVersion: String = uninitialized

  var localRepo: String = uninitialized

  var remoteRepo: String = uninitialized

  var ip: String = uninitialized

  var hostname: Option[String] = None

  var httpPort: Int = 80

  var proxyEngine: String = uninitialized

  var maxconn: Int = 30000

  var enableHttps: Boolean = false

  var httpsPort: Int = 443

  var sslCiphers: Option[String] = None

  var sslProtocols: Option[String] = None

  var forceHttps: Boolean = true

  def qualifiedName: String = {
    if null == org then name else org.name + "." + name
  }

  def qualifiedTitle: String = {
    if null == org then title else org.title + "." + title
  }
}
