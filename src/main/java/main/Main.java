package main;

import evolution.evaluation.AbstractFitnessEvaluator;
import evolution.evaluation.TestBasedFitnessEvaluator;
import evolution.mutation.AbstractMutator;
import evolution.mutation.LineBasedMutator;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner scn = new Scanner(System.in);

    System.out.println("### Welcome to CMOP ###");
    System.out.println("Currently only line based mutation and test based evaluation are supported.");
    System.out.println("We first need to set the configuration.");
    System.out.println("How many test cases do you require to pass? (Required for fitness goal");
    int requiredPasses = scn.nextInt();
    System.out.println("After how many generations should the process abort? (Recommended: X)"); // TODO
    int maxGenerations = scn.nextInt();
    System.out.println("How big should each generation's population be? (Recommended: X)"); // TODO
    int populationSize = scn.nextInt();

    System.out.println("Enter the path to the file");
    String originalFilePath = scn.nextLine();
    System.out.println(originalFilePath);

    AbstractMutator mutator = new LineBasedMutator();
    AbstractFitnessEvaluator evaluator = new TestBasedFitnessEvaluator(1.0, 1.0);

  }

}
