package evolution.evaluation;

import evolution.Individual;

import java.util.HashMap;

/**
 * AbstractFitnessEvaluator.
 * Base class for the concrete Evaluators.
 */
public abstract class AbstractFitnessEvaluator {

  /**
   * Contains the Individuals and their respective fitness values
   **/
  private final HashMap<Individual, Double> population = new HashMap<>();

  /**
   * Will add an individual to the current population including its fitness value.
   * Should the Individual not compile, it will simply be discarded and not added to the population.
   *
   * @param individual The individual to be added and evaluated.
   * @return Boolean value, stating whether the individual was added or not
   */
  public boolean addIndividual(Individual individual) {
    double fitness = 0.0;

    // if cant compile set fitness = worst val
    // compile individual, if fail ->
    if (fitness != -1) {
      this.population.put(individual, fitness);
      return true;
    }

    return false;
  }

  /**
   * Evaluates the fitness for given individual.
   *
   * @param individual The individual to be evaluated.
   * @return Fitness of the individual. -1 if it fails to compile.
   */
  protected abstract double evaluateFitness(Individual individual);
}
