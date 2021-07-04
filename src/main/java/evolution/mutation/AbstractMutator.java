package evolution.mutation;

import evolution.population.Individual;

import java.util.Random;

/**
 * Resembles a Mutator object.
 * Mutator is responsible for mutating a given individual.
 * Extending classes should implement the desired mutations.
 * Extending classes should also implement the mutate method, which mutates according to the defined probabilities.
 */
public abstract class AbstractMutator {
  protected final Random rnd;

  public AbstractMutator() {
    this.rnd = new Random();
  }

  /**
   * Performs a random mutation on the original based on chance and returns a new instance.
   *
   * @return Returns a new Individual instance containing the mutant.
   */
  public abstract Individual mutate(Individual original);
}
