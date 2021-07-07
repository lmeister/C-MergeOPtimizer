package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SourceUtilities class offers the utilities required to work with C-Code.
 */
public class SourceUtilities {

  /**
   * Will compile given source and output into given target.
   *
   * @return a boolean value indicating whether compilation was succesful.
   */
  public static boolean compile(File source, File target) {
    // TODO this should be initialized with the given compiler args
    String compilerCommand;

    return true;
  }

  private List<String> readContents(File file) throws IOException {
    return Files.lines(Paths.get(file.getPath()))
               .collect(Collectors.toList());
  }
}