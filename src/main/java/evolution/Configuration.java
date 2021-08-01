package evolution;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Encapsulates several configuration properties.
 */
public class Configuration {
  public final static String ORIGINAL = "_original";
  private double weightPositiveTestCases;
  private double weightNegativeTestCases;

  private int amountOfPositiveTestCases;
  private int amountOfNegativeTestCases;

  private int maxGenerations;
  private int generationSize;

  private File diffFile;

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

  public File getDiffFile() {
    return diffFile;
  }

  public int getNeighborhoodRange() {
    return neighborhoodRange;
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

    this.diffFile = new File(configuration.getProperty("diffFilePath"));


    this.neighborhoodRange = Integer.parseInt(configuration.getProperty("neighborhoodRange"));
  }
}
