package evolution.population;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class used to represent the individuals of our population.
 */
public class Individual {

  /**
   * Each line equals to an entry in this list
   **/
  private final List<String> contents;

  private String identifier;

  public File getTestResults() {
    return testResults;
  }

  private File testResults;

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

  /*
    TODO
    Wir arbeiten nur in einer Datei?
    Muss man schauen, ob man sich entscheidet nur einen Teil (Den für uns relevanten)
    zu nehmen und dann in die Datei (bzw. eine Kopie einzufügen)
    Oder ob man die gesamte Datei nimmt - Das könnte speichertechnisch echt doof sein
  */
  // TODO Konstruktor erstellen
  public Individual() {
    this.contents = readContents();

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

  public void printContents() {
    this.contents.forEach(System.out::println);
  }

  private List<String> readContents() {
    return null;
  }
}


