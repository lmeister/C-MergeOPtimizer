package evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompilerArguments {
  private final Scanner scn = new Scanner(System.in);
  private String source;
  private List<String> macros;
  private List<String> flags;

  public CompilerArguments() {
    this.source = readSource();
    this.flags = readStringList("Enter your flags including the dash. Entering an empty string will stop the loop.");
  }

  private String readSource() {
    System.out.println("Enter the path to the source:");
    return this.scn.nextLine();
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

}
