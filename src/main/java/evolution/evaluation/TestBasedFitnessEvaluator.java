package evolution.evaluation;

import evolution.CompilerArguments;
import evolution.Configuration;
import evolution.population.Individual;
import util.SourceUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

  private final int timeOut;

  public TestBasedFitnessEvaluator(double weightPositiveTestCases,
                                   double weightNegativeTestCases,
                                   int timeOut) {
    this.weightNegativeTestCases = weightNegativeTestCases;
    this.weightPositiveTestCases = weightPositiveTestCases;
    this.timeOut = timeOut;
  }

  /**
   * Evaluates the fitness for given individual.
   * When given multiple variants to test, it will calculate the avg of all the variants.
   *
   * @param individual The individual to be evaluated.
   * @return Fitness of the individual. -1 if it fails to compile.
   */
  @Override
  public double evaluateFitness(Individual individual, CompilerArguments compilerArguments) throws IOException, InterruptedException {
    double fitness = 0.0;

    List<List<String>> compilerArgumentList = compilerArguments.getArgs();
    for (List<String> args : compilerArgumentList) {
      // Kompilieren
      if (SourceUtilities.compile(individual, args)) {
        // Test laufen lassen
        if (!executeTests(compilerArguments.getOutput(), this.timeOut)) {
          return -1.0; // Wenn Ausführung failed
        }
        // Ergebnis einlesen und auf fitness addieren
        File testResults = new File(Configuration.TEST_RESULT_PATH);
        fitness += computeFitness(countTestCases(true, true, testResults),
            countTestCases(false, true, testResults));
      } else {
        // Wenn fail direkt rausbrechen bzw fitness -1 returnen
        return -1.0;
      }
      // Files cleanen (result und individual
      Files.deleteIfExists(Paths.get(Configuration.TEST_RESULT_PATH));
      individual.deleteFiles();
    }
    return fitness / compilerArgumentList.size();
  }

  public double computeFitness(int positivePasses, int negativePasses) {
    // TODO: Irgendwo muss das File gespeichert sein, siehe whiteboard
    return (weightPositiveTestCases * positivePasses)
               + (weightNegativeTestCases * negativePasses);
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

  /**
   * Executes the given file
   *
   * @param fileName Name to the file that should be executed
   * @param timeOut  Determines after how many milliseconds execution will be canceled
   * @return true if execution was sucessful, else false
   * @throws IOException          if I/O error occurs
   * @throws InterruptedException if the current thread is interrupted while waiting
   */
  private boolean executeTests(String fileName, int timeOut) throws IOException, InterruptedException {
    Process runTests = new ProcessBuilder("./", fileName).start();
    runTests.waitFor(timeOut, TimeUnit.SECONDS);

    return runTests.exitValue() != 1;
  }
}
