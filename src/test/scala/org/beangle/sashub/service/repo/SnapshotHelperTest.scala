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

import org.beangle.commons.file.digest.Sha1
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.io.{File, FileOutputStream}
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.Comparator
import java.util.jar.{Attributes, JarEntry, JarOutputStream, Manifest}

class SnapshotHelperTest extends AnyFunSpec with Matchers {

  describe("SnapshotHelper") {
    it("uploads artifact and stores its sha1 checksum besides it") {
      val home = Files.createTempDirectory("snapshot-home").toFile
      val originalHome = System.getProperty("user.home")
      var jar: File = null
      var checksum: File = null
      try {
        System.setProperty("user.home", home.getAbsolutePath)
        jar = tempJar("org.beangle", "app", "1.0-SNAPSHOT")
        val fileName = "app-1.0-20260908.120000-1.jar"
        SnapshotHelper.upload(jar, fileName)._1 shouldBe true

        val artifact = new File(home, s".m2/snapshots/org/beangle/app/1.0-SNAPSHOT/$fileName")
        artifact.exists() shouldBe true
        val sha1 = Sha1.digest(artifact)

        checksum = tempFile("checksum.sha1", sha1 + "\n")
        SnapshotHelper.upload(checksum, fileName + ".sha1")._1 shouldBe true

        val stored = new File(artifact.getParentFile, fileName + ".sha1")
        stored.exists() shouldBe true
        new String(Files.readAllBytes(stored.toPath), StandardCharsets.US_ASCII) shouldBe (sha1 + "\n")
      } finally {
        if (null != jar) jar.delete()
        if (null != checksum) checksum.delete()
        System.setProperty("user.home", originalHome)
        deleteRecursively(home)
      }
    }

    it("stores sha1 checksum uploaded before its artifact") {
      val home = Files.createTempDirectory("snapshot-home").toFile
      val originalHome = System.getProperty("user.home")
      var jar: File = null
      var checksum: File = null
      try {
        System.setProperty("user.home", home.getAbsolutePath)
        jar = tempJar("org.beangle", "app", "1.0-SNAPSHOT")
        val fileName = "app-1.0-20260908.120001-1.jar"
        checksum = tempFile("checksum.sha1", Sha1.digest(jar) + "\n")
        SnapshotHelper.upload(checksum, fileName + ".sha1")._1 shouldBe true

        val artifact = new File(home, s".m2/snapshots/org/beangle/app/1.0-SNAPSHOT/$fileName")
        artifact.exists() shouldBe false
        val pending = new File(home, s".m2/snapshots/.pending/$fileName.sha1")
        pending.exists() shouldBe true
        SnapshotHelper.upload(jar, fileName)._1 shouldBe true

        artifact.exists() shouldBe true
        pending.exists() shouldBe false
        val stored = new File(artifact.getParentFile, fileName + ".sha1")
        stored.exists() shouldBe true
        new String(Files.readAllBytes(stored.toPath), StandardCharsets.US_ASCII) shouldBe
          (Sha1.digest(jar) + "\n")
      } finally {
        if (null != jar) jar.delete()
        if (null != checksum) checksum.delete()
        System.setProperty("user.home", originalHome)
        deleteRecursively(home)
      }
    }

    it("rejects invalid sha1 checksum file") {
      var checksum: File = null
      try {
        checksum = tempFile("checksum.sha1", "not-a-sha1")
        SnapshotHelper.upload(checksum, "app-1.0-20260908.120000-1.jar.sha1")._1 shouldBe false
      } finally {
        if (null != checksum) checksum.delete()
      }
    }

    it("recognizes and unwraps sha1 checksum paths") {
      SnapshotHelper.isChecksum("app-1.0-SNAPSHOT.war.sha1") shouldBe true
      SnapshotHelper.isChecksum("app-1.0-SNAPSHOT.war") shouldBe false
      SnapshotHelper.unwrapChecksum("g/a/1.0-SNAPSHOT/app-1.0-SNAPSHOT.war.sha1") shouldBe
        ("g/a/1.0-SNAPSHOT/app-1.0-SNAPSHOT.war", ".sha1")
      SnapshotHelper.unwrapChecksum("g/a/1.0-SNAPSHOT/app-1.0-SNAPSHOT.jar") shouldBe
        ("g/a/1.0-SNAPSHOT/app-1.0-SNAPSHOT.jar", "")
      SnapshotHelper.isLatestRequest("g/a/1.0-SNAPSHOT/app-1.0-SNAPSHOT.war.sha1") shouldBe true
    }
  }

  private def tempFile(name: String, content: String): File = {
    val file = File.createTempFile(name, "")
    Files.write(file.toPath, content.getBytes(StandardCharsets.US_ASCII))
    file
  }

  private def tempJar(vendorId: String, title: String, version: String): File = {
    val jar = File.createTempFile("artifact", ".jar")
    val out = new JarOutputStream(new FileOutputStream(jar))
    val manifest = new Manifest()
    manifest.getMainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0")
    manifest.getMainAttributes.putValue("Implementation-Vendor-Id", vendorId)
    manifest.getMainAttributes.putValue("Implementation-Title", title)
    manifest.getMainAttributes.putValue("Implementation-Version", version)
    out.putNextEntry(new JarEntry("META-INF/MANIFEST.MF"))
    manifest.write(out)
    out.closeEntry()
    out.close()
    jar
  }

  private def deleteRecursively(dir: File): Unit = {
    if (dir.exists()) {
      val stream = Files.walk(dir.toPath)
      try stream.sorted(Comparator.reverseOrder()).forEach(p => Files.deleteIfExists(p))
      finally stream.close()
    }
  }
}
