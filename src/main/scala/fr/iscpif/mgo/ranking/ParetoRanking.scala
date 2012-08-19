/*
 * Copyright (C) 2012 Romain Reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
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

package fr.iscpif.mgo.ranking

import fr.iscpif.mgo._
import tools.Lazy

trait ParetoRanking extends Dominance with G {
  def rank(evaluated: IndexedSeq[Individual[G]]) = { 
    evaluated.zipWithIndex.map { 
      case (indiv, index) =>
        Lazy(evaluated.zipWithIndex.filter {
            case (_, index2) => index != index2
          }.count { 
            case(indiv2, _) => isDominated(indiv.fitness.values, indiv2.fitness.values)
          })
    }
  }
}



