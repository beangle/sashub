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

package org.beangle.sashub.model.config

import org.beangle.commons.lang.Strings

object Version {

  def isNewer(newVersion: String, oldVersion: String): Boolean = {
    if (newVersion == oldVersion) {
      false
    } else {
      val v = Strings.replace(oldVersion, "-SNAPSHOT", "")
      val vParts = Strings.split(v, ".")
      val nParts = Strings.split(newVersion, ".") //new version parts
      val min = Math.min(vParts.length, nParts.length)
      var i = 0
      var greate = 0
      while (i < min) {
        val lp = nParts(i)
        val np = vParts(i)
        if (lp != np) {
          val l = Math.max(lp.length, np.length)
          val cmp = Strings.leftPad(lp, l, '0').compare(Strings.leftPad(np, l, '0'))
          if (cmp > 0) {
            greate = 1
            i = min
          } else if (cmp < 0) {
            if (greate == 0) {
              greate = -1
              i = min
            }
          }
        }
        i += 1
      }
      greate >= 0
    }
  }
}
