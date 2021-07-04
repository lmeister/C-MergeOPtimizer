package evolution;

import evolution.evaluation.AbstractFitnessEvaluator;
import evolution.mutation.AbstractMutator;
import evolution.population.Generation;
import evolution.population.Individual;

import java.util.Optional;
import java.util.OptionalDouble;

public class Optimizer {

  private final int maxGenerations;
  private final AbstractFitnessEvaluator evaluator;
  private final AbstractMutator mutator;
  private final Generation generation;
  private final double fitnessGoal;

  /**
   * Constructor for the optimizer.
   *
   * @param maxGenerations as termination criterion
   * @param evaluator      Selected fitness evaluator
   * @param mutator        Selected mutator
   * @param generation     Current population
   */
  public Optimizer(int maxGenerations, AbstractFitnessEvaluator evaluator, AbstractMutator mutator, Generation generation, double fitnessGoal) {
    this.maxGenerations = maxGenerations;
    this.evaluator = evaluator;
    this.mutator = mutator;
    this.generation = generation;
    this.fitnessGoal = fitnessGoal;
  }

  /**
   * Runs the optimization loop to find an accepted solution.
   *
   * @return Optional, containing the accepted individual or an empty optional, if solution not acceptable.
   */
  public Optional<Individual> optimize() {
    Optional<Individual> result = Optional.empty();

    // Perform GA loop
    while (Generation.getGenerationId() <= this.maxGenerations) {

      // Check if fittest Individual of the generation meets the target, if so, return it
      Optional<Individual> fittestIndividual = this.generation.getFittestIndividual();
      if (checkIfGoalMet(fittestIndividual)) {
        return fittestIndividual;
      }

      // perform mutations and create new generation
    }
    return result;
  }

  /**
   * Retrieves fitness of candidate solution and compares it to the target
   *
   * @param candidate Optional of Individual that is to be checked against target
   * @return true if target has been met, otherwise false
   */
  private boolean checkIfGoalMet(Optional<Individual> candidate) {
    // Check for acceptable solution
    Optional<Individual> fittestIndividual = this.generation.getFittestIndividual();
    OptionalDouble fitnessOfFittestIndividual = OptionalDouble.empty();

    // Check if fittest Individual if present, if yes, calculate fitness and compare to fitnessgoal
    if (fittestIndividual.isPresent()) {
      fitnessOfFittestIndividual = this.generation.getFitnessOfIndividual(fittestIndividual.get());
      // if fitness of fittest individual is present and at least as high as goal, return individual as solution
      if (fitnessOfFittestIndividual.isPresent()) {
        return fitnessOfFittestIndividual.getAsDouble() >= this.fitnessGoal;
      }
    }

    return false;
  }

}
