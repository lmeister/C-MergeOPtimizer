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

    File properties = new File("settings.properties");
    try {
      Configuration configuration = new Configuration(properties);

      LineBasedMutator mutator = new LineBasedMutator();
      TestBasedFitnessEvaluator evaluator =
          new TestBasedFitnessEvaluator(configuration.getWeightPositiveTestCases(),
              configuration.getWeightNegativeTestCases(),
              configuration.getTimeOut());
      double fitnessGoal = evaluator.computeFitness(configuration.getAmountOfPositiveTestCases(),
          configuration.getAmountOfNegativeTestCases());


      Optimizer optimizer = new Optimizer(configuration.getMaxGenerations(),
          fitnessGoal, evaluator, mutator,
          configuration.getGenerationSize(),
          configuration.getDiffPath());

      Optional<Individual> result = optimizer.optimize();
      // If result is present write it
      if (result.isPresent()) {
        for (ManipulationInformationContainer mic : result.get().getContents()) {
          SourceUtilities.mergeMutantWithOriginal(mic);
        }
      } else {
        System.out.println("No solution found.");
      }
    } catch (IOException e) {
      System.out.println("Could not read configuration file. Will exit.");
    }
  }

}
