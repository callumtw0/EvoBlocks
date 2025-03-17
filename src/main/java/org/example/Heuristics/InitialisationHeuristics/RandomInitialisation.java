package org.example.Heuristics.InitialisationHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class RandomInitialisation implements InitialisationHeuristic{
    double[][] distanceMatrix;
    int numCities;

    public RandomInitialisation(double[][] distanceMatrix, Map<String, Double> selectionparam){
        this.distanceMatrix = distanceMatrix;
        numCities = distanceMatrix.length;
    }

    @Override
    public ArrayList<Individual> run(int populationSize) {
        int numCities = distanceMatrix.length;
        ArrayList<Individual> population = new ArrayList<>();

        int[] tour = new int[numCities];
        for (int i = 0; i < numCities; i++) {
            tour[i] = i;
        }

        for (int i = 0; i < populationSize; i++){
            shuffleArray(tour);
            population.add(new Individual(tour, distanceMatrix));
        }

        return population;
    }

    private void shuffleArray(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            // Pick a random index from 0 to i
            int j = rand.nextInt(i + 1);

            // Swap elements at i and j
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
