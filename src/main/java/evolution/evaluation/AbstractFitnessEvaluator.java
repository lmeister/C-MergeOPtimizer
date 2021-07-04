package evolution.evaluation;

import evolution.population.Individual;

/**
 * AbstractFitnessEvaluator.
 * Base class for the concrete Evaluators.
 */
public abstract class AbstractFitnessEvaluator {

  /**
   * Evaluates the fitness for given individual.
   *
   * @param individual The individual to be evaluated.
   * @return Fitness of the individual. -1 if it fails to compile.
   */
  protected abstract double evaluateFitness(Individual individual);
}
