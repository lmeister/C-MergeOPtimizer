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
  private double weightPositiveTestCases;
  private double weightNegativeTestCases;

  private int amountOfPositiveTestCases;
  private int amountOfNegativeTestCases;

  private int maxGenerations;
  private int generationSize;

  private Path diffFile;

  public int timeOut;

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

  public int getNeighborhoodRange() {
    return neighborhoodRange;
  }

  public int getTimeOut() {
    return this.timeOut;
  }

  private int neighborhoodRange;

  public Configuration(File properties) throws IOException {
    Properties configuration = new Properties();
    configuration.load(new FileInputStream(properties.getAbsolutePath()));
    this.weightPositiveTestCases = Double.parseDouble(configuration.getProperty("weightPositiveTestCases"));
    this.weightNegativeTestCases = Double.parseDouble(configuration.getProperty("weightNegativeTestCases"));
    this.amountOfNegativeTestCases = Integer.parseInt(configuration.getProperty("amountOfNegativeTestCases"));
    this.amountOfPositiveTestCases = Integer.parseInt(configuration.getProperty("amountOfPositiveTestCases"));

    this.maxGenerations = Integer.parseInt(configuration.getProperty("maxGenerations"));
    this.generationSize = Integer.parseInt(configuration.getProperty("generationSize"));

    this.diffFile = Paths.get(configuration.getProperty("diffFilePath"));
    TEST_RESULT_PATH = configuration.getProperty("testResultPath");

    this.timeOut = Integer.parseInt(configuration.getProperty("timeOut"));

    this.neighborhoodRange = Integer.parseInt(configuration.getProperty("neighborhoodRange"));
  }
}
