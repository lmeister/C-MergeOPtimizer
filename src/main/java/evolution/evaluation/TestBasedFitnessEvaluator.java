package evolution.evaluation;

import evolution.CompilerArguments;
import evolution.Configuration;
import evolution.population.Individual;
import util.SourceUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

  public TestBasedFitnessEvaluator(Configuration configuration) {
    this.weightNegativeTestCases = configuration.getWeightNegativeTestCases();
    this.weightPositiveTestCases = configuration.getWeightPositiveTestCases();
    this.timeOut = configuration.getTimeOut();
  }

  /**
   * @param individual        The individual to be evaluated.
   * @param compilerArguments The compiler arguments necessary for compilation
   * @return fitness value. -1 if it does not compile
   * @throws IOException if files can not be found
   */
  @Override
  public double evaluateFitness(Individual individual, CompilerArguments compilerArguments) throws IOException {
    double fitness = 0.0;

    // List<List<String>> compilerArgumentList = compilerArguments.getArgs();
    // TODO disabled for testing meson
    // TODO: This is currently implemented to only test one configuration. When trying to use multiple variants, we should loop over the arguments
    // for (List<String> args : compilerArgumentList) {

    // Hard coded meson arguments in here
    if (SourceUtilities.compile(individual, compilerArguments.getMesonBuild(), compilerArguments.getMesonCompile(), this.timeOut)) {
      // run each test
      for (String test : compilerArguments.getTests()) {
        if (!executeTests(test, this.timeOut)) {
          return -1.0; // if execution fails
        }
      }
      // Read results
      File testResults = new File(Configuration.TEST_RESULT_PATH);
      // Log which test cases have been executed + result
      //Files.lines(Paths.get(Configuration.TEST_RESULT_PATH)).forEach(System.out::println);
      // Add up fitness
      fitness += computeFitness(countTestCases(true, true, testResults),
          countTestCases(false, true, testResults));
    } else {
      // Wenn fail direkt rausbrechen bzw fitness -1 returnen
      return -1.0;
    }
    // Files cleanen (result und individual
    Files.deleteIfExists(Paths.get(Configuration.TEST_RESULT_PATH));
    individual.deleteFiles();
    // }

    // TODO Currently disabled as we're just testing a single variant on scrcpy
    // return fitness / compilerArgumentList.size();
    return fitness;
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
      System.out.println("Test results not located.");
    }

    return occurrences;
  }


  /**
   * Executes given Tests.
   *
   * @param fileName file name of test
   * @param timeOut  timeout in seconds, after whcih testing will be cancelled if not completed
   * @return boolean whether tests have been successfully executed
   * @throws IOException if tests can notb e found
   */
  private boolean executeTests(String fileName, int timeOut) throws IOException {
    ProcessBuilder runTests = new ProcessBuilder(fileName);
    File test = new File(fileName);
    File testDirectory = test.getParentFile();
    runTests.directory(testDirectory);
    Process runTestsProcess = runTests.start();
    try {
      runTestsProcess.waitFor(timeOut, TimeUnit.SECONDS);
      if (runTestsProcess.isAlive()) {
        runTestsProcess.destroy();
      }
    } catch (InterruptedException e) {
      return false;
    }

    return runTestsProcess.exitValue() != 1;
  }
}
