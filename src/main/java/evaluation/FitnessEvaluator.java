package evaluation;

import mutation.Individual;

import java.util.HashMap;

/**
 * FitnessEvaluator class.
 * Manages the individuals and ranks them according to their fitness.
 * Responsible for evaluating the individuals fitness.
 */
public class FitnessEvaluator {

    /** Contains the Individuals and their respective fitness values **/
    private HashMap<Individual, Double> population;

    /**
     * Constructor for the FitnessEvaluator.
     */
    public FitnessEvaluator() {
        this.population = new HashMap<Individual, Double>();
    }


    /**
     * Will add an individual to the current population including its fitnessvalue.
     *
     * @param individual The individual to be added and evaluated.
     */
    public void addIndividual(Individual individual) {
        double fitness;

        // if cant compile set fitness = worst val
        // compile individual, if fail ->
    }
}
