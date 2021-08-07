package evolution;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Encapsulates several configuration properties.
 */
public class Configuration {
  public final static String ORIGINAL = "_original";
  public static String TEST_RESULT_PATH;
  public static String PROJECT_PATH;

  private final double weightPositiveTestCases;
  private final double weightNegativeTestCases;

  private final int amountOfPositiveTestCases;
  private final int amountOfNegativeTestCases;

  private final int maxGenerations;
  private final int generationSize;
  private final int maxRuntimeInSeconds;

  private final Path diffFile;

  private int timeOut;

  public double getWeightPositiveTestCases() {
    return weightPositiveTestCases;
  }

  public double getWeightNegativeTestCases() {
    return weightNegativeTestCases;
  }

  public int getAmountOfPositiveTestCases() {
    return amountOfPositiveTestCases;
  }

  public int getAmountOfNegativeTestCases() {
    return amountOfNegativeTestCases;
  }

  public int getMaxGenerations() {
    return maxGenerations;
  }

  public int getGenerationSize() {
    return generationSize;
  }

  public Path getDiffPath() {
    return diffFile;
  }

  public int getTimeOut() {
    return this.timeOut;
  }

  public int getMaxRuntimeInSeconds() {
    return this.maxRuntimeInSeconds;
  }

  public Configuration(File properties) throws IOException {
    Properties configuration = new Properties();
    configuration.load(new FileInputStream(properties.getAbsolutePath()));
    this.weightPositiveTestCases = Double.parseDouble(configuration.getProperty("weightPositiveTestCases"));
    this.weightNegativeTestCases = Double.parseDouble(configuration.getProperty("weightNegativeTestCases"));
    this.amountOfNegativeTestCases = Integer.parseInt(configuration.getProperty("amountOfNegativeTestCases"));
    this.amountOfPositiveTestCases = Integer.parseInt(configuration.getProperty("amountOfPositiveTestCases"));

    this.maxGenerations = Integer.parseInt(configuration.getProperty("maxGenerations"));
    this.generationSize = Integer.parseInt(configuration.getProperty("generationSize"));
    this.maxRuntimeInSeconds = Integer.parseInt(configuration.getProperty("maxRuntimeInSeconds"));

    this.diffFile = Paths.get(configuration.getProperty("diffFilePath"));
    TEST_RESULT_PATH = configuration.getProperty("testResultPath");
    PROJECT_PATH = configuration.getProperty("projectPath");

    this.timeOut = Integer.parseInt(configuration.getProperty("timeOut"));
  }
}
