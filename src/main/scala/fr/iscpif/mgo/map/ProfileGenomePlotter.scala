/*
 * Copyright (C) 07/01/13 Romain Reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.iscpif.mgo.map

import fr.iscpif.mgo._

trait ProfileGenomePlotter extends ProfilePlotter with GA {

  def x: Int
  def nX: Int

  override def plot(i: Individual[G, P, F]) = {
    val niche = (values.get(i.genome)(x) * nX).toInt
    if (niche == nX) niche - 1 else niche
  }
}
