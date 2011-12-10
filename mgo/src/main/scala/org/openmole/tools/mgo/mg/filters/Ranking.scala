/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.tools.mgo.mg.filters

import org.openmole.tools.mgo.AbstractGenome
import org.openmole.tools.mgo.mg.IndividualMG
import org.openmole.tools.mgo.model.MultiGoalLike

abstract class Ranking[G <: AbstractGenome , MG <: MultiGoalLike] {
  type RankType <: IndividualMG[G,MG]
  
  def operate(individuals :IndexedSeq [RankType]):IndexedSeq[RankType]
}