package org.example.Heuristics.CrossoverHeuristics;



import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.*;

public class EdgeRecombinationCrossover extends CrossoverHeuristic{
    int populationSize;
    SelectionHeuristic selectionHeuristic;
    double[][] distanceMatrix;

    public EdgeRecombinationCrossover(SelectionHeuristic selectionHeuristic, int populationSize) {
        super(selectionHeuristic, populationSize);
    }


    private Map<Integer, HashSet<Integer>> buildEdgeTable(int[] parent1, int[] parent2) {
        Map<Integer, HashSet<Integer>> edgeTable = new HashMap<>();
        int numCities = parent1.length;

        for (int city : parent1) edgeTable.put(city, new HashSet<>());

        for (int i = 0; i < numCities; i++) {
            int p1Left = parent1[(i - 1 + numCities) % numCities];
            int p1Right = parent1[(i + 1) % numCities];
            int p2Left = parent2[(i - 1 + numCities) % numCities];
            int p2Right = parent2[(i + 1) % numCities];

            edgeTable.get(parent1[i]).add(p1Left);
            edgeTable.get(parent1[i]).add(p1Right);
            edgeTable.get(parent2[i]).add(p2Left);
            edgeTable.get(parent2[i]).add(p2Right);
        }

        return edgeTable;
    }

    private void removeCityFromNeighboursLists(Map<Integer, HashSet<Integer>> edgeTable, int city) {
        for (HashSet<Integer> neighbors : edgeTable.values()) {
            neighbors.remove(city);
        }
    }


    private int findBestNeighbor(HashSet<Integer> candidates, Map<Integer, HashSet<Integer>> edgeTable) {
        return candidates.stream()
                .min(Comparator.comparingInt(city -> edgeTable.get(city).size()))
                .orElseThrow();
    }

    private int findRandomUnvisitedCity(int[] offspring, int numCities) {
        HashSet<Integer> usedCities = new HashSet<>();
        for (int city : offspring) usedCities.add(city);

        for (int city = 0; city < numCities; city++) {
            if (!usedCities.contains(city)) return city;
        }
        throw new RuntimeException("No available cities left!");
    }
@Override
    int[] generateOffspring(Individual parent1, Individual parent2){
        int numCities = parent1.getNumberoOfCities();
        int[] offspringTour = new int[numCities];
        Arrays.fill(offspringTour, -1); // Mark empty spots

        // Build Edge Table
        Map<Integer, HashSet<Integer>> edgeTable = buildEdgeTable(parent1.getTour(), parent2.getTour());

        // Generate Offspring
        int currentCity = parent1.getTour()[0]; // Start from Parent 1's first city
        for (int i = 0; i < numCities; i++) {
            offspringTour[i] = currentCity;
            removeCityFromNeighboursLists(edgeTable, currentCity);
            // Pick Next City
            if (!edgeTable.get(currentCity).isEmpty()) {
                int nextCity = findBestNeighbor(edgeTable.get(currentCity), edgeTable);
                edgeTable.remove(currentCity);
                currentCity = nextCity;
            } else if (offspringTour[numCities-1] == -1){
                currentCity = findRandomUnvisitedCity(offspringTour, numCities);
            }
        }
        return offspringTour;
    }
}
