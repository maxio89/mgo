/*
 * Copyright (C) 2010 reuillon
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

package org.openmole.tools.mgo.domination

import org.openmole.tools.mgo.model.MultiGoal
import DominateType._

object DominateMinimization {
  
  def isDominated(p1: MultiGoal, p2: MultiGoal): Boolean = {
    for((g1, g2) <- p1.goals zip p2.goals) {
      if(g1.toDouble <= g2.toDouble) return false
    }   
    true
  }

  def dominated(p1: MultiGoal, p2: MultiGoal): DominateType = {
    var a1 = 0
    var a2 = 0
    var tot = 0
      
    for((g1, g2) <- p1.goals zip p2.goals) {
      val compare = g1.toDouble - g2.toDouble //g1Order.compare(g1.value, g2.value)

      if(compare < 0) a1 += 1
      else if(compare > 0) a2 +=1

      tot +=1

      if(a1 != tot && a2 != tot) return DominateType.NONE
    }
        
    if(a1==tot) return DominateType.RIGHT
    else if(a2==tot) return DominateType.LEFT
    else return DominateType.NONE
  }
}
