/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.tools.mgo.ga

trait SigmaParameters extends GAGenome { self : GAGenome => 
  def sigma: Array[Double]
}