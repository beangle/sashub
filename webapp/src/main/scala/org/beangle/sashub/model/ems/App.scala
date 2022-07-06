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

package org.beangle.sashub.model.ems

import _root_.org.beangle.data.model.IntId
import _root_.org.beangle.data.model.pojo.Named

/** EMS应用信息
 */
class App extends IntId with Named {
  /** 分组 */
  var group: AppGroup = _
  /** artifactId */
  var artifactId: String = _
  /** 标题 */
  var title: String = _
  /** 导航类型 */
  var navStyle: String = _
  /** 外部依赖(数据源、其他服务) */
  var services: Option[String] = None
  /** 部署上下文 */
  var base: String = _
  /** 入口地址 */
  var url: String = _

  def qualifiedTitle: String = {
    group.title + "." + title
  }
}
