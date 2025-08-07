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

package org.beangle.sashub.service.repo

import org.beangle.commons.collection.Collections
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{Objects, Strings, SystemInfo}
import org.beangle.commons.logging.{Logger, Logging}

import java.io.{File, FileInputStream, FileOutputStream}
import java.util.jar.Manifest
import java.util.zip.{ZipEntry, ZipFile}

object SnapshotHelper extends Logging {

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
          logger.info(s"Upload ${path}")
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

  private def getExt(path: String): String = {
    var ext = Strings.substringAfterLast(path, ".")
    if (Strings.isNotEmpty(ext)) ext = "." + ext
    ext
  }

  def findLatest(path: String): Option[File] = {
    val root = SystemInfo.user.home + "/.m2/snapshots/"
    val localPath = root + path
    val ext = getExt(path)
    val parent = new File(localPath).getParentFile
    if (parent.exists()) {
      val prefix = Strings.substringBefore(Strings.substringAfterLast(path, "/"), "-SNAPSHOT" + ext) + "-"
      val children = parent.list()
      val snapshots = if null == children then List.empty else children.toList
      val versions = Collections.newBuffer[SnaphotTimestamp]
      for (s <- snapshots) {
        if (s.startsWith(prefix) && s.endsWith(ext)) {
          val ts = Strings.substringBetween(s, prefix, ext)
          versions += new SnaphotTimestamp(ts)
        }
      }
      val rs = versions.sorted
      if (rs.isEmpty) None
      else {
        val filePath = root + Strings.replace(path, "SNAPSHOT" + ext, rs.last.toString + ext)
        val target = new File(filePath)
        if (target.exists()) Some(target) else None
      }
    } else {
      None
    }
  }
}


case class SnaphotTimestamp(timestamp: String, build: Int) extends Ordered[SnaphotTimestamp] {
  def this(s: String) = {
    this(Strings.substringBeforeLast(s, "-"), Strings.substringAfterLast(s, "-").toInt)
  }

  override def toString: String = {
    s"${timestamp}-${build}"
  }

  override def compare(that: SnaphotTimestamp): Int = {
    Objects.compareBuilder.add(this.timestamp, that.timestamp).add(this.build, that.build).build()
  }
}

