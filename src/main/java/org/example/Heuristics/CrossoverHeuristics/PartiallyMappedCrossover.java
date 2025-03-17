package org.example.Heuristics.CrossoverHeuristics;


import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class PartiallyMappedCrossover extends CrossoverHeuristic{
    int populationSize;
    SelectionHeuristic selectionHeuristic;
    double[][] distanceMatrix;

    public PartiallyMappedCrossover(SelectionHeuristic selectionHeuristic, int populationSize) {
        super(selectionHeuristic, populationSize);
    }


    @Override
    int[] generateOffspring(Individual parent1, Individual parent2){
        int numCities = parent1.getNumberoOfCities();
        int[] offspringTour = new int[numCities];
        Arrays.fill(offspringTour, -1); // Mark empty spots

        Random rand = new Random();
        int point1 = rand.nextInt(numCities);
        int point2 = rand.nextInt(numCities - point1) + point1;

        // Copy the segment from Parent 1
        HashMap<Integer, Integer> mapping = new HashMap<>();
        for (int i = point1; i <= point2; i++) {
            offspringTour[i] = parent1.getTour()[i];
//                mapping.put(parent2.getTour()[i], parent1.getTour()[i]);
            mapping.put(parent1.getTour()[i], parent2.getTour()[i]);
        }
        // Copy remaining elements from Parent 2, resolving conflicts
        for (int i = 0; i < numCities; i++) {
            if (offspringTour[i] == -1) { // If not filled yet
                int candidate = parent2.getTour()[i];

                // Resolve conflicts using mapping
                while (mapping.containsKey(candidate)) {
                    candidate = mapping.get(candidate);

                }

                offspringTour[i] = candidate;
            }
        }
        return offspringTour;
    }
}
