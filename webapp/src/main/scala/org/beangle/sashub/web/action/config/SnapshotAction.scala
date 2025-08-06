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
import org.beangle.commons.lang.{Strings, SystemInfo}
import org.beangle.webmvc.support.ActionSupport
import org.beangle.webmvc.view.View

import java.io.{File, FileInputStream, FileOutputStream}
import java.util.jar.Manifest
import java.util.zip.{ZipEntry, ZipFile}

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
      val msg = SnapshotAction.upload(tmpFile, part.get.getSubmittedFileName)
      tmpFile.delete()
      redirect("index", msg)
    } else {
      redirect("index", "没有发现上传的SNAPSHOT.war")
    }
  }

}

object SnapshotAction {

  def main(args: Array[String]): Unit = {
    upload(new File("D:\\workspace\\beangle\\otk\\target\\beangle-otk-ws-0.0.19-20250806.182112-1.war"), "beangle-otk-ws-0.0.19-20250806.182112-1.war")
  }

  def upload(tmpFile: File, fileName: String): String = {
    findManifest(tmpFile) match {
      case None => "没有发现上传的SNAPSHOT.war"
      case Some(manifest) =>
        val attributes = manifest.getMainAttributes
        val symbolicName = attributes.getValue("Bundle-SymbolicName")
        if (null == symbolicName) {
          "找不到MANIFEST.MF中找不到Bundle-SymbolicName属性"
        } else {
          val version = attributes.getValue("Bundle-Version")
          val groupId = Strings.substringBeforeLast(symbolicName, ".")
          val artifactId = Strings.substringAfterLast(symbolicName, ".")
          var path = SystemInfo.user.home + "/.m2/snapshots/"
          path += groupId.replace('.', '/')
          path += "/"
          path += artifactId
          path += "/"
          path += version
          path += "/"
          path += fileName
          new File(path).getParentFile.mkdirs()
          IOs.copy(new FileInputStream(tmpFile), new FileOutputStream(new File(path)))
          "上传成功"
        }
    }
  }

  private def findManifest(file: File): Option[Manifest] = {
    val buffer = new Array[Byte](1024)
    val zipFile = new ZipFile(file)
    val entries = zipFile.entries
    var entry: ZipEntry = null

    // 遍历所有条目查找目标文件
    while (entries.hasMoreElements && null == entry) {
      val currentEntry = entries.nextElement
      val entryName = currentEntry.getName
      val lastSlashIndex = entryName.lastIndexOf('/')
      val entryFileName = if (lastSlashIndex >= 0) entryName.substring(lastSlashIndex + 1) else entryName
      if (entryFileName == "MANIFEST.MF") {
        entry = currentEntry
      }
    }
    if (null != entry) {
      val is = zipFile.getInputStream(entry)
      val manifest = new Manifest(is)
      is.close()
      Some(manifest)
    } else {
      None
    }
  }
}
