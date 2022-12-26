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

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Named

import scala.collection.mutable

class Webapp extends LongId {
  var artifact: Artifact = _
  var profile: Profile = _
  var version: String = _

  var unpack: Boolean = false
  var targets: mutable.Buffer[Server] = new mutable.ArrayBuffer[Server]
  var contextPath: String = _

  def gav: String = {
    artifact.gav(version)
  }
}
