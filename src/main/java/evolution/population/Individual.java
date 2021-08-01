package evolution.population;

import evolution.ManipulationInformationContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

/**
 * Class used to represent the individuals of our population.
 */
public class Individual {

  private final List<ManipulationInformationContainer> contents;

  public Individual(List<ManipulationInformationContainer> contents) {
    this.contents = contents;
  }

  public List<ManipulationInformationContainer> getContents() {
    return this.contents;
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

  public void deleteFiles() throws IOException {
    for (ManipulationInformationContainer mic : contents) {
      Files.deleteIfExists(mic.getPath());
    }
  }
}


