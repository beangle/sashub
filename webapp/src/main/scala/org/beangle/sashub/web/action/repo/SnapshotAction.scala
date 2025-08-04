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

import org.beangle.commons.collection.Collections
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{Objects, Strings, SystemInfo}
import org.beangle.webmvc.annotation.mapping
import org.beangle.webmvc.context.ActionContext
import org.beangle.webmvc.support.ActionSupport
import org.beangle.webmvc.view.{Status, View}

import java.io.{File, FileInputStream}

class SnapshotAction extends ActionSupport {

  @mapping(method = "head", value = "{path*}")
  def access(): View = {
    val path = getPath()
    val res = ActionContext.current.response
    val localPath = SystemInfo.user.home + "/.m2/snapshots/" + path
    if (path.endsWith("-SNAPSHOT.jar")) {
      findLatest(path) match {
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

  @mapping(method = "get", value = "{path*}")
  def download(): View = {
    val path = getPath()
    val localPath = SystemInfo.user.home + "/.m2/snapshots/" + path
    if (path.endsWith("-SNAPSHOT.jar")) {
      findLatest(path) match {
        case None => Status.NotFound
        case Some(f) =>
          val request = ActionContext.current.request
          ActionContext.current.response.sendRedirect(request.getContextPath + "/repo/snapshot/" +
            Strings.substringBeforeLast(path, "/") + "/" + f.getName)
          Status.Ok
      }
    } else {
      val root = SystemInfo.user.home + "/.m2/snapshots/"
      val target = new File(root + path)
      if (target.exists()) {
        val is = new FileInputStream(target)
        IOs.copy(is, ActionContext.current.response.getOutputStream)
        Status.Ok
      } else {
        Status.NotFound
      }
    }
  }

  private def getPath(): String = {
    val uri = ActionContext.current.request.getRequestURI
    var path = get("path", "")
    val postfix = Strings.substringAfterLast(uri, ".")
    if (Strings.isNotBlank(postfix)) {
      path += ("." + postfix)
    }
    path
  }

  private def findLatest(path: String): Option[File] = {
    val root = SystemInfo.user.home + "/.m2/snapshots/"
    val localPath = root + path
    val parent = new File(localPath).getParentFile
    if (parent.exists()) {
      val prefix = Strings.substringBefore(Strings.substringAfterLast(path, "/"), "-SNAPSHOT.jar") + "-"
      val snapshots = parent.list().toList
      val versions = Collections.newBuffer[SnaphotTimestamp]
      for (s <- snapshots) {
        if (s.startsWith(prefix) && s.endsWith(".jar")) {
          val ts = Strings.substringBetween(s, prefix, ".jar")
          versions += new SnaphotTimestamp(ts)
        }
      }
      val rs = versions.sorted
      if (rs.isEmpty) None
      else {
        val filePath = root + Strings.replace(path, "SNAPSHOT.jar", rs.last.toString + ".jar")
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
