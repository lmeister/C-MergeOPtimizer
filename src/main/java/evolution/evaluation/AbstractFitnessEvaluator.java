package evolution.evaluation;

import evolution.population.Individual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractFitnessEvaluator.
 * Base class for the concrete Evaluators.
 */
public abstract class AbstractFitnessEvaluator {

  /**
   * Evaluates the fitness for given individual.
   *
   * @param individual The individual to be evaluated.
   * @return Fitness of the individual. -1 if it fails to compile.
   */
  public abstract double evaluateFitness(Individual individual);

  /**
   * Compiles the source file with given macro definitions.
   *
   * @param individual The Individual that will be compiled
   * @param lmFlag     Indicates whether -lm should be passed as argument
   * @param macros     Macros can be given with value "MACRO=Value" or simply with "MACRO".
   * @param source     The source file
   * @return false if the individual does not compile properly, true if compilation was successful
   */
  private boolean compile(String source, Individual individual, boolean lmFlag, String... macros) throws IOException {
    String output = "";

    List<String> compilationCommand = getBasicCompilationArgs(source, output);
    compilationCommand.add("-D"); // for Macro definition
    compilationCommand.addAll(List.of(macros));
    if (lmFlag) {
      compilationCommand.add("-lm");
    }

    String outputName = source + "_" + individual.getIdentifier(); // Todo somehow incorporate variant id
    Process compilation = new ProcessBuilder(compilationCommand).start();

    return compilation.exitValue() != -1;
  }

  /**
   * Compiles the source file.
   *
   * @param individual The Individual that will be compiled
   * @param lmFlag     Indicates whether -lm should be passed as argument
   * @param source     The source file
   * @return false if the individual does not compile properly, true if compilation was successful
   */
  private boolean compile(String source, Individual individual, boolean lmFlag) throws IOException {
    String output = "";

    List<String> compilationCommand = getBasicCompilationArgs(source, output);
    if (lmFlag) {
      compilationCommand.add("-lm");
    }

    String outputName = source + "_" + individual.getIdentifier(); // Todo somehow incorporate variant id
    Process compilation = new ProcessBuilder(compilationCommand).start();

    return compilation.exitValue() != -1;
  }

  private List<String> getBasicCompilationArgs(String source, String output) {
    List<String> compilationCommand = new ArrayList<>();
    compilationCommand.add("gcc");
    compilationCommand.add("-o");
    compilationCommand.add(output);
    compilationCommand.add(source);

    return compilationCommand;
  }
}
