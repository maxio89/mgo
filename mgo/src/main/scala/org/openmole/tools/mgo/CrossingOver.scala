/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.tools.mgo

trait CrossingOver [G <: AbstractGenome, F <: GenomeFactory [G]] 
  extends Operator [G, F]