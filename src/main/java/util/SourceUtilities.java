package util;

import evolution.Configuration;
import evolution.ManipulationInformationContainer;
import evolution.population.Individual;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * SourceUtilities class offers the utilities required to work with C-Code.
 */
public class SourceUtilities {

  /**
   * Will compile given source and output into given target.
   *
   * @return a boolean value indicating whether compilation was succesful.
   */
  public static boolean compile(Individual individual, List<String> buildArgs, List<String> compileArgs) throws IOException, InterruptedException {
    // First merge the manipulated lines with the original
    for (ManipulationInformationContainer mic : individual.getContents()) {
      mergeMutantWithOriginal(mic);
    }
    // Then compile it
    //Process compile = new ProcessBuilder(compilerArgs).start();
    ProcessBuilder build = new ProcessBuilder(buildArgs);
    build.directory(new File(Configuration.PROJECT_PATH));
    Process buildProcess = build.start();
    buildProcess.waitFor();

    ProcessBuilder compile = new ProcessBuilder(compileArgs);
    compile.directory(new File(Configuration.PROJECT_PATH));
    Process compileProcess = compile.start();
    compileProcess.waitFor();

    return compileProcess.exitValue() != 1;
  }

  /**
   * Retrieves the content of the original file and merges it in memory with the manipulated line. Then writes the new file.
   *
   * @param mic ManipulationInformationContainer containing the mutations
   * @throws IOException
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
   * @param path
   * @param string
   * @return
   */
  public static Path appendStringBeforeExtension(Path path, String string) {
    String extension = path.toString().substring(path.toString().lastIndexOf('.'));
    String pathWithoutExtension = path.toString().substring(0, path.toString().lastIndexOf('.'));
    return Paths.get(pathWithoutExtension + string + extension);
  }

}