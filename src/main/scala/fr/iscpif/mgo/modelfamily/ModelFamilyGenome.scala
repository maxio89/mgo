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

package fr.iscpif.mgo.modelfamily

import fr.iscpif.mgo.genome._
import monocle.Macro._
import monocle._
import scala.util.Random

object ModelFamilyGenome {
  case class Genome(modelId: Int, values: Seq[Double], sigma: Seq[Double])
}

trait ModelFamilyGenome <: ModelId with Sigma with GA with RandomGenome {

  type G = ModelFamilyGenome.Genome

  def models: Int
  def genomeSize: Int

  def modelId = mkLens[G, Int]("modelId")

  def values = mkLens[G, Seq[Double]]("values")

  def genome = SimpleLens[G, Seq[Double]](
    v => v.values ++ v.sigma,
    (c, v) =>
      ModelFamilyGenome.Genome(
        c.modelId,
        v.slice(0, v.size / 2),
        v.slice(v.size / 2, v.size)
      )
  )

  def sigma = mkLens[G, Seq[Double]]("sigma")

  def randomGenome(implicit rng: Random) = {
    def rnd = Stream.continually(rng.nextDouble).take(genomeSize).toIndexedSeq
    ModelFamilyGenome.Genome(rng.nextInt(models), rnd, rnd)
  }
}