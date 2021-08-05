package evolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CompilerArguments {
  private final Scanner scn = new Scanner(System.in);
  /**
   * Each element in this List is structured as "macro1,macro2,macro3"
   */
  private final List<String> variants;
  private final List<String> flags;
  private final String output;

  public CompilerArguments() {
    Scanner scn = new Scanner(System.in);
    this.output = scn.next();
    this.flags = readStringList("Enter your flags including the dash. " +
                                    "Entering an empty string will stop the loop.");
    this.variants = readStringList("Enter the macro definitions for the variants that you would like to test." +
                                       "\n Macro definitions are separated by commas." +
                                       "\ne.g. D=4,WIN32,A=3" +
                                       "\nOne line equals to one variant. " +
                                       "The program will automatically define the TEST macro." +
                                       "Entering an empty string will stop the loop");

    // Die
  }

  /**
   * Forms a list consisting of compilation args. (Flags + macros)
   * Args are like gcc -o OUTPUT INPUT.C FLAGS -D MAKROS TEST
   *
   * @return
   */
  public List<List<String>> getArgs() {
    List<String> beginning = new ArrayList<>();
    beginning.add("gcc");
    beginning.add("-o");
    beginning.add(this.output);
    List<List<String>> args = new ArrayList<>();
    for (String variant : variants) {
      List<String> arguments = new ArrayList<>();
      arguments.addAll(beginning);
      arguments.addAll(flags);
      arguments.add("-D");
      arguments.addAll(Arrays.asList(variant.split(",")));
      arguments.add("TEST");
      args.add(arguments);
    }

    return args;
  }


  private List<String> readStringList(String prompt) {
    System.out.println(prompt);
    String input;
    List<String> output = new ArrayList<>();

    while ((input = scn.next()) != null) {
      output.add(input);
    }

    return output;
  }

  public String getOutput() {
    return output;
  }

  public List<String> getMesonBuild() {
    List<String> build = new ArrayList<>(
        Arrays.asList(
            "meson x --buildtype debug --strip -Db_lto=true -Dcompile_server=false -Dbuild_server=false"
                .split(" ")));

    return build;
  }

  public List<String> getMesonCompile() {
    List<String> compile = new ArrayList<>();
    compile.add("ninja");
    compile.add("-Cx");

    return compile;
  }
}
