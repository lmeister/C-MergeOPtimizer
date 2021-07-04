package evolution.population;

import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * This class resembles a generation of individuals.
 * Saves the individuals and their fitness value in a map.
 * Also offers methods for retrieval of fittest individuals and tournament selection.
 */
public class Generation {

  private static int generationId;
  private final HashMap<Individual, Double> individualFitnessMap;

  public Generation() {
    generationId++;
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
    if (fitness != -1) {
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
   * @return Optional containing the fittest individual, or empty if none was found
   */
  public Optional<Individual> getFittestIndividual() {
    double bestFitness = -1;
    Optional<Individual> fittestIndividual = Optional.empty();

    for (Individual individual : individualFitnessMap.keySet()) {
      if (individualFitnessMap.get(individual) != null
              && individualFitnessMap.get(individual) > bestFitness) {
        fittestIndividual = Optional.of(individual);
      }
    }
    return fittestIndividual;
  }

  public Individual getIndividualByTournamentSelection() {
    return null;
  }


  public OptionalDouble getFitnessOfIndividual(Individual individual) {
    OptionalDouble fitness = OptionalDouble.empty();
    if (this.individualFitnessMap.get(individual) != null) {
      fitness = OptionalDouble.of(this.individualFitnessMap.get(individual));
    }
    return fitness;
  }

}
