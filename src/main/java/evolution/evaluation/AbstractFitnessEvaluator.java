package evolution.evaluation;

import evolution.CompilerArguments;
import evolution.population.Individual;

import java.io.IOException;

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
  public abstract double evaluateFitness(Individual individual, CompilerArguments compilerArguments) throws IOException, InterruptedException;
}
