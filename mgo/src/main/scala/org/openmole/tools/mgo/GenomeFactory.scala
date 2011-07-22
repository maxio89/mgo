/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.tools.mgo

abstract class GenomeFactory[G <: AbstractGenome] {
  def buildGenome(values: Array[Double]): G
}