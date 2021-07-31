package evolution.population;

import evolution.ManipulationInformationContainer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
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
  private final List<String> contents;

  private String identifier;

  private File file;

  private List<ManipulationInformationContainer> manipulationInformationContainers;

  /**
   * Needs to be array to account for variability (different test results for each variant)
   */
  private File[] testResults;

  public Individual(List<String> contents) {
    this.contents = contents;
  }

  public List<String> getContents() {
    return Collections.unmodifiableList(this.contents);
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

  /*
    TODO
    Wir arbeiten nur in einer Datei?
    Muss man schauen, ob man sich entscheidet nur einen Teil (Den für uns relevanten)
    zu nehmen und dann in die Datei (bzw. eine Kopie einzufügen)
    Oder ob man die gesamte Datei nimmt - Das könnte speichertechnisch echt doof sein
  */
  public Individual(File source) throws IOException {
    this.contents = readContents(source);
    this.file = new File(createPathOfMutation(source.getPath()));
    // TODO hier muss dann noch für diffs angepasst werden
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
    return Files.lines(this.file.toPath())
               .collect(Collectors.toList());
  }

  public void toFile() throws IOException {
    Files.write(this.file.toPath(), this.contents, Charset.defaultCharset());
  }

  private String createPathOfMutation(String originalPath) {
    // Cut off the extension
    String extension = originalPath.substring(originalPath.lastIndexOf('.'));
    String pathWithoutExtension = originalPath.substring(0, originalPath.lastIndexOf('.'));
    // output path will be pathToDir/fileName_Identifier.Extension
    return (pathWithoutExtension + "_" + this.getIdentifier() + extension);
  }

  public void deleteFile() throws IOException {
    Files.deleteIfExists(this.file.toPath());
  }
}


