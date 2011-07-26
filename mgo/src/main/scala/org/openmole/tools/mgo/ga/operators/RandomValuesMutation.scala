/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.tools.mgo.ga

import org.openmole.tools.mgo._
import java.util.Random
import org.openmole.tools.mgo.tools.Random._
import org.openmole.tools.mgo.genomefactory._

class RandomValuesMutation [
  G <: GAGenome, 
  F <: GAGenomeFactory [G] with FromValuesFactory [G]] (
  rate: Random => Double = rng => rng.nextFloat, val factory:F) 
  extends Mutation [G, F]  {
  
  def this(rate: Double, factory:F) = this( _ => rate, factory)
  
  override def operate(genomes: IndexedSeq[G])(implicit aprng: Random): G = {
   
    val mutationRate = rate(aprng)
    val genome = genomes.random
    
    val newValues = genome.values map { v => 
      if (aprng.nextDouble < mutationRate) 
        aprng.nextDouble
      else v }
    
    return factory.buildFromValues (genome,newValues)
  }
  
}