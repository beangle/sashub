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

import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{Strings, SystemInfo}
import org.beangle.commons.logging.Logging

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, StandardCopyOption}

/** 原生发行包仓库: native-image 产物(tar.gz)、增量补丁(.diff)及其sha1校验文件。
  *
  * 落盘时上传路径即仓库内的相对路径,如
  * `org/beangle/beangle-ems-portal/4.20.14-SNAPSHOT/beangle-ems-portal-4.20.14-SNAPSHOT-linux-amd64.tar.gz`。
  * 与war/jar一样按maven布局归档: 正式版本落在 `<home>/repository`,开发版(`-SNAPSHOT`)
  * 落在 `<home>/snapshots`,`home` 默认 `~/.m2`;路径里的版本目录(文件所在目录)决定用哪个根。
  * 与 [[SnapshotHelper]] 不同,这里的构件不是war/jar,没有MANIFEST.MF可以解析坐标,坐标完全由上传路径决定。
  *
  * 不含时间戳的SNAPSHOT请求解析到时间戳最新的同名构件,如
  * `beangle-ems-portal-4.20.13_4.20.14-SNAPSHOT-linux-amd64.tar.gz.diff` 解析到
  * `beangle-ems-portal-4.20.13_4.20.14-SNAPSHOT-20260913.101500-1-linux-amd64.tar.gz.diff`。
  */
class NativeHelper(val home: File) extends Logging {

  /** 正式版本(非SNAPSHOT)仓库根: `<home>/repository` */
  def repositoryRoot: File = new File(home, "repository")

  /** 开发版(SNAPSHOT)仓库根: `<home>/snapshots` */
  def snapshotRoot: File = new File(home, "snapshots")

  /** 路径对应的仓库根: 版本目录带 `-SNAPSHOT` 的走快照仓库,其余走正式仓库。 */
  def rootOf(path: String): File = {
    val versionDir = new File(path).getParent
    if (null != versionDir && versionDir.endsWith(NativeHelper.SnapshotMark)) snapshotRoot else repositoryRoot
  }

  def fileOf(path: String): File = new File(rootOf(path), path)

  /** 构件尚未上传时sha1校验文件的暂存目录,内部保持与仓库一致的目录结构 */
  private def pendingDir(path: String): File = new File(rootOf(path), ".pending")

  /** 仓库内的相对路径,用作下载url */
  def relativePath(file: File): String = {
    val absolute = file.getAbsolutePath
    val base = Seq(repositoryRoot, snapshotRoot)
      .map(_.getAbsolutePath)
      .find(root => absolute.startsWith(root + File.separator))
    base.map(root => Strings.replace(absolute.substring(root.length + 1), File.separator, "/"))
      .getOrElse(file.getName)
  }

  /** 解析请求路径对应的实际文件:不带时间戳的SNAPSHOT请求返回时间戳最新的同名构件 */
  def resolve(path: String): Option[File] = {
    if (!NativeHelper.isSafe(path)) {
      None
    } else {
      val direct = fileOf(path)
      if (direct.exists()) {
        Some(direct)
      } else {
        val (artifactPath, checksumSuffix) = NativeHelper.unwrapChecksum(path)
        findLatest(artifactPath) flatMap { artifact =>
          val target = new File(artifact.getParentFile, artifact.getName + checksumSuffix)
          if (target.exists()) Some(target) else None
        }
      }
    }
  }

  /** 查找时间戳最新的同名构件,如xxx-1.0-SNAPSHOT-linux-amd64.tar.gz => xxx-1.0-SNAPSHOT-20260913.101500-1-linux-amd64.tar.gz */
  def findLatest(path: String): Option[File] = {
    val file = fileOf(path)
    val name = file.getName
    val index = name.lastIndexOf(NativeHelper.SnapshotMark)
    val dir = file.getParentFile
    if (index < 0 || null == dir || !dir.exists()) {
      None
    } else {
      val prefix = name.substring(0, index + NativeHelper.SnapshotMark.length) + "-"
      val suffix = name.substring(index + NativeHelper.SnapshotMark.length)
      val children = dir.list()
      val candidates = if (null == children) List.empty[String]
        else children.toList.filter { c =>
          c.length > prefix.length + suffix.length && c.startsWith(prefix) && c.endsWith(suffix) &&
            NativeHelper.TimestampPattern.matches(c.substring(prefix.length, c.length - suffix.length))
        }
      if (candidates.isEmpty) {
        None
      } else {
        val latest = candidates.maxBy(c => new SnaphotTimestamp(c.substring(prefix.length, c.length - suffix.length)))
        val target = new File(dir, latest)
        if (target.exists()) Some(target) else None
      }
    }
  }

  def upload(tmpFile: File, path: String): (Boolean, String) = {
    if (!NativeHelper.isSafe(path)) {
      val error = "非法的上传路径"
      logger.error(s"Failed upload $path due to $error")
      (false, error)
    } else if (NativeHelper.isChecksum(path)) {
      uploadChecksum(tmpFile, path)
    } else {
      uploadArtifact(tmpFile, path)
    }
  }

  private def uploadChecksum(tmpFile: File, path: String): (Boolean, String) = {
    val bytes = Files.readAllBytes(tmpFile.toPath)
    val sha1 = new String(bytes, StandardCharsets.UTF_8).trim
    if (!isSha1Hex(sha1)) {
      val error = "不是合法的SHA-1校验文件"
      logger.error(s"Failed upload $path due to $error")
      (false, error)
    } else {
      val artifactPath = Strings.substringBeforeLast(path, NativeHelper.ChecksumSuffix)
      val target = if (fileOf(artifactPath).exists()) fileOf(path) else new File(pendingDir(path), path)
      saveChecksum(bytes, target)
      (true, "上传成功")
    }
  }

  private def uploadArtifact(tmpFile: File, path: String): (Boolean, String) = {
    val target = fileOf(path)
    target.getParentFile.mkdirs()
    IOs.copy(new FileInputStream(tmpFile), new FileOutputStream(target))
    movePendingChecksum(path, target)
    logger.info(s"Upload ${target}")
    (true, "上传成功")
  }

  private def saveChecksum(bytes: Array[Byte], target: File): Unit = {
    target.getParentFile.mkdirs()
    Files.write(target.toPath, bytes)
    logger.info(s"Upload ${target}")
  }

  /** 构件上传后,把此前先到达并暂存的sha1校验文件移动到构件旁边 */
  private def movePendingChecksum(path: String, artifact: File): Unit = {
    val pending = new File(pendingDir(path), path + NativeHelper.ChecksumSuffix)
    if (pending.exists()) {
      val target = new File(artifact.getParentFile, artifact.getName + NativeHelper.ChecksumSuffix)
      Files.move(pending.toPath, target.toPath, StandardCopyOption.REPLACE_EXISTING)
      logger.info(s"Upload ${target}")
    }
  }

  private def isSha1Hex(value: String): Boolean = {
    value.length == 40 && value.forall(c => Character.digit(c, 16) != -1)
  }
}

object NativeHelper {

  private[repo] val ChecksumSuffix = ".sha1"

  private[repo] val SnapshotMark = "-SNAPSHOT"

  private[repo] val TimestampPattern = """\d{8}\.\d{6}-\d+""".r

  /** 默认仓库: `~/.m2`(正式版 `<home>/repository`,快照版 `<home>/snapshots`) */
  def default: NativeHelper = new NativeHelper(new File(SystemInfo.user.home + "/.m2"))

  /** 是否为sha1校验文件,如xxx.tar.gz.sha1/xxx.tar.gz.diff.sha1 */
  def isChecksum(path: String): Boolean = path.endsWith(ChecksumSuffix)

  /** 上传路径必须是仓库内的相对路径,不接受绝对路径和上跳目录 */
  def isSafe(path: String): Boolean = {
    Strings.isNotEmpty(path) && !path.startsWith("/") && !path.split("[/\\\\]").contains("..")
  }

  /** 拆解sha1校验文件路径:xxx.tar.gz.sha1 => (xxx.tar.gz, .sha1); 普通路径 => (path, "") */
  def unwrapChecksum(path: String): (String, String) = {
    if (isChecksum(path)) (Strings.substringBeforeLast(path, ChecksumSuffix), ChecksumSuffix)
    else (path, "")
  }
}
