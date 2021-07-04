package main;

import evolution.evaluation.AbstractFitnessEvaluator;
import evolution.evaluation.TestBasedFitnessEvaluator;
import evolution.mutation.AbstractMutator;
import evolution.mutation.LineBasedMutator;

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

  // create mutator
  AbstractMutator mutator = new LineBasedMutator();
  double weightPositive = 0.5;
  double weightNegative = 1.0;
  AbstractFitnessEvaluator fitnessEvaluator = new TestBasedFitnessEvaluator(weightPositive, weightNegative);


}
