package evolution.mutation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * This class encapsulates information necessary to manipulate an original files.
 * Contains the Path to the original file, the contents as a List of Strings and a list of manipulatable lines.
 */
public class ManipulationInformationContainer {
  /**
   * This contains the path of the original i.e. source file, used for serializing
   */
  private final Path pathToFile;

  /**
   * Key = line number
   * String = line content
   * Allows easy merging into the file
   */
  private final Map<Integer, String> manipulations;

  public ManipulationInformationContainer(Path pathToFile, Map<Integer, String> manipulations) {
    this.pathToFile = pathToFile;
    this.manipulations = manipulations;
  }

  public ManipulationInformationContainer(ManipulationInformationContainer mic) {
    this.pathToFile = Paths.get(mic.pathToFile.toString());
    this.manipulations = new HashMap<>(mic.manipulations);
  }

  public String getLine(int line) {
    return this.manipulations.get(line);
  }

  public void update(int line, String content) {
    if (this.manipulations.containsKey(line)) {
      this.manipulations.put(line, content);
    }
  }

  public int getSize() {
    return this.manipulations.size();
  }

  public Path getPath() {
    return this.pathToFile;
  }

  public Map<Integer, String> getManipulations() {
    return this.manipulations;
  }

}
