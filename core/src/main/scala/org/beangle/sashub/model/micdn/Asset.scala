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

package org.beangle.sashub.model.micdn

import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{Named, Remark}

import scala.collection.mutable

class Asset extends IntId, Named, Remark {
  var group: AssetGroup = _
  var base: String = _
  var bundles: mutable.Buffer[AssetBundle] = Collections.newBuffer[AssetBundle]

  @transient
  def bundleUris: String = {
    bundles.map(b => b.uri).mkString("\n")
  }

  def bundleUris_=(str: String): Unit = {
    val parts = Strings.split(str).toSet
    val removed = bundles.filterNot(x => parts.contains(x.uri))
    bundles --= removed
    parts foreach { p =>
      if !bundles.exists(_.uri == p) then
        bundles.addOne(AssetBundle(this, p))
    }
  }
}
