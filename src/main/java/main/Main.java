package main;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<String> list = List.of(
        "0: print(a)",
        "1: if (0 < 1) {",
        "2: \tprint(b)",
        "3: }",
        "4: print(c)",
        "5: command()",
        "6: exit()"
    );
  }
}
