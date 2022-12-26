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

import org.beangle.data.model.IntId

object AssetBundle {
  def apply(asset: Asset, uri: String): AssetBundle = {
    if (uri.contains("/")) {
      val location = if uri.startsWith("file://") then uri.substring("file://".length) else uri
      val sb = new AssetBundle
      sb.asset = asset
      sb.location = Some(location)
      sb
    } else {
      val gav = if uri.startsWith("gav://") then uri.substring("gav://".length) else uri
      val sb = new AssetBundle
      sb.asset = asset
      sb.gav = Some(gav)
      sb
    }
  }
}

class AssetBundle extends IntId {
  var gav: Option[String] = None
  var location: Option[String] = None
  var asset: Asset = _

  def uri: String = {
    gav match
      case None => location match
        case None => ""
        case Some(l) => s"file://$l"
      case Some(g) => s"gav://$g"
  }
}
