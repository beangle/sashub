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

/**
 * 操作系统功能的检查和安装脚本
 */
class PlatformFeatureScript extends LongId {

  var feature: PlatformFeature = _

  /** 软件平台(os) */
  var platform: Platform = _

  /** 相应的脚本 */
  var scripts: String = _

}
