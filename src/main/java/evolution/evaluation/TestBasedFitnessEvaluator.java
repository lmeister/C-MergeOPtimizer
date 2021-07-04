package evolution.evaluation;

import evolution.population.Individual;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * TestBasedFitnessEvaluator class.
 * Manages the individuals and ranks them according to their fitness.
 * Responsible for evaluating the individuals fitness.
 * Fitness is a measure, which is calculated by addition of the weighted amount of both positive and negative test cases passed.
 * Higher fitness equals a better performing individual.
 */
public class TestBasedFitnessEvaluator extends AbstractFitnessEvaluator {

  private final double weightPositiveTestCases;
  private final double weightNegativeTestCases;

  public TestBasedFitnessEvaluator(double weightPositiveTestCases,
                                   double weightNegativeTestCases) {
    this.weightNegativeTestCases = weightNegativeTestCases;
    this.weightPositiveTestCases = weightPositiveTestCases;
  }

  /**
   * Evaluates the fitness for given individual.
   *
   * @param individual The individual to be evaluated.
   * @return Fitness of the individual. -1 if it fails to compile.
   */
  @Override
  protected double evaluateFitness(Individual individual) {
    // TODO: Irgendwo muss das File gespeichert sein, siehe whiteboard
    return (weightPositiveTestCases * countTestCases(true, individual.getTestResults()))
               + (weightNegativeTestCases * countTestCases(false, individual.getTestResults()));
  }

  /**
   * Counts the amount of test cases passed.
   * Reads in the given results file on line-by-line basis.
   *
   * @param positive if true, will search for positive test cases, otherwise negative test cases.
   * @param results  The file with the test results. Must be in defined format.
   * @return The amount of specified test cases passed
   */
  private int countTestCases(boolean positive, File results) {
    String keyword = (positive ? "POS" : "NEG");
    int occurrences = -1;

    try {
      occurrences = Math.toIntExact(
          Files.lines(Paths.get(results.getPath()))
              .filter(line -> line.contains(keyword))
              .count());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return occurrences;
  }
}
