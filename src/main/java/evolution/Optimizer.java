package evolution;

import evolution.evaluation.AbstractFitnessEvaluator;
import evolution.mutation.AbstractMutator;
import evolution.population.Generation;
import evolution.population.Individual;

import java.util.Optional;
import java.util.OptionalDouble;

public class Optimizer {

  private final int maxGenerations;
  private Generation generation;
  private final double fitnessGoal;
  private final int populationSize;
  private final AbstractMutator mutator;
  private final AbstractFitnessEvaluator evaluator;

  /**
   * Constructor for the optimizer.
   *
   * @param maxGenerations as termination criterion
   * @param evaluator      Selected fitness evaluator
   * @param mutator        Selected mutator
   * @param generation     Current population
   */
  public Optimizer(int maxGenerations, Generation generation, double fitnessGoal, AbstractFitnessEvaluator evaluator, AbstractMutator mutator, int populationSize) {
    this.maxGenerations = maxGenerations;
    this.generation = generation;
    this.fitnessGoal = fitnessGoal;
    this.mutator = mutator;
    this.evaluator = evaluator;
    this.populationSize = populationSize;
  }

  /**
   * Runs the optimization loop to find an accepted solution.
   *
   * @return Optional, containing the accepted individual or an empty optional, if solution not acceptable.
   */
  public Optional<Individual> optimize() {
    Optional<Individual> result = Optional.empty();

    // Perform genetic algorithm loop
    while (Generation.getGenerationId() <= this.maxGenerations) {
      // Check if fittest Individual of the generation meets the target, if so, return it
      Optional<Individual> fittestIndividual = this.generation.getFittestIndividual();
      if (fittestIndividual.isPresent()) {
        if (checkIfGoalMet(fittestIndividual.get())) {
          return fittestIndividual;
        }
      }

      // Create a new evolved generation and set it as current generation
      this.generation = createEvolvedGeneration();
    }

    return result;
  }

  /**
   * Retrieves fitness of candidate solution and compares it to the target
   *
   * @param candidate Individual that is to be checked against target
   * @return true if target has been met, otherwise false
   */
  private boolean checkIfGoalMet(Individual candidate) {
    OptionalDouble fitnessOfFittestIndividual;

    // Check if fittest Individual if present, if yes, calculate fitness and compare to fitnessgoal
    fitnessOfFittestIndividual = this.generation.getFitnessOfIndividual(candidate);
    // if fitness of fittest individual is present and at least as high as goal, return individual as solution
    if (fitnessOfFittestIndividual.isPresent()) {
      return fitnessOfFittestIndividual.getAsDouble() >= this.fitnessGoal;
    }

    return false;
  }

  private Generation createEvolvedGeneration() {
    int invalidCounter = 0; // this is used for evaluation only
    // perform mutations and create new generation
    Generation newGeneration = new Generation();

    while (newGeneration.getPopulationSize() < populationSize) {
      Individual individualToBeMutated = generation.getIndividualByTournamentSelection();
      Individual mutant = mutator.mutate(individualToBeMutated);
      double fitnessOfMutant = evaluator.evaluateFitness(mutant);
      // this is used for evaluation only - if is unnecessary
      if (!newGeneration.addIndividual(mutant, fitnessOfMutant)) {
        System.out.println("Created an invalid mutant. Counter: " + invalidCounter);
        invalidCounter++;
      }
    }

    return newGeneration;
  }

}
