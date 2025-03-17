package org.example.Heuristics.CrossoverHeuristics;



import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class OrderCrossover extends CrossoverHeuristic{
    int populationSize;
    SelectionHeuristic selectionHeuristic;
    double[][] distanceMatrix;

    public OrderCrossover(SelectionHeuristic selectionHeuristic, int populationSize) {
        super(selectionHeuristic, populationSize);
    }

    @Override
    int[] generateOffspring(Individual parent1, Individual parent2){
        int numCities = parent1.getNumberoOfCities();
        int[] offspringTour = new int[numCities];
        Arrays.fill(offspringTour, -1); // Mark empty spots

        Random random = new Random();
        int point1 = random.nextInt(numCities);
        int point2 = random.nextInt(numCities - point1) + point1;

        // Copy the crossover segment from Parent 1
        HashSet<Integer> usedCities = new HashSet<>();
        for (int i = point1; i <= point2; i++) {
            offspringTour[i] = parent1.getTour()[i];
            usedCities.add(parent1.getTour()[i]);
        }

        // Fill remaining elements from Parent 2, maintaining order
        int index = (point2 + 1) % numCities;
        for (int i = 0; i < numCities; i++) {
            int city = parent2.getTour()[i];
            if (!usedCities.contains(city)) {
                offspringTour[index] = city;
                usedCities.add(city);
                index = (index + 1) % numCities; // Wrap around if needed
            }
        }
        return offspringTour;
    }
}
