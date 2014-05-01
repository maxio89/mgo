/*
 * Copyright (C) 08/01/13 Romain Reuillon
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

package fr.iscpif.mgo.elitism

import fr.iscpif.mgo._

trait ProfileElitism <: Elitism with ProfilePlotter with Aggregation with MergedGenerations {
  override def elitism(individuals: Seq[Individual[G, P, F]], archive: A): Seq[Individual[G, P, F]] =
    individuals.groupBy(plot).toSeq.map {
      case (_, is) => is.minBy(i => aggregate(i.fitness))
    }
}
