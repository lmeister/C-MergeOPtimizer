package util;

import evolution.Configuration;
import evolution.mutation.ManipulationInformationContainer;
import evolution.population.Individual;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * SourceUtilities class offers the utilities required to work with C-Code.
 */
public class SourceUtilities {

  /**
   * Will compile a given Individual according to build and compilation args.
   * Merges the manipulated lines with the original by reading the original on a line by line basis and overwriting the appropriate lines.
   * <p>
   * Will cancel the compilation and build if time taken is longer than defined timeout.
   *
   * @param individual  The individual that is to be compiled
   * @param buildArgs   Building Arguments
   * @param compileArgs Compiling Arguments
   * @param timeout     Timeout in seconds, after which compilation is cancelled
   * @return False if individual can not be compiled or built
   * @throws IOException IOException if it can not be written or original be read
   */
  public static boolean compile(Individual individual, List<String> buildArgs, List<String> compileArgs, int timeout) throws IOException {
    // First merge the manipulated lines with the original
    for (ManipulationInformationContainer mic : individual.getContents()) {
      mergeMutantWithOriginal(mic);
    }

    // TODO: This has to be adjusted when not used with scrcpy, as not every solution requires a building tool
    // First build then compile
    ProcessBuilder build = new ProcessBuilder(buildArgs);
    build.directory(new File(Configuration.PROJECT_PATH));
    Process buildProcess = build.start();
    try {
      buildProcess.waitFor(timeout, TimeUnit.SECONDS);
      // cancel the process and return false if it takes longer than timeout
      if (buildProcess.isAlive()) {
        buildProcess.destroy();
        return false;
      }
    } catch (InterruptedException e) {
      return false;
    }

    ProcessBuilder compile = new ProcessBuilder(compileArgs);
    compile.directory(new File(Configuration.PROJECT_PATH));
    Process compileProcess = compile.start();
    try {
      compileProcess.waitFor(timeout, TimeUnit.SECONDS);
      // cancel the process and return false if it takes longer than timeout
      if (compileProcess.isAlive()) {
        compileProcess.destroy();
        return false;
      }
    } catch (InterruptedException e) {
      return false;
    }
    return compileProcess.exitValue() == 0;
  }

  /**
   * Merges the contents of a ManipulationInformationContainer with the corresponding original file.
   *
   * @param mic The manipulationinformationcontainer holding the information
   * @throws IOException if original can not be read or manipulations not be written
   */
  public static void mergeMutantWithOriginal(ManipulationInformationContainer mic) throws IOException {
    Path original = mic.getPath();
    Map<Integer, String> manipulatedLines = mic.getManipulations();
    List<String> originalContent =
        Files.readAllLines(appendStringBeforeExtension(original, Configuration.ORIGINAL));
    // Perform the merge
    for (Integer key : manipulatedLines.keySet()) {
      originalContent.set(key - 1, manipulatedLines.get(key));
    }

    // Write the file
    // TODO: Account for file existing + overwrite
    Files.write(original, originalContent, Charset.defaultCharset());
  }

  /**
   * Appends a given string to a path before the extension.
   * Example when called with Path "foo/bar/a.c/" and String "_b" -> "foo/bar/a_b.c"
   *
   * @param path   of file
   * @param string the appended string
   * @return the merged path
   */
  public static Path appendStringBeforeExtension(Path path, String string) {
    String extension = path.toString().substring(path.toString().lastIndexOf('.'));
    String pathWithoutExtension = path.toString().substring(0, path.toString().lastIndexOf('.'));
    return Paths.get(pathWithoutExtension + string + extension);
  }

}