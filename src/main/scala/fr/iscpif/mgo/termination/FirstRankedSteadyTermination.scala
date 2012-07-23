/*
 * Copyright (C) 2011 Sebastien Rey
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
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.iscpif.mgo.termination

import fr.iscpif.mgo._
import fr.iscpif.mgo.ranking.Rank
import fr.iscpif.mgo.ranking.Rank._
import fr.iscpif.mgo.selection._
import fr.iscpif.mgo.tools.Math

trait FirstRankedSteadyTermination extends Termination {
  self: Evolution { type MF <: Rank } =>
  
  type STATE = (Int, Population[G, MF])
  
  def initialState(p: Population[G, MF]) = (0, p)
  
  def steadySince: Int

  def terminated(population: Population[G, MF], state: STATE): (Boolean, STATE) = {
    val (step, oldPop) = state
    val newStep = if ( 
      Math.allTheSame(firstRanked(population).map {_.fitness.values},
                      firstRanked(oldPop).map {_.fitness.values})
    ) step + 1 else  0
    (newStep >= steadySince, newStep -> population)
  } 
  
  
}
