package main;

import evolution.Configuration;
import evolution.ManipulationInformationContainer;
import evolution.Optimizer;
import evolution.evaluation.TestBasedFitnessEvaluator;
import evolution.mutation.LineBasedMutator;
import evolution.population.Individual;
import util.SourceUtilities;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main {

  public static void main(String[] args) {

    System.out.println("### Welcome to CMOP ###");
    System.out.println("Currently only line based mutation and test based evaluation are supported.");

    File properties = new File("/home/leon/IdeaProjects/C-MergeOPtimizer/src/main/resources/settings.properties");
    try {
      Configuration configuration = new Configuration(properties);

      LineBasedMutator mutator = new LineBasedMutator();
      TestBasedFitnessEvaluator evaluator = new TestBasedFitnessEvaluator(configuration);

      double fitnessGoal = evaluator.computeFitness(configuration.getAmountOfPositiveTestCases(),
          configuration.getAmountOfNegativeTestCases());

      Optimizer optimizer = new Optimizer(fitnessGoal, evaluator, mutator, configuration);

      long startTime = System.currentTimeMillis();

      Optional<Individual> result = optimizer.optimize();
      long endTime = System.currentTimeMillis();
      // If result is present write it
      System.out.println("===========================================================");
      if (result.isPresent()) {
        for (ManipulationInformationContainer mic : result.get().getContents()) {
          SourceUtilities.mergeMutantWithOriginal(mic);
        }
        System.out.println("Solution found and written.");
      } else {
        System.out.println("No solution found.");
      }
      System.out.println("Time required for optimization: " + ((endTime - startTime) / 1000.0) + " Seconds.");
    } catch (IOException e) {
      System.out.println("Could not read configuration file. Will exit.");
    } catch (InterruptedException e) {
      System.out.println("Interrupted Exception");
    }
  }

}
