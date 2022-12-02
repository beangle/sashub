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

import _root_.org.beangle.data.model.LongId
import _root_.org.beangle.data.model.pojo.Named
import org.beangle.commons.collection.Collections

import scala.collection.mutable

class Host extends LongId with Named {

  var profile: Profile = _

  var ip: String = _

  /** 内存多少兆 */
  var memory: Int = _

  /** 软件平台(os) */
  var platform: Platform = _

  /** 操作系统版本 */
  var platformVersion: String = _

  /** CPU核心数 */
  var cores: Int = _

  /** CPU描述 */
  var cpu: String = _

  /** 操作系统特性 */
  var features: mutable.Set[PlatformFeature] = Collections.newSet[PlatformFeature]

  /** 体系结构 */
  var arch: Arch = Arch.Noarch

  def title: String = s"$name $ip"
}
