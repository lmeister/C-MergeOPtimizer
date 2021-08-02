package evolution;

import evolution.evaluation.AbstractFitnessEvaluator;
import evolution.mutation.AbstractMutator;
import evolution.population.Generation;
import evolution.population.Individual;
import util.GitDiffParser;
import util.SourceUtilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

  private CompilerArguments compilerArguments;

  private Individual original;

  /**
   * Constructor for the optimizer.
   *
   * @param maxGenerations as termination criterion
   * @param evaluator      Selected fitness evaluator
   * @param mutator        Selected mutator
   * @throws IOException if source file does not exist
   */
  public Optimizer(int maxGenerations, double fitnessGoal,
                   AbstractFitnessEvaluator evaluator, AbstractMutator mutator,
                   int populationSize, Path pathToDiff) throws IOException {
    this.maxGenerations = maxGenerations;
    this.mutator = mutator;
    this.evaluator = evaluator;
    this.fitnessGoal = fitnessGoal;
    this.populationSize = populationSize;

    // Create original individual
    GitDiffParser parser = new GitDiffParser();
    // Retrieve the relevant files from git diff
    this.original = new Individual(parser.parseDiff(pathToDiff));

    this.generation = generateInitialPopulation(this.original);
  }

  /**
   * Runs the optimization loop to find an accepted solution.
   *
   * @return Optional, containing the accepted individual or an empty optional, if not acceptable solution was found.
   */
  public Optional<Individual> optimize() throws IOException {
    Optional<Individual> result = Optional.empty();

    // Copy the original files and add pre-/suffix? original_
    // Um zu wissen wie es heißt brauchen wir ja dann doch den path in jedem mic, auch wenn überall gleich
    // können den beim original dann ja hier direkt editieren und das original ranhängen
    for (ManipulationInformationContainer mic : this.original.getContents()) {
      // Copy the originals to the target path
      Files.copy(mic.getPath(),
          SourceUtilities.appendStringBeforeExtension(mic.getPath(), Configuration.ORIGINAL));
    }

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
   * Evaluates the individuals
   *
   * @return new Generation object.
   */
  private Generation createEvolvedGeneration() {
    int invalidCounter = 0; // TODO this is used for evaluation only - Maybe replace with logger
    Generation newGeneration = new Generation(false);

    while (newGeneration.getPopulationSize() < populationSize) {
      Individual individualToBeMutated = this.generation.tournamentSelection();
      Individual mutant = mutator.mutate(individualToBeMutated);

      double fitnessOfMutant = 0;
      try {
        fitnessOfMutant = evaluator.evaluateFitness(mutant, compilerArguments);
      } catch (IOException e) {
        System.out.println("IO exception create evolved");
        e.printStackTrace();
      } catch (InterruptedException e) {
        System.out.println("Interrupted exception create evolved");
        e.printStackTrace();
      }
      // TODO this is used for evaluation only - if is unnecessary - Maybe replace with logger
      if (!newGeneration.addIndividual(mutant, fitnessOfMutant)) {
        invalidCounter++;
      }
    }

    // clean files of the last generation
    this.generation.cleanFiles();

    // TODO this is used for evaluation only  - Maybe replace with logger
    System.out.println("Created " + invalidCounter + " invalid mutants.");
    return newGeneration;
  }

  /**
   * Generates the initial population, which are only random mutants of the original file.
   *
   * @return new Generation
   */
  private Generation generateInitialPopulation(Individual original) throws IOException {
    Generation generation = new Generation(false);
    // Create new mutants as long as populationSize hasn't been reached by this population
    while (generation.getPopulationSize() < this.populationSize) {
      Individual mutant = this.mutator.mutate(original);
      try {
        generation.addIndividual(mutant, evaluator.evaluateFitness(original, compilerArguments));
      } catch (InterruptedException e) {
        System.out.println("Interrupted exception create initial");
      }
    }
    return null;
  }

}
