package util;

import evolution.Configuration;
import evolution.ManipulationInformationContainer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
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

  /**
   * Compiles the source file with given macro definitions.
   *
   * @param individual The Individual that will be compiled
   * @param lmFlag     Indicates whether -lm should be passed as argument
   * @param macros     Macros can be given with value "MACRO=Value" or simply with "MACRO".
   * @param source     The source file
   * @return false if the individual does not compile properly, true if compilation was successful
   */
//  private boolean compile(String source, Individual individual, boolean lmFlag, String... macros) throws IOException {
//    String output = "";
//
//    List<String> compilationCommand = getBasicCompilationArgs(source, output);
//    compilationCommand.add("-D"); // for Macro definition
//    compilationCommand.addAll(List.of(macros));
//    if (lmFlag) {
//      compilationCommand.add("-lm");
//    }
//
//    String outputName = source + "_" + individual.getIdentifier(); // Todo somehow incorporate variant id
//    Process compilation = new ProcessBuilder(compilationCommand).start();
//
//    return compilation.exitValue() != -1;
//  }

  /**
   * Compiles the source file.
   *
   * @param individual The Individual that will be compiled
   * @param lmFlag     Indicates whether -lm should be passed as argument
   * @param source     The source file
   * @return false if the individual does not compile properly, true if compilation was successful
   */
//  private boolean compile(String source, Individual individual, boolean lmFlag) throws IOException {
//    String output = "";
//
//    List<String> compilationCommand = getBasicCompilationArgs(source, output);
//    if (lmFlag) {
//      compilationCommand.add("-lm");
//    }
//
//    String outputName = source + "_" + individual.getIdentifier(); // Todo somehow incorporate variant id
//    Process compilation = new ProcessBuilder(compilationCommand).start();
//
//    return compilation.exitValue() != -1;
//  }
//
//  private List<String> getBasicCompilationArgs(String source, String output) {
//    List<String> compilationCommand = new ArrayList<>();
//    compilationCommand.add("gcc");
//    compilationCommand.add("-o");
//    compilationCommand.add(output);
//    compilationCommand.add(source);
//
//    return compilationCommand;
//  }
}