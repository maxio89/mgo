/*
 * Copyright (C) 2012 Romain Reuillon
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

package fr.iscpif.mgo.test

import fr.iscpif.mgo._
import java.util.Random

object TestFunction extends App { 
  
  val zdt = new ZDT4 {
    def n = 10
  }
 
  implicit val rng = new Random
  
  val nsga2 =
      new NSGAIISigma
                     with MGBinaryTournamentSelection
                     with CounterTermination
                     with NonDominatedSortingElitism
                     with CoEvolvingSigmaValuesMutation
                     with SBXBoundedCrossover 
                     with Crowding
                     with ParetoRanking
                     with StrictDominance
                     with RankDiversityModifier {
      def distributionIndex = 2
      //def windowSize = 100
      //def crowdingDeviationEpsilon = 0.01
      def maxStep = 150
      def mu = 15
      def lambda = 200
      def genomeSize = 10
    }
  
  val res = nsga2.run(zdt).dropWhile{ s => println(s.terminationState) ; !s.terminated }.next.population
  res sortBy (_.metaFitness.rank) foreach { e => println(zdt.scale(e)._2.values.mkString(",")) }
}
