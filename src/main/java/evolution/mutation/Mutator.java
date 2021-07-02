package evolution.mutation;

import evolution.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Resembles a Mutator object.
 * Mutator is responsible for mutating a given individual.
 * Currently the following operations are allowed:
 * <ul>
 *   <li>Removal of a line</li>
 *   <li>Swapping of lines</li>
 *   <li>Swapping of a block?</li>
 * </ul>
 */
public class Mutator {
  private final Random rnd;

  public Mutator() {
    rnd = new Random();
  }

  public Individual deleteRandomLine(Individual individual) {
    List<String> mutation = new ArrayList<>(individual.getContents());

    mutation.remove(rnd.nextInt(mutation.size()));
    return new Individual(mutation);
  }

  public Individual swapRandomLines(Individual individual) {
    List<String> mutation = new ArrayList<>(individual.getContents());

    int indexFirst = rnd.nextInt(mutation.size());
    int indexSecond = rnd.nextInt(mutation.size());

    while (indexFirst == indexSecond) {
      indexSecond = rnd.nextInt(mutation.size());
    }

    return swapLines(individual, indexFirst, indexSecond);
  }

  public Individual swapLines(Individual individual, int indexFirst, int indexSecond) {
    List<String> mutation = new ArrayList<>(individual.getContents());
    Collections.swap(mutation, indexFirst, indexSecond);

    return new Individual(mutation);
  }

  /**
   * Swaps two blocks of given size and returns a mutated individual
   *
   * @param individual       The individual that is to be mutated
   * @param firstBlockStart  Start index of first block (inclusive)
   * @param firstBlockEnd    End index of first block (inclusive)
   * @param secondBlockStart Start index second first block (inclusive)
   * @param secondBlockEnd   End index of second block (inclusive)
   * @return Mutated individual or null if invalid indices are given
   */
  public Individual swapBlock(Individual individual, int firstBlockStart, int firstBlockEnd, int secondBlockStart, int secondBlockEnd) {
    List<String> contents = new ArrayList<>(individual.getContents());

    List<String> mutation = new ArrayList<>(contents.subList(0, firstBlockStart));
    mutation.addAll(contents.subList(secondBlockStart, secondBlockEnd + 1));
    mutation.addAll(contents.subList(firstBlockEnd + 1, secondBlockStart));
    mutation.addAll(contents.subList(firstBlockStart, firstBlockEnd + 1));
    mutation.addAll(contents.subList(secondBlockEnd + 1, contents.size()));

    return new Individual(mutation);
  }

  /**
   * Performs a crossover between two individuals.
   * Will choose random crossover point and take sublist 0, point from parent1 and point, end from parent 2.
   *
   * @param parent1 First parent to be used for crossover
   * @param parent2 Second parent to be used for crossover
   * @return A new individual, result of crossover
   */
  public Individual crossover(Individual parent1, Individual parent2) {
    int crossoverPoint = rnd.nextInt(parent1.getContents().size());
    List<String> contents = new ArrayList<>(parent1.getContents().subList(0, crossoverPoint));
    contents.addAll(parent2.getContents().subList(crossoverPoint, parent2.getContents().size()));

    return new Individual(contents);
  }

}
