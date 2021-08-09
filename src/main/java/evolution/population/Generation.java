package evolution.population;

import java.util.*;

/**
 * This class resembles a generation of individuals.
 * Saves the individuals and their fitness value in a map.
 * Also offers methods for retrieval of fittest individuals and tournament selection.
 */
public class Generation {

  private static int generationId;
  private final HashMap<Individual, Double> individualFitnessMap;

  public Generation(boolean temporary) {
    if (!temporary) {
      generationId++;
    }
    this.individualFitnessMap = new HashMap<>();
  }

  /**
   * Will add an individual to the current population including its fitness value.
   * Will ignore non compiling individuals.
   *
   * @param individual The individual to be added and evaluated.
   * @param fitness    The fitness of the added individual.
   * @return Boolean value, stating whether the individual was added or not
   */
  public boolean addIndividual(Individual individual, double fitness) {
    if (fitness >= 0.0) {
      this.individualFitnessMap.put(individual, fitness);
      return true;
    }
    return false;
  }

  public static int getGenerationId() {
    return generationId;
  }

  /**
   * Retrieves the fittest individual of the generation.
   *
   * @return null if no individual found (shouldnt happen), else fittest individual
   */
  public Individual getFittestIndividual() {
    double bestFitness = -1;
    Individual fittestIndividual = null;

    for (Individual individual : individualFitnessMap.keySet()) {
      if (individualFitnessMap.get(individual) != null
              && individualFitnessMap.get(individual) >= bestFitness) {
        bestFitness = individualFitnessMap.get(individual);
        fittestIndividual = individual;
      }
    }
    return fittestIndividual;
  }

  public int getPopulationSize() {
    return this.individualFitnessMap.size();
  }

  /**
   * Returns the fitness of given individual
   *
   * @param individual The individual in question
   * @return -1 if individual not in population, otherwise fitness as double
   */
  public double getFitnessOfIndividual(Individual individual) {
    if (this.individualFitnessMap.get(individual) != null) {
      return this.individualFitnessMap.get(individual);
    }
    return -1;
  }

  /**
   * Performs tournament selection algorithm to select an individual.
   * Tournament size is half the population size
   *
   * @return the fittest individual of the tournament
   */
  public Individual tournamentSelection() {
    // Create temporary generation
    Generation tournament = new Generation(true);
    // Retrieve all Individuals of the generation and shuffle them around
    List<Individual> candidates = new ArrayList<>(this.individualFitnessMap.keySet());
    Collections.shuffle(candidates);

    // Perform tournament with half the population
    for (int i = 0; i < (this.getPopulationSize() / 2); i++) {
      tournament.addIndividual(candidates.get(i), this.individualFitnessMap.get(candidates.get(i)));
    }

    return tournament.getFittestIndividual();
  }

  // only for logging
  public OptionalDouble getAverageFitness() {
    return this.individualFitnessMap.values().stream().mapToDouble(fitness -> fitness).average();
  }
}
