package main;

import mutation.Individual;
import mutation.Mutator;

import java.util.ArrayList;
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

    Individual individual = new Individual(list);
    Mutator mutator = new Mutator();

    Individual mutation1 = mutator.swapRandomLines(individual);
    System.out.println("Original:");
    individual.printContents();

    /*
    System.out.println("\nTest random swap of lines");
    System.out.println("Mutation 1:");
    mutation1.printContents();

    System.out.println("\nTest block swap");
    System.out.println("Mutation 2:");
    Individual mutation2 = mutator.swapBlock(individual, 1, 3, 4, 5);
    mutation2.printContents();

    Individual mutation3 = mutator.swapLines(individual, 3, 4);
    System.out.println("\nSwap lines:");
    mutation3.printContents();
    */
    Individual mutation4 = mutator.deleteRandomLine(individual);
    System.out.println("\nDelete random line:");
    mutation4.printContents();

    for (int i = 0; i < 4; i++) {
      mutation4 = mutator.swapRandomLines(mutation4);
    }

    System.out.println("\nLots of swaps:");
    mutation4.printContents();

    System.out.println("\nmutation1:");
    mutation1.printContents();

    for (int i = 0; i < 5; i++) {
      Individual mutation5 = mutator.crossover(mutation4, mutation1);
      System.out.println("\nCrossover " + i);
      mutation5.printContents();
    }
  }
}
