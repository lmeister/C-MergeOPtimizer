package mutation;

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

  public Individual deleteRandomLine(Individual individual) {
    List<String> mutation = List.copyOf(individual.getContents());
    Random rnd = new Random();

    mutation.remove(rnd.nextInt(mutation.size()));

    return new Individual(mutation);
  }

  public Individual swapRandomLines(Individual individual) {
    List<String> mutation = List.copyOf(individual.getContents());
    Random rnd = new Random();

    int indexFirst = rnd.nextInt(mutation.size());
    int indexSecond = rnd.nextInt(mutation.size());
    while (indexFirst == indexSecond) {
      indexSecond = rnd.nextInt(mutation.size());
    }

    return swapLines(individual, indexFirst, indexSecond);
  }

  public Individual swapLines(Individual individual, int indexFirst, int indexSecond) {
    List<String> mutation = List.copyOf(individual.getContents());
    Collections.swap(mutation, indexFirst, indexSecond);

    return new Individual(mutation);
  }

  public Individual swapBlock(List<String> contents, int indexFirst, int indexSecond) {
    List<String> mutation = List.copyOf(contents);
    Collections.swap(mutation, indexFirst, indexSecond);

    return new Individual(mutation);
  }

}
