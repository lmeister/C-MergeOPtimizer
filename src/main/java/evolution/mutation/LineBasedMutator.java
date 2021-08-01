package evolution.mutation;

import evolution.ManipulationInformationContainer;
import evolution.population.Individual;

import java.util.ArrayList;
import java.util.List;

/**
 * LineBasedMutator class extends the AbstractMutator.
 * LineBasedMutator performs mutations on a line-basis.
 * Currently the following operations are allowed:
 * <ul>
 *   <li>Removal of a line</li>
 *   <li>Swapping of lines</li>
 *   <li>Swapping of a block?</li>
 * </ul>
 */
public class LineBasedMutator extends AbstractMutator {

  /**
   * Performs a random mutation on the original based on chance and returns a new instance.
   *
   * @param original Original individual that is to be mutated
   * @return Returns a new Individual instance containing the mutant.
   */
  @Override
  public Individual mutate(Individual original) {
    double random = rnd.nextDouble();
    Individual mutant = null;

    if (random < 0.33) {
      mutant = swapRandomLines(original);
    } else if (random >= 0.33) {
      mutant = deleteRandomLine(original);
    }
    return mutant;
  }

  /**
   * Deletes a random line in given individual and returns a new instance.
   *
   * @param original Original individual that is to be mutated
   * @return new instance of an individual.
   */
  private Individual deleteRandomLine(Individual original) {
    List<ManipulationInformationContainer> mutation = new ArrayList<>(original.getContents());
    ManipulationInformationContainer container = mutation.get(rnd.nextInt(mutation.size()));

    container.removeLine(rnd.nextInt(container.getSize()));
    return new Individual(mutation);
  }

  /**
   * Swaps two random lines in given individual and returns a new instance.
   *
   * @param original Original individual that is to be mutated
   * @return new instance of an individual.
   */
  private Individual swapRandomLines(Individual original) {
    List<ManipulationInformationContainer> mutation = new ArrayList<>(original.getContents());
    ManipulationInformationContainer container = mutation.get(rnd.nextInt(mutation.size()));

    int indexFirst = rnd.nextInt(container.getSize()); // TODO: Anpassen auf die ManipulationInformation
    int indexSecond = rnd.nextInt(container.getSize());

    while (indexFirst == indexSecond) {
      indexSecond = rnd.nextInt(container.getSize());
    }

    // Triangular swap
    String temp = container.getLine(indexFirst);
    container.update(indexFirst, container.getLine(indexSecond));
    container.update(indexSecond, temp);

    return swapLines(original, indexFirst, indexSecond);
  }

  /**
   * Swaps two given lines in given individual and returns a new instance.
   *
   * @param original    Original individual that is to be mutated
   * @param indexFirst  index of the first line
   * @param indexSecond index of the second line
   * @return new instance of an individual.
   */
  private Individual swapLines(Individual original, int indexFirst, int indexSecond) {
    List<ManipulationInformationContainer> mutation = new ArrayList<>(original.getContents());
    ManipulationInformationContainer container = mutation.get(rnd.nextInt(mutation.size()));

    // Triangular swap
    String temp = container.getLine(indexFirst);
    container.update(indexFirst, container.getLine(indexSecond));
    container.update(indexSecond, temp);

    return new Individual(mutation);
  }

  /**
   * Swaps two blocks of given size and returns a mutated individual
   *
   * @param original         The individual that is to be mutated
   * @param firstBlockStart  Start index of first block (inclusive)
   * @param firstBlockEnd    End index of first block (inclusive)
   * @param secondBlockStart Start index second first block (inclusive)
   * @param secondBlockEnd   End index of second block (inclusive)
   * @return Mutated individual or null if invalid indices are given
   */
//  private Individual swapBlock(Individual original, int firstBlockStart, int firstBlockEnd, int secondBlockStart, int secondBlockEnd) {
//    List<String> contents = new ArrayList<>(original.getContents());
//
//    List<String> mutation = new ArrayList<>(contents.subList(0, firstBlockStart));
//    mutation.addAll(contents.subList(secondBlockStart, secondBlockEnd + 1));
//    mutation.addAll(contents.subList(firstBlockEnd + 1, secondBlockStart));
//    mutation.addAll(contents.subList(firstBlockStart, firstBlockEnd + 1));
//    mutation.addAll(contents.subList(secondBlockEnd + 1, contents.size()));
//
//    return new Individual(mutation);
//  }

  /**
   * Performs a crossover between two individuals.
   * Will choose random crossover point and take sublist 0, point from parent1 and point, end from parent 2.
   *
   * @param parent1 First parent to be used for crossover
   * @param parent2 Second parent to be used for crossover
   * @return A new individual, result of crossover
   */
//  private Individual crossover(Individual parent1, Individual parent2) {
//    int crossoverPoint = rnd.nextInt(parent1.getContents().size());
//    List<String> contents = new ArrayList<>(parent1.getContents().subList(0, crossoverPoint));
//    contents.addAll(parent2.getContents().subList(crossoverPoint, parent2.getContents().size()));
//
//    return new Individual(contents);
//  }

}
