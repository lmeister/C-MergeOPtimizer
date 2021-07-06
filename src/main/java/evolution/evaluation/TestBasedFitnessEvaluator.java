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
  public double evaluateFitness(Individual individual) {
    // TODO: Irgendwo muss das File gespeichert sein, siehe whiteboard
    return (weightPositiveTestCases * countTestCases(true, true, individual.getTestResults()))
               + (weightNegativeTestCases * countTestCases(false, true, individual.getTestResults()));
  }

  /**
   * Counts the amount of test cases passed.
   * Reads in the given results file on line-by-line basis.
   *
   * @param positive if true, will search for positive test cases, otherwise negative test cases.
   * @param passed   if true, will search for passed test cases, else for failed
   * @param results  The file with the test results. Must be in defined format.
   * @return The amount of specified test cases passed
   */
  private int countTestCases(boolean positive, boolean passed, File results) {
    String type = (positive ? "POS" : "NEG");
    String criterion = (passed ? "passed" : "failed");
    int occurrences = -1;

    try {
      occurrences = Math.toIntExact(
          Files.lines(Paths.get(results.getPath()))
              .filter(line -> line.contains(type) && line.contains(criterion))
              .count());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return occurrences;
  }

  // Todo How to handle the multiple configurations we will test?
  // Execute tests for each variant
  // first protype without different variants, only define TEST macro
  private boolean executeTests(String file) {


    return false;
  }
}
