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

import org.beangle.data.orm.MappingModule
import org.beangle.sashub.model.micdn.{Asset, AssetBundle, AssetGroup}

class DefaultMapping extends MappingModule {
  override def binding(): Unit = {
    bind[Organization]
    bind[Profile]
    bind[Engine]
    bind[Host].declare { e =>
      e.cpu is length(200)
    }
    bind[Farm].declare { e =>
      e.servers is depends("farm")
    }
    bind[Server]
    bind[Artifact]
    bind[Webapp]
    bind[Platform]
    bind[PlatformFeature] declare { e =>
      e.scripts is depends("feature")
      e.dependencies is joinColumn("from_feature_id")
    }

    bind[PlatformFeatureScript] declare { e =>
      e.scripts is length(4000)
    }

    bind[Asset].declare { e =>
      e.bundles is depends("asset")
    }
    bind[AssetBundle]

    bind[AssetGroup]
  }
}
