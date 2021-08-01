package main;

import evolution.Configuration;
import evolution.Optimizer;
import evolution.evaluation.TestBasedFitnessEvaluator;
import evolution.mutation.LineBasedMutator;
import evolution.population.Individual;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner scn = new Scanner(System.in);

    System.out.println("### Welcome to CMOP ###");
    System.out.println("Currently only line based mutation and test based evaluation are supported.");
    System.out.println("We first need to set the configuration.");
    System.out.println("How many positive test cases do you require to pass? (Required for fitness goal");
    int requiredPositivePasses = scn.nextInt();
    System.out.println("How many negative test cases do you require to pass? (Required for fitness goal");
    int requiredNegativePasses = scn.nextInt();
    System.out.println("After how many generations should the process abort? (Recommended: X)"); // TODO
    int maxGenerations = scn.nextInt();
    System.out.println("How big should each generation's population be? (Recommended: X)"); // TODO
    int populationSize = scn.nextInt();

    System.out.println("Enter the path to the file");
    String originalFilePath = scn.nextLine();
    System.out.println(originalFilePath);

    System.out.println("Enter");

    LineBasedMutator mutator = new LineBasedMutator();
    TestBasedFitnessEvaluator evaluator = new TestBasedFitnessEvaluator(1.0, 1.0);
    double fitnessGoal = evaluator.computeFitness(requiredPositivePasses, requiredNegativePasses);

    Optimizer optimizer = null;
    System.out.println("Enter the Path to the properties file");
    String propertiesPath = scn.nextLine();
    try {
      Configuration configuration = new Configuration(new File(propertiesPath));

      Optional<Individual> result = optimizer.optimize();

    } catch (IOException e) {
      System.out.println("Could not locate properties.");
    }


  }

}
