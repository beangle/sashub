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
import org.beangle.commons.lang.Strings
import org.beangle.ems.app.EmsApp
import org.beangle.sashub.service.repo.NativeHelper
import org.beangle.webmvc.annotation.mapping
import org.beangle.webmvc.context.ActionContext
import org.beangle.webmvc.support.ActionSupport
import org.beangle.webmvc.view.{Status, View}

import java.io.{FileInputStream, FileOutputStream}

/** 管理原生发行包(tar.gz)与增量补丁(.diff)
  *
  * 上传地址为 `/repo/native/upload/{仓库内相对路径}`,例如
  * `/repo/native/upload/org/beangle/beangle-ems-portal/4.20.14-SNAPSHOT/beangle-ems-portal-4.20.14-SNAPSHOT-linux-amd64.tar.gz.sha1`
  */
class NativeAction extends ActionSupport {

  @mapping(value = "{path*}", methods = "head")
  def access(): View = {
    val path = getPath()
    val repo = NativeHelper.default
    repo.resolve(path) match {
      case None => Status.NotFound
      case Some(file) =>
        val res = ActionContext.current.response
        if (path != repo.relativePath(file)) then res.addHeader("latest", file.getName)
        res.addDateHeader("last-modified", file.lastModified())
        Status.Ok
    }
  }

  @mapping(value = "{path*}", methods = "get")
  def download(): View = {
    val path = getPath()
    val repo = NativeHelper.default
    repo.resolve(path) match {
      case None => Status.NotFound
      case Some(file) =>
        val relativePath = repo.relativePath(file)
        if (path != relativePath) {
          // SNAPSHOT别名:重定向到带时间戳的具体文件,便于客户端缓存
          val request = ActionContext.current.request
          ActionContext.current.response.sendRedirect(request.getContextPath + "/repo/native/" + relativePath)
        } else {
          val is = new FileInputStream(file)
          try IOs.copy(is, ActionContext.current.response.getOutputStream)
          finally is.close()
        }
        Status.Ok
    }
  }

  @mapping(value = "upload/{path*}", methods = "post")
  def upload(): View = {
    val request = ActionContext.current.request
    val authorizationHeader = request.getHeader("Authorization")
    if (Strings.isBlank(authorizationHeader) || !authorizationHeader.startsWith("Basic ")) {
      Status.Forbidden
    } else {
      val usertoken = new String(Base64.decode(authorizationHeader.substring("Basic ".length).trim()))
      if (usertoken == EmsApp.properties.get("snapshot.usertoken").orNull) {
        val tmpFile = java.nio.file.Files.createTempFile("native", "upload").toFile
        try {
          IOs.copy(request.getInputStream, new FileOutputStream(tmpFile))
          val rs = NativeHelper.default.upload(tmpFile, getPath("path"))
          if (rs._1) then Status.Ok else Status(500)
        } finally {
          tmpFile.delete()
        }
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
