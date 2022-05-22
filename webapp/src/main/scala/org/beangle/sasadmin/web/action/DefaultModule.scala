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

package org.beangle.sasadmin.web.action

import org.beangle.cdi.bind.BindModule
import org.beangle.sasadmin.web.action.config

class DefaultModule extends BindModule {

  protected override def binding(): Unit = {
    bind(classOf[config.OrgAction])
    bind(classOf[config.ProfileAction])
    bind(classOf[config.EngineAction])
    bind(classOf[config.FarmAction])
    bind(classOf[config.ServerAction])
    bind(classOf[config.HostAction])
    bind(classOf[config.ArtifactAction])
    bind(classOf[config.WebappAction])
    bind(classOf[IndexAction])
  }

}
