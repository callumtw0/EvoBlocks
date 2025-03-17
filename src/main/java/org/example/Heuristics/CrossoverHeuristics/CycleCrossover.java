package org.example.Heuristics.CrossoverHeuristics;



import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.Arrays;

public class CycleCrossover extends CrossoverHeuristic{
    int populationSize;
    SelectionHeuristic selectionHeuristic;
    double[][] distanceMatrix;

    public CycleCrossover(SelectionHeuristic selectionHeuristic, int populationSize) {
        super(selectionHeuristic, populationSize);
    }


    // Find index of a value in an array
    private int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1; // Invalid input
    }

    @Override
    int[] generateOffspring(Individual parent1, Individual parent2){
        int numCities = parent1.getNumberoOfCities();
        int[] offspringTour = new int[numCities];
        Arrays.fill(offspringTour, -1); // Mark empty spots

        boolean[] visited = new boolean[numCities]; // Track cycle
        int index = 0;
        // Identify cycles and copy values from Parent 1
        while (!visited[index]) {
            visited[index] = true;
            offspringTour[index] = parent1.getTour()[index];

            // Find corresponding index in Parent 2
            int value = parent2.getTour()[index];
            index = indexOf(parent1.getTour(), value);
        }

        // Copy remaining elements from Parent 2
        for (int i = 0; i < numCities; i++) {
            if (offspringTour[i] == -1) { // If not filled
                offspringTour[i] = parent2.getTour()[i];
            }
        }
        return offspringTour;
    }

}
