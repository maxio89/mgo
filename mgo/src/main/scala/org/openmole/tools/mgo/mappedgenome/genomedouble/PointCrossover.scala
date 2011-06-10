/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.tools.mgo.mappedgenome.genomedouble
import org.openmole.tools.mgo.evolution.GenomeOperation
import org.openmole.tools.mgo.mappedgenome.genomedouble._
import java.util.Random
import org.openmole.tools.mgo.tools.Random._
import IntervalSet._

class PointCrossover (interval:IntervalSet) extends GenomeOperation[GenomeDouble] {
  
  override def operate(genomes: IndexedSeq[GenomeDouble])(implicit rng: Random): GenomeDouble = {
    
    val size:Int = genomes.size
    if (size == 1){
      return genomes(0)
    }
    
    var genomeA = genomes.random
    var genomeB = genomes.random
    var pointCrossover = rng.nextInt(genomes.head.size)
    
    var newGenome = IntervalSet.intervallDecorator.generateEmpty(interval)
    
    var i = 0
    
    genomeA.foreach{ case(key,value) => 
        {  
          if(i < pointCrossover) 
            newGenome.update(key,genomeA.apply(key))
          else
            newGenome.update(key,genomeB.apply(key)) 
          
          i+=1 
        }
    }
    
    return newGenome
  }
}
