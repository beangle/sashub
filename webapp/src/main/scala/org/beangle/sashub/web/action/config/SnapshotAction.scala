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

package org.beangle.sashub.web.action.config

import jakarta.servlet.http.Part
import org.beangle.commons.io.IOs
import org.beangle.sashub.service.repo.SnapshotHelper
import org.beangle.webmvc.support.ActionSupport
import org.beangle.webmvc.view.View

import java.io.FileOutputStream

/** 管理版本号为SNAPSHOT的war包
 */
class SnapshotAction extends ActionSupport {

  def index(): View = {
    forward()
  }

  def upload(): View = {
    val part = get("warfile", classOf[Part])
    val tmpFile = java.nio.file.Files.createTempFile("artifact", "war").toFile
    if (part.nonEmpty) {
      IOs.copy(part.get.getInputStream, new FileOutputStream(tmpFile))
      val msg = SnapshotHelper.upload(tmpFile, part.get.getSubmittedFileName)
      tmpFile.delete()
      redirect("index", msg)
    } else {
      redirect("index", "没有发现上传的SNAPSHOT.war")
    }
  }

}
