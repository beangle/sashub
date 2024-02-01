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

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Named

import scala.collection.mutable

class PlatformFeature extends LongId with Named {

  var scripts: mutable.Buffer[PlatformFeatureScript] = Collections.newBuffer[PlatformFeatureScript]

  var logo: String = _

  var version: String = _

  var description: String = _

  var dependencies: mutable.Buffer[PlatformFeature] = Collections.newBuffer

  def support(platform: Platform): Boolean = {
    scripts.exists(_.platform == platform)
  }
}
