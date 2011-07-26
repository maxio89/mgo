/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.tools.mgo.ga.operators

import org.openmole.tools.mgo._
import ga._
import java.util.Random

class RandomCrossOver [G <: GAGenome, F <: GAGenomeFactory [G]] (val factory : F)
  extends CrossOver [G, F] {
  def operate (genomes : IndexedSeq [G]) (implicit aprng : Random) : G = {
    val g1 = genomes (aprng.nextInt(genomes.size))
    val g2 = genomes (aprng.nextInt(genomes.size))
    
    factory.buildGenome (
      IndexedSeq.tabulate (g1.values.size) (i => 
        if (aprng.nextBoolean) g1.values (i) else g2.values (i))
    )
  }
}

