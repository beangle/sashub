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

/** 组件
 */
class Artifact extends LongId {
  var groupId: String = _
  var artifactId: String = _
  var packaging: String = _
  var classifier: Option[String] = None
  var latestVersion: String = _
  var title: String = _
  var description: String = _
  var resolveSupport: Boolean = _
  var jspSupport: Boolean = false
  var websocketSupport: Boolean = false
  var profile: Option[Profile] = None
  /** 体系结构 */
  var arch: Arch = Arch.Noarch
  /** 推荐上下文设置
   */
  var contextPath: String = _

  def name: String = s"$groupId $artifactId"

  def gav(version: String): String = {
    classifier match {
      case Some(c) => s"$groupId:$artifactId:$packaging:$c:$version"
      case None => s"$groupId:$artifactId:$packaging:$version"
    }
  }

}
