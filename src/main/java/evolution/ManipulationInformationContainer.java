package evolution;

import java.nio.file.Path;
import java.util.Map;

/**
 * This class encapsulates information necessary to manipulate an original files.
 * Contains the Path to the original file, the contents as a List of Strings and a list of manipulatable lines.
 */
public class ManipulationInformationContainer {
  /**
   * This contains the path of the original i.e. source file, used for serializing
   */
  Path pathToFile;

  /**
   * Key = line number
   * String = line content
   * Allows easy merging into the file
   */
  Map<Integer, String> manipulations;


  /**
   * Constructor.
   *
   * @param pathToDiff Path to the git diff object, from which the information will be read;
   */
  public ManipulationInformationContainer(Path pathToDiff) {
    // get contents from diff file
  }

  public ManipulationInformationContainer(Path pathToFile, Map<Integer, String> manipulations) {
    this.pathToFile = pathToFile;
    this.manipulations = manipulations;
  }

  public void removeLine(int line) {
    this.manipulations.remove(line);
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
