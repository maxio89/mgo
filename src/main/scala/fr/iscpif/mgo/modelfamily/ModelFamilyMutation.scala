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

import fr.iscpif.mgo.mutation.CoEvolvingSigmaValuesMutation
import scala.util.Random
import fr.iscpif.mgo._
import tools.Math._

trait ModelFamilyMutation <: CoEvolvingSigmaValuesMutation with ModelFamilyGenome with Aggregation {

  def changeNiche = 0.1

  override def mutate(genome: G, population: Seq[Individual[G, P, F]], archive: A)(implicit rng: Random): G = {
    val (newValues, newSigma) = CoEvolvingSigmaValuesMutation.mutate(values.get(genome), sigma.get(genome), minimumSigma, mutationRate)
    val res = sigma.set(values.set(genome, newValues), newSigma)
    if (rng.nextDouble < changeNiche) modelId.set(res, rng.nextInt(models)) else res
  }

}