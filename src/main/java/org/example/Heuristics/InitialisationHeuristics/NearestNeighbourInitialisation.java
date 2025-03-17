package org.example.Heuristics.InitialisationHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class NearestNeighbourInitialisation implements InitialisationHeuristic{
    double[][] distanceMatrix;
    int numCities;

    public NearestNeighbourInitialisation(double[][] distanceMatrix, Map<String, Double> selectionparam){
        this.distanceMatrix = distanceMatrix;
        numCities = distanceMatrix.length;
    }

    @Override
    public ArrayList<Individual> run(int populationSize) {
        ArrayList<Individual> population = new ArrayList<>();

        Random rand = new Random();

        while (population.size() < populationSize) {
            int startCity = rand.nextInt(numCities); // Choose a random starting city
            int[] tour = constructNearestNeighbourTour(startCity);
            population.add(new Individual(tour, distanceMatrix));
        }

        return population;
    }

    private int[] constructNearestNeighbourTour(int startCity) {
        boolean[] visited = new boolean[numCities];
        int[] tour = new int[numCities];
        int currentCity = startCity;

        tour[0] = currentCity;
        visited[currentCity] = true;

        for (int i = 1; i < numCities; i++) {
            int nearestCity = -1;
            double shortestDistance = Double.MAX_VALUE;

            for (int j = 0; j < numCities; j++) {
                if (!visited[j] && distanceMatrix[currentCity][j] < shortestDistance) {
                    nearestCity = j;
                    shortestDistance = distanceMatrix[currentCity][j];
                }
            }

            currentCity = nearestCity;
            tour[i] = currentCity;
            visited[currentCity] = true;
        }

        return tour;
    }


}
