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
import scala.util.Random

trait HitMapArchive <: Archive with Niche {

  type A = Map[NICHE, Int]

  def hits(a: A, c: NICHE): Int = a.getOrElse(c, 0)

  def initialArchive(implicit rng: Random) = Map[NICHE, Int]()

  override def archive(archive: A, oldIndividuals: Population[G, P, F], offspring: Population[G, P, F])(implicit rng: Random): A =
    combine(archive, toArchive(offspring))

  def toArchive(population: Population[G, P, F]): A =
    population.toIndividuals.groupBy(niche).map { case (k, v) => (k -> v.size) }

  def combine(a1: A, a2: A): A = a2.foldLeft(a1)((a: A, kv: (NICHE, Int)) => {
    val a2key: NICHE = kv._1
    val a2value: Int = kv._2
    if (a contains a2key) a + ((a2key, a(a2key) + a2value))
    else a + ((a2key, a2value))
  })

  def diff(original: A, modified: A)(implicit rng: Random): A = {
    modified.foldLeft(initialArchive)((m, kv) => {
      val key = kv._1
      val value = kv._2
      if (original contains key) m + ((key, value - original(key)))
      else m + ((key, value))
    })
  }

}
