package org.example.Heuristics.LocalSearchHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class twoOptLocalSearch implements LocalSearchHeuristic{
    public double searchRate;
    public twoOptLocalSearch(Map<String,Double> param ) {searchRate = param.get("LocalSearchProbability");}


    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population) {
        for(Individual individual : population) {
            run(individual);

        }
        return population;
    }

    @Override
    public Individual run(Individual individual) {
        Random random = new Random(42); // Use a fixed seed for testing
        if (random.nextDouble() > searchRate) return individual;
        int[] tour = individual.getTour();
        double[][] distanceMatrix = individual.getDistanceMatrix();
        boolean improved = true;

        while (improved){
            improved = false;
            for (int i = 1; i < tour.length - 2; i++) {
                for (int j = i + 1; j < tour.length; j++) {
                    double oldFitness = individual.getFitness(); // Get current fitness

                    // Apply the 2-opt swap
                    reverseSegment(tour, i, j);
                    individual.setTour(tour); // Updates fitness

                    // If no improvement, undo the change
                    if (individual.getFitness() <= oldFitness) {
                        reverseSegment(tour, i, j); // Revert swap
                        individual.setTour(tour); // Updates fitness
                    } else {
                        improved = true;
                    }
                }
            }
        }
        individual.setTour(tour);
        return individual;
    }
    private void reverseSegment(int[] tour, int i, int j) {
        while (i < j) {
            int temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;
            i++;
            j--;
        }
    }
}
