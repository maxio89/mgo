/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.tools.mgo.ga

import org.openmole.tools.mgo.AbstractGenome

trait GAGenome extends AbstractGenome {
  val wrappedValues = values
  val values : IndexedSeq [Double] 
}
