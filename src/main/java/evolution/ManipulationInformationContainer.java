package evolution;

import java.nio.file.Path;
import java.util.Map;

/**
 * This class encapsulates information necessary to manipulate an original files.
 * Contains the Path to the original file, the contents as a List of Strings and a list of manipulatable lines.
 */
public class ManipulationInformationContainer {
  Path pathToFile;

  /**
   * Key = line number
   * String = line content
   * Allows easy merging into the file
   */
  Map<Integer, String> lineContentMap;


  /**
   * Constructor.
   *
   * @param pathToDiff Path to the git diff object, from which the information will be read;
   */
  public ManipulationInformationContainer(Path pathToDiff) {

    // get contents from diff file
  }


}
