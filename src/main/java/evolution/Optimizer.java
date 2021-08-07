package evolution;

import evolution.evaluation.AbstractFitnessEvaluator;
import evolution.mutation.AbstractMutator;
import evolution.population.Generation;
import evolution.population.Individual;
import util.GitDiffParser;
import util.SourceUtilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Optimizer Class performs the optimization of the c-code.
 */
public class Optimizer {

  private final int maxGenerations;
  private final double fitnessGoal;
  private final int populationSize;
  private final AbstractMutator mutator;
  private final AbstractFitnessEvaluator evaluator;
  private final CompilerArguments compilerArguments;
  private final Individual original;
  private Generation generation;
  private Configuration configuration;


  // TODO only for evaluation
  private List<Integer> invalids = new ArrayList<>();

  public List<Integer> getInvalids() {
    return invalids;
  }

  /**
   * Constructor for the optimizer.
   *
   * @param evaluator Selected fitness evaluator
   * @param mutator   Selected mutator
   * @throws IOException if source file does not exist
   */
  public Optimizer(double fitnessGoal,
                   AbstractFitnessEvaluator evaluator,
                   AbstractMutator mutator,
                   Configuration configuration) throws IOException {
    this.maxGenerations = configuration.getMaxGenerations();
    this.mutator = mutator;
    this.evaluator = evaluator;
    this.fitnessGoal = fitnessGoal;
    this.populationSize = configuration.getGenerationSize();
    this.configuration = configuration;


    // Create original individual
    GitDiffParser parser = new GitDiffParser();
    // Retrieve the relevant files from git diff
    this.original = new Individual(parser.parseDiff(configuration.getDiffPath()));

    // Read the compiler arguments
    this.compilerArguments = new CompilerArguments();
  }

  /**
   * Runs the optimization loop to find an accepted solution.
   *
   * @return Optional, containing the accepted individual or an empty optional, if not acceptable solution was found.
   */
  public Optional<Individual> optimize() throws IOException, InterruptedException {
    long startTime = System.currentTimeMillis();
    long runtime = (System.currentTimeMillis() - startTime) / 1000;
    System.out.println("===========================================================");
    System.out.println("Beginning Optimization.\n");
    Optional<Individual> result = Optional.empty();

    // Copy the original files and add pre-/suffix? original_
    // Um zu wissen wie es heißt brauchen wir ja dann doch den path in jedem mic, auch wenn überall gleich
    // können den beim original dann ja hier direkt editieren und das original ranhängen
    for (ManipulationInformationContainer mic : this.original.getContents()) {
      // Copy the originals to the target path
      Files.copy(mic.getPath(),
          SourceUtilities.appendStringBeforeExtension(mic.getPath(), Configuration.ORIGINAL), StandardCopyOption.REPLACE_EXISTING);
    }

    // First check if original meets the goal
    this.generation = new Generation(true);
    double fitness = this.evaluator.evaluateFitness(original, compilerArguments);
    this.generation.addIndividual(this.original, fitness);
    System.out.println("Original evaluated, Fitness: " + fitness);
    if (checkIfGoalMet()) {
      result = Optional.of(this.original);
    } else {
      // if Original does not satisfy the goal, generate initial population
      this.generation = generateInitialPopulation(this.original);

      // Check if there is a solution in the initial generation already
      if (checkIfGoalMet()) {
        return Optional.of(this.generation.getFittestIndividual());
      }
      // Perform genetic algorithm loop
      while (Generation.getGenerationId() <= this.maxGenerations && runtime <= configuration.getMaxRuntimeInSeconds()) {
        // Create a new evolved generation and set it as current generation
        this.generation = createEvolvedGeneration();

        // Check if fittest Individual of the generation meets the target, if so, return it
        if (checkIfGoalMet()) {
          return Optional.of(this.generation.getFittestIndividual());
        }
        runtime = (System.currentTimeMillis() - startTime) / 1000;
      }
    }
    return result;
  }

  /**
   * Retrieves fitness of candidate solution and compares it to the target
   *
   * @return true if target has been met, otherwise false
   */
  private boolean checkIfGoalMet() {
    double fitnessOfFittestIndividual;
    // Check if fittest Individual if present, if yes, calculate fitness and compare to fitnessgoal
    fitnessOfFittestIndividual = this.generation.getFitnessOfIndividual(this.generation.getFittestIndividual());
    // if fitness of fittest individual is present and at least as high as goal, return individual as solution
    return fitnessOfFittestIndividual >= this.fitnessGoal;
  }

  /**
   * Creates the evolved generation, containing the new mutations.
   * Evaluates the individuals
   *
   * @return new Generation object.
   */
  private Generation createEvolvedGeneration() throws IOException {
    int invalidCounter = 0; // TODO this is used for evaluation only - Maybe replace with logger
    Generation newGeneration = new Generation(false);
    System.out.println("GENERATING GENERATION " + Generation.getGenerationId());

    while (newGeneration.getPopulationSize() < populationSize) {
      Individual mutant = new Individual(this.generation.tournamentSelection());
      System.out.println("Population Size: " + newGeneration.getPopulationSize() + ". Generating an Individual:");
      mutant = this.mutator.mutate(mutant);

      try {
        double fitnessOfMutant = evaluator.evaluateFitness(mutant, compilerArguments);
        System.out.println("Fitness: " + fitnessOfMutant);
        if (fitnessOfMutant >= fitnessGoal) {
          System.out.println(newGeneration.getPopulationSize());
          invalids.add(invalidCounter);
          newGeneration.addIndividual(mutant, fitnessOfMutant);
          return newGeneration;
        } else {
          if (!newGeneration.addIndividual(mutant, fitnessOfMutant)) {
            // this is used for evaluation only - if is unnecessary - Maybe replace with logger
            invalidCounter++;
          }
          System.out.println("-----------------------------------------------------------");
        }

      } catch (InterruptedException e) {
        System.out.println("Interrupted exception create evolved");
        e.printStackTrace();
      }

    }

    // TODO this is used for evaluation only  - Maybe replace with logger
    System.out.println("LOG: Created " + invalidCounter + " invalid mutants in Generation "
                           + Generation.getGenerationId() + ".");
    return newGeneration;
  }

  /**
   * Generates the initial population, which are only random mutants of the original file.
   *
   * @return new Generation
   */
  private Generation generateInitialPopulation(Individual original) throws IOException {
    Generation generation = new Generation(false);
    int invalidCounter = 0; // TODO this is used for evaluation only - Maybe replace with logger
    System.out.println("GENERATING INITIAL POPULATION");
    // Create new mutants as long as populationSize hasn't been reached by this population
    while (generation.getPopulationSize() < this.populationSize) {
      System.out.println("Population size: " + generation.getPopulationSize() + ". Generating an individual:");
      Individual mutant = new Individual(original);
      mutant = this.mutator.mutate(mutant);
      try {
        double fitnessOfMutant = evaluator.evaluateFitness(mutant, compilerArguments);
        System.out.println("Fitness of mutant: " + fitnessOfMutant);

        if (!generation.addIndividual(mutant, fitnessOfMutant)) {
          invalidCounter++;
        }
        // If we already find a solution, we can already return the generation
        if (fitnessOfMutant >= fitnessGoal) {
          System.out.println(generation.getPopulationSize());
          invalids.add(invalidCounter);
          return generation;
        }

      } catch (InterruptedException e) {
        System.out.println("Interrupted exception create initial");
      }
      System.out.println("-----------------------------------------------------------");
    }
    invalids.add(invalidCounter);
    return generation;
  }

}
