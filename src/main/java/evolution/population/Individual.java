package evolution.population;

import evolution.ManipulationInformationContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class used to represent the individuals of our population.
 */
public class Individual {

  /**
   * Each line equals to an entry in this list
   **/

  private String identifier;

  private File file;

  private List<ManipulationInformationContainer> contents;

  /**
   * Needs to be array to account for variability (different test results for each variant)
   */
  private File[] testResults;

  public Individual(List<ManipulationInformationContainer> contents) {
    this.contents = contents;
  }

  public List<ManipulationInformationContainer> getContents() {
    return this.contents;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public File[] getTestResults() {
    return testResults;
  }

  /**
   * Generated equals function.
   *
   * @param o The object to be compared to
   * @return Boolean value, expressing whether the objects are equal or not
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Individual that = (Individual) o;
    return contents.equals(that.contents);
  }

  /**
   * Generated hashcode function.
   *
   * @return the hashcode.
   */
  @Override
  public int hashCode() {
    return Objects.hash(contents);
  }

  /**
   * Used for debugging.
   */
  public void printContents() {
    this.contents.forEach(System.out::println);
  }

  /**
   * Reads contents from file into a string list.
   *
   * @param file
   * @return List, where each element is line in the file.
   * @throws IOException
   */
  private List<String> readContents(File file) throws IOException {
    return Files.lines(this.
                               file.toPath())
               .collect(Collectors.toList());
  }

  public Path createPathOfMutation(String originalPath) {
    // Cut off the extension
    String extension = originalPath.substring(originalPath.lastIndexOf('.'));
    String pathWithoutExtension = originalPath.substring(0, originalPath.lastIndexOf('.'));
    // output path will be pathToDir/fileName_Identifier.Extension
    return Paths.get(pathWithoutExtension + "_" + this.getIdentifier() + extension);
  }

  public void deleteFiles() throws IOException {
    for (ManipulationInformationContainer mic : contents) {
      Files.deleteIfExists(mic.getPath());
    }
  }
}


