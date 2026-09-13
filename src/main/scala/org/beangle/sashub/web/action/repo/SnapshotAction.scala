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

package org.beangle.sashub.web.action.repo

import org.beangle.commons.codec.binary.Base64
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{Strings, SystemInfo}
import org.beangle.ems.app.EmsApp
import org.beangle.sashub.service.repo.SnapshotHelper
import org.beangle.webmvc.annotation.{mapping, param}
import org.beangle.webmvc.context.ActionContext
import org.beangle.webmvc.support.ActionSupport
import org.beangle.webmvc.view.{Status, View}

import java.io.{File, FileInputStream, FileOutputStream}

/** 管理快照构件(war/jar)及其sha1校验文件。
  *
  * 上传地址为 `/repo/snapshot/upload/{文件名}`,例如
  * `/repo/snapshot/upload/beangle-commons-5.0.0-20250803.132600-31.jar`
  *
  * 下载地址为 `/repo/snapshot/{仓库内相对路径}`(GET/HEAD),例如
  *
  * 1. 下载带时间戳的具体构件:
  * {{{
  * curl -O https://sas.openurp.net/sas/repo/snapshot/org/beangle/commons/beangle-commons/5.0.0-SNAPSHOT/beangle-commons-5.0.0-20250803.132600-31.jar
  * }}}
  *
  * 2. 用不带时间戳的SNAPSHOT别名下载最新构件(重定向到上者,`HEAD` 的响应头 `latest` 给出实际文件名):
  * {{{
  * curl -O https://sas.openurp.net/sas/repo/snapshot/org/beangle/commons/beangle-commons/5.0.0-SNAPSHOT/beangle-commons-5.0.0-SNAPSHOT.jar
  * }}}
  *
  * 3. 校验文件遵循同样的规则,把上面的文件名换成 `beangle-commons-5.0.0-SNAPSHOT.jar.sha1` 即可。
  */
class SnapshotAction extends ActionSupport {

  @mapping(value = "{path*}", methods = "head")
  def access(): View = {
    val path = getPath()
    val res = ActionContext.current.response
    if (SnapshotHelper.isLatestRequest(path)) {
      SnapshotHelper.resolve(path) match {
        case None => Status.NotFound
        case Some(f) =>
          res.addHeader("latest", f.getName)
          res.addDateHeader("last-modified", f.lastModified())
          Status.Ok
      }
    } else {
      val root = SystemInfo.user.home + "/.m2/snapshots/"
      val target = new File(root + path)
      if (target.exists()) {
        res.addDateHeader("last-modified", target.lastModified())
        Status.Ok
      } else {
        Status.NotFound
      }
    }
  }

  @mapping(value = "{path*}", methods = "get")
  def download(): View = {
    val path = getPath()
    val res = ActionContext.current.response
    if (SnapshotHelper.isLatestRequest(path)) {
      SnapshotHelper.resolve(path) match {
        case None => Status.NotFound
        case Some(f) =>
          val request = ActionContext.current.request
          res.sendRedirect(request.getContextPath + "/repo/snapshot/" +
            Strings.substringBeforeLast(path, "/") + "/" + f.getName)
          Status.Ok
      }
    } else {
      val root = SystemInfo.user.home + "/.m2/snapshots/"
      val target = new File(root + path)
      if (target.exists()) {
        val is = new FileInputStream(target)
        IOs.copy(is, res.getOutputStream)
        Status.Ok
      } else {
        Status.NotFound
      }
    }
  }

  @mapping(value = "upload/{fileName}", methods = "post")
  def upload(@param("fileName") fileName: String): View = {
    val request = ActionContext.current.request
    val authorizationHeader = request.getHeader("Authorization")
    if (Strings.isBlank(authorizationHeader) || !authorizationHeader.startsWith("Basic ")) {
      Status.Forbidden
    } else {
      val usertoken = new String(Base64.decode(authorizationHeader.substring("Basic ".length).trim()))
      if (usertoken == EmsApp.properties.get("snapshot.usertoken").orNull) {
        val tmpFile = java.nio.file.Files.createTempFile("artifact", "war").toFile
        IOs.copy(request.getInputStream, new FileOutputStream(tmpFile))
        val rs = SnapshotHelper.upload(tmpFile, getPath("fileName"))
        tmpFile.delete()
        if (rs._1) then Status.Ok else  Status(500)
      } else {
        Status.Forbidden
      }
    }
  }

  private def getPath(name: String = "path"): String = {
    val uri = ActionContext.current.request.getRequestURI
    var path = get(name, "")
    val postfix = Strings.substringAfterLast(uri, ".")
    if (Strings.isNotBlank(postfix)) {
      path += ("." + postfix)
    }
    path
  }

}
