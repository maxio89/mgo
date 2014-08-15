/*
 * Copyright (C) 2011 Sebastien Rey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.iscpif.mgo

import util.Random
import org.apache.commons.math3.random.{ RandomAdaptor, Well44497b }

/**
 * Trait evolution provide the feature to define an evolutionary algorithm
 */
trait Evolution extends Termination
    with Modifier
    with Lambda
    with G
    with F
    with P
    with MF
    with Archive
    with Breeding
    with Elitism { self =>

  /**
   * Represent a state of the evolution algorithm
   */
  case class EvolutionState(
      /** The current population of solution */
      population: Population[G, P, F, MF],
      /** The current achive */
      archive: A,
      /** The number of the generation */
      generation: Int,
      /** The state maintained for the termination criterium */
      terminationState: STATE,
      /** true if the termination criterium is met false otherwhise */
      terminated: Boolean) {
    /** The current population of solution */
    def individuals: Seq[Individual[G, P, F]] = population.toIndividuals
  }

  def buildRNG(seed: Long) = new Random(new RandomAdaptor(new Well44497b(seed)))

  /**
   * Run the evolutionary algorithm
   *
   * @param individuals the initial individuals
   * @param expression the genome expression
   * @param evaluation the fitness evaluator
   * @return an iterator over the states of the evolution
   */
  def evolve(individuals: Seq[Individual[G, P, F]], a: A, expression: (G, Random) => P, evaluation: (P, Random) => F)(implicit aprng: Random): Iterator[EvolutionState] =
    Iterator.iterate(EvolutionState(toPopulation(individuals, a), a, 0, initialState, false)) {
      s =>
        val (newIndividuals, newArchive) = step(s.individuals, s.archive, expression, evaluation)
        val newPop = toPopulation(newIndividuals, newArchive)
        val (stop, newState) = terminated(newPop, s.terminationState)
        EvolutionState(newPop, newArchive, s.generation + 1, newState, stop)
    }

  /**
   * Run the evolutionary algorithm
   * @param expression the genome expression
   * @param evaluation the fitness evaluator
   * @return an iterator over the states of the evolution
   */
  def evolve(expression: (G, Random) => P, evaluation: (P, Random) => F)(implicit prng: Random): Iterator[EvolutionState] = {
    val archive = initialArchive
    evolve(Seq.empty, archive, expression, evaluation)
  }

  /**
   * Evolve one step
   *
   * @param individuals the current population
   * @param archive the current archive
   * @param expression expression of the genome
   * @param evaluation the fitness evaluator
   * @return a new population of evaluated solutions
   *
   */
  def step(individuals: Seq[Individual[G, P, F]], archive: A, expression: (G, Random) => P, evaluation: (P, Random) => F)(implicit rng: Random): (Seq[Individual[G, P, F]], A) = {
    val offspringGenomes = breed(individuals, archive, lambda)
    val rngs = (0 until offspringGenomes.size).map(_ => buildRNG(rng.nextLong))

    val offspring = (offspringGenomes zip rngs).par.map { case (g, rng) => Individual[G, P, F](g, expression, evaluation)(rng) }.seq
    val newArchive = self.archive(archive, offspring)

    //Elitism strategy
    (elitism(individuals.toList, offspring.toList, newArchive), newArchive)
  }

}
