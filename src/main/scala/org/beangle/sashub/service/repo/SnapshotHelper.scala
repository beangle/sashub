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
import org.beangle.commons.logging.Logging

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.jar.Manifest
import java.util.zip.{ZipEntry, ZipFile}

object SnapshotHelper extends Logging {

  private val ChecksumSuffix = ".sha1"

  private def root: String = SystemInfo.user.home + "/.m2/snapshots/"

  /** 构件尚未上传时sha1校验文件的暂存目录 */
  private def pendingDir: File = new File(root + ".pending")

  /** 是否为war/jar构件的sha1校验文件,如xxx.war.sha1/xxx.jar.sha1 */
  def isChecksum(path: String): Boolean = {
    path.endsWith(".war.sha1") || path.endsWith(".jar.sha1")
  }

  /** 拆解sha1校验文件路径:xxx.war.sha1 => (xxx.war, .sha1); 普通路径 => (path, "") */
  def unwrapChecksum(path: String): (String, String) = {
    if (isChecksum(path)) (Strings.substringBeforeLast(path, ChecksumSuffix), ChecksumSuffix)
    else (path, "")
  }

  /** 是否为快照请求,如xxx-SNAPSHOT.war/xxx-SNAPSHOT.war.sha1 */
  def isLatestRequest(path: String): Boolean = {
    val (artifactPath, _) = unwrapChecksum(path)
    artifactPath.endsWith("-SNAPSHOT" + getExt(artifactPath))
  }

  /** 解析请求路径对应的实际文件:快照请求返回最新时间戳文件,校验文件返回其伴生的sha1文件 */
  def resolve(path: String): Option[File] = {
    if (isLatestRequest(path)) {
      val (artifactPath, checksumSuffix) = unwrapChecksum(path)
      findLatest(artifactPath) flatMap { f =>
        val target = new File(f.getParentFile, f.getName + checksumSuffix)
        if (target.exists()) Some(target) else None
      }
    } else {
      val target = new File(root + path)
      if (target.exists()) Some(target) else None
    }
  }

  def upload(tmpFile: File, fileName: String): (Boolean, String) = {
    if (isChecksum(fileName)) uploadChecksum(tmpFile, fileName)
    else uploadArtifact(tmpFile, fileName)
  }

  private def uploadChecksum(tmpFile: File, fileName: String): (Boolean, String) = {
    val bytes = Files.readAllBytes(tmpFile.toPath)
    val sha1 = new String(bytes, java.nio.charset.StandardCharsets.UTF_8).trim
    if (!isSha1Hex(sha1)) {
      val error = "不是合法的SHA-1校验文件"
      logger.error(s"Failed upload ${fileName} due to $error")
      (false, error)
    } else {
      val artifactName = Strings.substringBeforeLast(fileName, ChecksumSuffix)
      findArtifact(artifactName) match {
        case Some(artifact) =>
          saveChecksum(bytes, artifact.getParentFile, fileName)
        case None =>
          saveChecksum(bytes, pendingDir, fileName)
          findArtifact(artifactName) foreach { artifact =>
            movePendingChecksum(fileName, artifact.getParentFile)
          }
      }
      (true, "上传成功")
    }
  }

  private def saveChecksum(bytes: Array[Byte], dir: File, fileName: String): Unit = {
    val target = new File(dir, fileName)
    target.getParentFile.mkdirs()
    Files.write(target.toPath, bytes)
    logger.info(s"Upload ${target}")
  }

  /** 在仓库中查找与校验文件同名的构件,存在多个时取最近修改的 */
  private def findArtifact(artifactName: String): Option[File] = {
    val candidates = Collections.newBuffer[File]
    collectArtifacts(new File(root), artifactName, candidates)
    if (candidates.isEmpty) None
    else Some(candidates.maxBy(_.lastModified()))
  }

  private def collectArtifacts(dir: File, name: String, result: collection.mutable.Buffer[File]): Unit = {
    val children = dir.listFiles()
    if (null != children) {
      children foreach { child =>
        if (child.isDirectory) collectArtifacts(child, name, result)
        else if (child.getName == name) result += child
      }
    }
  }

  private def uploadArtifact(tmpFile: File, fileName: String): (Boolean, String) = {
    findManifest(tmpFile) match {
      case None =>
        val error = "上传的文件中没有找到MANIFEST.MF"
        logger.error(s"Failed upload ${fileName} due to $error")
        (false, error)
      case Some(manifest) =>
        val attributes = manifest.getMainAttributes
        val groupId = attributes.getValue("Implementation-Vendor-Id")
        val artifactId = attributes.getValue("Implementation-Title")
        val version = attributes.getValue("Implementation-Version")

        if (null == groupId || null == version || null == version) {
          val error = "找不到MANIFEST.MF中找不到Implementation-*属性"
          logger.error(s"Failed upload ${fileName} due to $error")
          (false, error)
        } else {
          var path = root
          path += groupId.replace('.', '/')
          path += "/"
          path += artifactId
          path += "/"
          path += version
          path += "/"
          path += fileName
          val target = new File(path)
          target.getParentFile.mkdirs()
          IOs.copy(new FileInputStream(tmpFile), new FileOutputStream(target))
          movePendingChecksum(fileName + ChecksumSuffix, target.getParentFile)
          logger.info(s"Upload ${path}")
          (true, "上传成功")
        }
    }
  }

  /** 构件上传后,把此前先到达并暂存的sha1校验文件移动到构件旁边 */
  private def movePendingChecksum(fileName: String, dir: File): Unit = {
    val pending = new File(pendingDir, fileName)
    if (pending.exists()) {
      val target = new File(dir, fileName)
      Files.move(pending.toPath, target.toPath, StandardCopyOption.REPLACE_EXISTING)
      logger.info(s"Upload ${target}")
    }
  }

  private def isSha1Hex(value: String): Boolean = {
    value.length == 40 && value.forall(c => Character.digit(c, 16) != -1)
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
