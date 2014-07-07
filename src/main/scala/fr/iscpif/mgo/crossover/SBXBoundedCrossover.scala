/*
 * Copyright (C) 2012 reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
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

package fr.iscpif.mgo.crossover

import fr.iscpif.mgo._
import fr.iscpif.mgo.tools.Math._
import math._
import util.Random

/**
 * SBX RGA operator with Bounded Variable modification, see APPENDIX A p30 into :
 *
 * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.33.7291&rep=rep1&type=pdf
 *
 * @INPROCEEDINGS{Deb98anefficient,
 *   author = {Kalyanmoy Deb},
 *   title = {An Efficient Constraint Handling Method for Genetic Algorithms},
 *   booktitle = {Computer Methods in Applied Mechanics and Engineering},
 *   year = {1998},
 *   pages = {311--338}
 * }
 *
 * Notes : Deb implementation differs from NSGA2 he proposed on this site :
 * http://www.iitk.ac.in/kangal/codes.shtml
 *
 */
object SBXBoundedCrossover {

  def crossOver(g1: Seq[Double], g2: Seq[Double], crossoverRate: Double, distributionIndex: Double)(implicit rng: Random) = {

    /** crossover probability */
    val offspring = {
      (g1 zip g2).map {
        case (g1e, g2e) =>
          if (rng.nextDouble <= crossoverRate) {
            if (abs(g1e - g2e) > epsilon) {
              val y1 = min(g1e, g2e)
              val y2 = max(g2e, g1e)

              val yL = 0.0 //g1e.getLowerBound
              val yU = 1.0 //g1e.getUpperBound

              def inBound(v: Double) = if (v < yL) yL else if (v > yU) yU else v

              val rand = rng.nextDouble // ui

              val beta1 = 1.0 + (2.0 * (y1 - yL) / (y2 - y1))
              val alpha1 = 2.0 - pow(beta1, -(distributionIndex + 1.0))

              val betaq1 = {
                if (rand <= (1.0 / alpha1)) pow((rand * alpha1), (1.0 / (distributionIndex + 1.0)))
                else pow((1.0 / (2.0 - rand * alpha1)), (1.0 / (distributionIndex + 1.0)))
              }

              val c1 = inBound(0.5 * ((y1 + y2) - betaq1 * (y2 - y1)))

              // -----------------------------------------------

              val beta2 = 1.0 + (2.0 * (yU - y2) / (y2 - y1))
              val alpha2 = 2.0 - pow(beta2, -(distributionIndex + 1.0))

              val betaq2 = {
                if (rand <= (1.0 / alpha2)) pow((rand * alpha2), (1.0 / (distributionIndex + 1.0)))
                else pow((1.0 / (2.0 - rand * alpha2)), (1.0 / (distributionIndex + 1.0)))
              }

              val c2 = inBound(0.5 * ((y1 + y2) + betaq2 * (y2 - y1)))

              if (rng.nextBoolean) (c2, c1) else (c1, c2)
            } else (g1e, g2e)
          } else (g2e, g1e)
      }
    }
    (offspring.unzip._1, offspring.unzip._2)
  }

}

trait SBXBoundedCrossover extends Crossover with GA with CrossoverRate {

  /** distribution index parameter of the algorithm */
  def distributionIndex: Double = 2

  override def crossover(g1: G, g2: G, population: Seq[Individual[G, P, F]], archive: A)(implicit rng: Random) = {
    val (o1, o2) = sbxCrossover(g1, g2)
    Seq(o1, o2)
  }

  def sbxCrossover(g1: G, g2: G)(implicit rng: Random) = {
    val (o1, o2) = SBXBoundedCrossover.crossOver(genome.get(g1), genome.get(g2), crossoverRate, distributionIndex)
    (genome.set(g1, o1), genome.set(g2, o2))
  }

}