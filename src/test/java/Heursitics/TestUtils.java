package Heursitics;

import org.example.MemeticAlgorithm.Individual;
import java.util.ArrayList;

public class TestUtils {
    public static ArrayList<Individual> createTestPopulation(int size, int numCities) {
        double[][] distanceMatrix = new double[numCities][numCities];
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                distanceMatrix[i][j] = Math.abs(i - j);
            }
        }

        ArrayList<Individual> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int[] tour = new int[numCities];
            if (i < size / 2) {
                // First half: "good" tours with low distance (e.g., [0, 1, 2, 3, 4])
                for (int j = 0; j < numCities; j++) {
                    tour[j] = j;
                }
            } else {
                // Second half: "bad" tours with higher distance (e.g., [0, 4, 1, 3, 2])
                tour[0] = 0;
                tour[1] = numCities - 1; // Jump to farthest city
                tour[2] = 1;
                for (int j = 3; j < numCities; j++) {
                    tour[j] = j - 1; // Alternate between nearby cities
                }
            }
            population.add(new Individual(tour, distanceMatrix));
        }
        return population;
    }
    public static double[][] createDistanceMatrix(int numCities) {
        double[][] distanceMatrix = new double[numCities][numCities];
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                distanceMatrix[i][j] = Math.abs(i - j);
            }
        }
        return distanceMatrix;
    }

    public static Individual createIndividual(int[] tour, double[][] distanceMatrix) {
        return new Individual(tour, distanceMatrix);
    }
}