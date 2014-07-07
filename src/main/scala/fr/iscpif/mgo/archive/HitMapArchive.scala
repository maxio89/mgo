/*
 * Copyright (C) 30/05/2014 Guillaume Chérel
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

package fr.iscpif.mgo.archive

import fr.iscpif.mgo._
import scala.collection.Map

trait HitMapArchive <: Archive {

  type A = Map[Seq[Int], Int]

  type Cell = Seq[Int]

  def hitCell(i: Individual[G, P, F]): Cell

  def hits(a: A, c: Seq[Int]): Int = a(c)

  def initialArchive = Map[Seq[Int], Int]()

  def toArchive(individuals: Seq[Individual[G, P, F]]): A = {
    individuals.groupBy(hitCell).map { case (k, v) => (k -> v.size) }
  }

  def combine(a1: A, a2: A): A = a2.foldLeft(a1)((a: A, kv: (Seq[Int], Int)) => {
    val a2key: Seq[Int] = kv._1
    val a2value: Int = kv._2
    if (a contains a2key) a + ((a2key, a(a2key) + a2value))
    else a + ((a2key, a2value))
  })

  def diff(original: A, modified: A): A = {
    modified.foldLeft(initialArchive)((m, kv) => {
      val key = kv._1
      val value = kv._2
      if (original contains key) m + ((key, value - original(key)))
      else m + ((key, value))
    })
  }

}