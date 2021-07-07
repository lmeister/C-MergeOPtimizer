package evolution;

import evolution.evaluation.AbstractFitnessEvaluator;
import evolution.mutation.AbstractMutator;
import evolution.population.Generation;
import evolution.population.Individual;

import java.io.File;
import java.util.Optional;

/**
 * Optimizer Class performs the optimization of the c-code.
 */
public class Optimizer {

  private final int maxGenerations;
  private Generation generation;
  private final double fitnessGoal;
  private final int populationSize;
  private final AbstractMutator mutator;
  private final AbstractFitnessEvaluator evaluator;
  private final String originalFilePath;

  /**
   * Constructor for the optimizer.
   *
   * @param maxGenerations as termination criterion
   * @param evaluator      Selected fitness evaluator
   * @param mutator        Selected mutator
   */
  public Optimizer(int maxGenerations, double fitnessGoal,
                   AbstractFitnessEvaluator evaluator, AbstractMutator mutator,
                   int populationSize, String originalFilePath) {
    this.maxGenerations = maxGenerations;
    this.mutator = mutator;
    this.evaluator = evaluator;
    this.fitnessGoal = fitnessGoal;
    this.populationSize = populationSize;
    this.originalFilePath = originalFilePath;
    this.generation = generateInitialPopulation(new File(originalFilePath));
  }

  /**
   * Runs the optimization loop to find an accepted solution.
   *
   * @return Optional, containing the accepted individual or an empty optional, if not acceptable solution was found.
   */
  public Optional<Individual> optimize() {
    Optional<Individual> result = Optional.empty();

    // Perform genetic algorithm loop
    while (Generation.getGenerationId() <= this.maxGenerations) {
      System.out.println("Generation: " + Generation.getGenerationId());
      // Check if fittest Individual of the generation meets the target, if so, return it
      Individual fittestIndividual = this.generation.getFittestIndividual();
      if (fittestIndividual != null) {
        if (checkIfGoalMet(fittestIndividual)) {
          return Optional.of(fittestIndividual);
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
    double fitnessOfFittestIndividual;
    // Check if fittest Individual if present, if yes, calculate fitness and compare to fitnessgoal
    fitnessOfFittestIndividual = this.generation.getFitnessOfIndividual(candidate);
    // if fitness of fittest individual is present and at least as high as goal, return individual as solution
    return fitnessOfFittestIndividual >= this.fitnessGoal;
  }

  /**
   * Creates the evolved generation, containing the new mutations.
   *
   * @return new Generation object.
   */
  private Generation createEvolvedGeneration() {
    int invalidCounter = 0; // TODO this is used for evaluation only - Maybe replace with logger
    Generation newGeneration = new Generation(false);

    while (newGeneration.getPopulationSize() < populationSize) {
      Individual individualToBeMutated = this.generation.tournamentSelection();
      Individual mutant = mutator.mutate(individualToBeMutated);

      String identifier = "Gen" + Generation.getGenerationId() + "Ind" + newGeneration.getPopulationSize();
      mutant.setIdentifier(identifier);

      double fitnessOfMutant = evaluator.evaluateFitness(mutant);
      // TODO this is used for evaluation only - if is unnecessary - Maybe replace with logger
      if (!newGeneration.addIndividual(mutant, fitnessOfMutant)) {
        invalidCounter++;
      }
    }

    // TODO this is used for evaluation only  - Maybe replace with logger
    System.out.println("Created " + invalidCounter + " invalid mutants.");
    return newGeneration;
  }

  /**
   * Generates the initial population, which are only random mutants of the original file.
   *
   * @return new Generation
   */
  private Generation generateInitialPopulation(File originalSource) {
    Individual original = new Individual(originalSource);
    Generation generation = new Generation(false);
    // Create new mutants as long as populationSize hasn't been reached by this population
    while (generation.getPopulationSize() < this.populationSize) {
      Individual mutant = this.mutator.mutate(original);
      generation.addIndividual(mutant, evaluator.evaluateFitness(original));
    }
    return null;
  }

}
