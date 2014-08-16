/*
 * Copyright (C) 2014 Romain Reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.iscpif.mgo.genome

import monocle.Macro._
import monocle.SimpleLens
import monocle.syntax._

import scala.util.Random

object GAGenomeWithRandomValue {
  case class Genome(values: Seq[Double], randomValues: Seq[Double])
}

trait GAGenomeWithRandomValue extends GA with RandomValue {

  type G = GAGenomeWithRandomValue.Genome

  def rawValues = mkLens[G, Seq[Double]]("values")

  def genome = SimpleLens[G, Seq[Double]](
    v => values.get(v) ++ randomValues.get(v),
    (c, v) =>
      (c |-> values set v.slice(0, v.size / 2)) |-> randomValues set v.slice(v.size / 2, v.size)
  )

  def randomValues = mkLens[G, Seq[Double]]("randomValues")

  def randomGenome(implicit rng: Random) = {
    def rnd = Stream.continually(rng.nextDouble).take(genomeSize).toIndexedSeq
    GAGenomeWithRandomValue.Genome(rnd, rnd)
  }

}