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

package org.beangle.sashub.service.config

import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.commons.logging.Logging
import org.beangle.commons.net.http.HttpUtils
import org.beangle.sashub.model.config.Artifact

class DefaultArtifactVersionRefresher extends DaoJob, Logging, ArtifactVersionRefresher {

  override def execute(): Unit = {
    refresh(entityDao.getAll(classOf[Artifact]))
  }

  override def refresh(artifacts: Iterable[Artifact]): Unit = {
    val template = "https://repo1.maven.org/maven2/{groupDir}/{artifactId}/maven-metadata.xml"
    val msgList = Collections.newBuffer[String]
    artifacts foreach { artifact =>
      val groupDir = Strings.replace(artifact.groupId, ".", "/")
      val artifactMetadataUrl = s"https://repo1.maven.org/maven2/${groupDir}/${artifact.artifactId}/maven-metadata.xml"
      val res = HttpUtils.access(artifactMetadataUrl)
      if (res.isOk) {
        val xml = scala.xml.XML.load(artifactMetadataUrl)
        (xml \ "versioning" \ "latest") foreach { e =>
          artifact.latestVersion = e.text.trim()
          msgList.addOne(s"${artifact.groupId}:${artifact.artifactId}:${artifact.latestVersion}")
        }
      }
    }
    if (msgList.nonEmpty) {
      logger.info(s"auto update ${msgList.size} artifact versions")
      logger.info(s"${msgList.mkString(",")}")
      entityDao.saveOrUpdate(artifacts)
    }
  }
}
