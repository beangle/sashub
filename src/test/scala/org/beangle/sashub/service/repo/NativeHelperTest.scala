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

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.Comparator

class NativeHelperTest extends AnyFunSpec with Matchers {

  private val version = "4.20.14-SNAPSHOT"
  private val distName = "beangle-ems-portal-4.20.14-SNAPSHOT-linux-amd64.tar.gz"
  private val distPath = s"org/beangle/beangle-ems-portal/$version/$distName"

  describe("NativeHelper") {
    it("uploads a distribution and stores its sha1 checksum besides it") {
      withRepo { repo =>
        val dist = tempFile("dist.tar.gz", "native distribution")
        repo.upload(dist, distPath)._1 shouldBe true

        val artifact = repo.fileOf(distPath)
        artifact.exists() shouldBe true
        val sha1 = Sha1.digest(artifact)

        val checksum = tempFile("dist.sha1", sha1)
        repo.upload(checksum, distPath + ".sha1")._1 shouldBe true
        new String(Files.readAllBytes(new File(artifact.getParentFile, distName + ".sha1").toPath),
          StandardCharsets.US_ASCII) shouldBe sha1

        dist.delete()
        checksum.delete()
      }
    }

    it("stores the sha1 uploaded before its artifact") {
      withRepo { repo =>
        val dist = tempFile("dist.tar.gz", "native distribution")
        val checksum = tempFile("dist.sha1", Sha1.digest(dist))
        repo.upload(checksum, distPath + ".sha1")._1 shouldBe true

        val artifact = repo.fileOf(distPath)
        artifact.exists() shouldBe false
        val pending = new File(repo.root, s".pending/$distPath.sha1")
        pending.exists() shouldBe true

        repo.upload(dist, distPath)._1 shouldBe true
        artifact.exists() shouldBe true
        pending.exists() shouldBe false
        new String(Files.readAllBytes(new File(artifact.getParentFile, distName + ".sha1").toPath),
          StandardCharsets.US_ASCII) shouldBe Sha1.digest(dist)

        dist.delete()
        checksum.delete()
      }
    }

    it("rejects an invalid sha1 checksum and an unsafe path") {
      val repo = NativeHelper.default
      val checksum = tempFile("dist.sha1", "not-a-sha1")
      repo.upload(checksum, distPath + ".sha1")._1 shouldBe false
      repo.upload(checksum, "../outside.tar.gz")._1 shouldBe false
      checksum.delete()

      NativeHelper.isSafe(distPath) shouldBe true
      NativeHelper.isSafe("/etc/passwd") shouldBe false
      NativeHelper.isSafe("org/../../etc/passwd") shouldBe false
      repo.resolve("../outside.tar.gz") shouldBe None
    }

    it("resolves a -SNAPSHOT alias to the newest timestamped distribution and delta") {
      withRepo { repo =>
        val dir = new File(repo.root, s"org/beangle/beangle-ems-portal/$version")
        val delta = "beangle-ems-portal-4.20.13_4.20.14-SNAPSHOT-linux-amd64.tar.gz.diff"
        val oldDelta = "beangle-ems-portal-4.20.13_4.20.14-SNAPSHOT-20260912.101500-1-linux-amd64.tar.gz.diff"
        val newDelta = "beangle-ems-portal-4.20.13_4.20.14-SNAPSHOT-20260913.101500-1-linux-amd64.tar.gz.diff"
        val newDist = "beangle-ems-portal-4.20.14-SNAPSHOT-20260913.101500-1-linux-amd64.tar.gz"
        Seq(oldDelta, newDelta, newDist).foreach(name => tempFileIn(dir, name))

        repo.resolve(distPath).map(_.getName) shouldBe Some(newDist)
        repo.resolve(distPath + ".sha1") shouldBe None
        repo.resolve(s"org/beangle/beangle-ems-portal/$version/$delta").map(_.getName) shouldBe Some(newDelta)
        repo.resolve(distPath).map(repo.relativePath(_)) shouldBe Some(distPath.replace(distName, newDist))
      }
    }
  }

  private def withRepo(f: NativeHelper => Unit): Unit = {
    val dir = Files.createTempDirectory("natives-repo").toFile
    try {
      f(new NativeHelper(dir))
    } finally {
      deleteRecursively(dir)
    }
  }

  private def tempFile(name: String, content: String): File = {
    val file = Files.createTempFile(name, "").toFile
    Files.write(file.toPath, content.getBytes(StandardCharsets.US_ASCII))
    file
  }

  private def tempFileIn(dir: File, name: String): File = {
    dir.mkdirs()
    val file = new File(dir, name)
    Files.write(file.toPath, name.getBytes(StandardCharsets.US_ASCII))
    file
  }

  private def deleteRecursively(dir: File): Unit = {
    if (dir.exists()) {
      val stream = Files.walk(dir.toPath)
      try stream.sorted(Comparator.reverseOrder()).forEach(p => Files.deleteIfExists(p))
      finally stream.close()
    }
  }
}
