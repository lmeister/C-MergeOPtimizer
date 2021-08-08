package evolution.mutation;

import evolution.population.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    Individual mutant = new Individual(original);

    if (random < 0.5) {
      mutant = swapRandomLines(mutant);
    } else if (random >= 0.5) {
      mutant = deleteRandomLine(mutant);
    }
    return mutant;
  }

  /**
   * nsfw Deletes a random line in given individual and returns a new instance.
   *
   * @param original Original individual that is to be mutated
   * @return new instance of an individual.
   */
  private Individual deleteRandomLine(Individual original) {
    Individual copy = new Individual(original);
    List<ManipulationInformationContainer> mutation = new ArrayList<>(copy.getContents());
    ManipulationInformationContainer container = mutation.get(rnd.nextInt(mutation.size()));

    List<Integer> lineNumbers = new ArrayList<>(container.getManipulations().keySet());
    Random r = new Random();
    int randomLine = lineNumbers.get(r.nextInt(lineNumbers.size()));
    System.out.println("Deleting line: " + randomLine + " in File: " + container.getPath());
    container.update(randomLine, "");

    return new Individual(mutation);
  }

  /**
   * Swaps two random lines in given individual and returns a new instance.
   *
   * @param original Original individual that is to be mutated
   * @return new instance of an individual.
   */
  private Individual swapRandomLines(Individual original) {
    Individual copy = new Individual(original);
    List<ManipulationInformationContainer> mutation = new ArrayList<>(copy.getContents());
    ManipulationInformationContainer container = mutation.get(rnd.nextInt(mutation.size()));

    List<Integer> lines = new ArrayList<>(container.getManipulations().keySet());
    int indexFirst = lines.get(rnd.nextInt(lines.size()));
    int indexSecond = lines.get(rnd.nextInt(lines.size()));

    while (indexFirst == indexSecond) {
      indexSecond = lines.get(rnd.nextInt(lines.size()));
    }

    System.out.println("Swapping lines " + indexFirst + " and " + indexSecond + " in File: " + container.getPath());
    // Triangular swap
    String temp = container.getLine(indexFirst);
    container.update(indexFirst, container.getLine(indexSecond));
    container.update(indexSecond, temp);

    return swapLines(copy, indexFirst, indexSecond);
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
    Individual copy = new Individual(original);
    ManipulationInformationContainer container = new ManipulationInformationContainer(copy.getContents().get(rnd.nextInt(copy.getContents().size())));

    // Triangular swap
    String temp = container.getLine(indexFirst);
    container.update(indexFirst, container.getLine(indexSecond));
    container.update(indexSecond, temp);

    return new Individual(copy);
  }

}
