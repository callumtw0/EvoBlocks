package Heursitics.Initialisation;

import org.example.Heuristics.InitialisationHeuristics.HybridInitialisation;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HybridInitialisationTest {

    private HybridInitialisation hybridInitialisation;
    private double[][] distanceMatrix;
    private final int NUM_CITIES = 10; // Small TSP instance for testing
    private Map<String, Double> selectionparam;

    @BeforeEach
    public void setUp() {
        // Create a simple distance matrix: cities in a straight line
        distanceMatrix = new double[NUM_CITIES][NUM_CITIES];
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                distanceMatrix[i][j] = Math.abs(i - j); // Distance = |i - j|
            }
        }

        // Initialize selectionparam with hybridRatio
        selectionparam = new HashMap<>();
        selectionparam.put("HybridRatio", 0.3); // 30% Nearest Neighbor, 70% Random
        hybridInitialisation = new HybridInitialisation(distanceMatrix, selectionparam);
    }

    @Test
    public void testRunReturnsCorrectPopulationSize() {
        int populationSize = 10;
        ArrayList<Individual> population = hybridInitialisation.run(populationSize);
        assertEquals(populationSize, population.size(), "Population size should match the requested size");
    }

    @Test
    public void testRunAppliesHybridRatioCorrectly() {
        int populationSize = 100;
        ArrayList<Individual> population = hybridInitialisation.run(populationSize);

        // Hybrid ratio of 0.3: 30% Nearest Neighbor, 70% Random
        int expectedNNCount = (int) (populationSize * 0.3);
        int expectedRandomCount = populationSize - expectedNNCount;

        // Identify Nearest Neighbor tours by checking if they follow the logic
        int nnCount = 0;
        for (Individual individual : population) {
            if (isNearestNeighbourTour(individual.getTour())) {
                nnCount++;
            }
        }

        assertEquals(expectedNNCount, nnCount, "Number of Nearest Neighbor tours should match hybrid ratio (30%)");
        assertEquals(expectedRandomCount, populationSize - nnCount, "Number of random tours should match (70%)");
    }

    @Test
    public void testRunGeneratesValidTours() {
        int populationSize = 5;
        ArrayList<Individual> population = hybridInitialisation.run(populationSize);

        for (Individual individual : population) {
            int[] tour = individual.getTour();
            assertEquals(NUM_CITIES, tour.length, "Tour should have correct number of cities");
            assertEquals(NUM_CITIES, individual.getNumberOfCities(), "Number of cities should match tour length");

            // Check for duplicates and ensure all cities are visited
            Set<Integer> visitedCities = new HashSet<>();
            for (int city : tour) {
                assertTrue(city >= 0 && city < NUM_CITIES, "City index should be within valid range");
                assertFalse(visitedCities.contains(city), "Tour should not have duplicate cities");
                visitedCities.add(city);
            }
            assertEquals(NUM_CITIES, visitedCities.size(), "Tour should visit all cities");
        }
    }

    @Test
    public void testRunWithExtremeHybridRatios() {
        // Test hybridRatio = 0.0 (fully random)
        Map<String, Double> paramsZero = new HashMap<>();
        paramsZero.put("HybridRatio", 0.0);
        HybridInitialisation fullyRandom = new HybridInitialisation(distanceMatrix, paramsZero);
        ArrayList<Individual> populationRandom = fullyRandom.run(10);
        assertEquals(10, populationRandom.size(), "Population size should be 10");
        for (Individual individual : populationRandom) {
            assertFalse(isNearestNeighbourTour(individual.getTour()), "All tours should be random (not Nearest Neighbor)");
        }

        // Test hybridRatio = 1.0 (fully Nearest Neighbor)
        Map<String, Double> paramsOne = new HashMap<>();
        paramsOne.put("HybridRatio", 1.0);
        HybridInitialisation fullyNN = new HybridInitialisation(distanceMatrix, paramsOne);
        ArrayList<Individual> populationNN = fullyNN.run(10);
        assertEquals(10, populationNN.size(), "Population size should be 10");
        for (Individual individual : populationNN) {
            assertTrue(isNearestNeighbourTour(individual.getTour()), "All tours should follow Nearest Neighbor logic");
        }
    }

    @Test
    public void testRunWithZeroPopulationSize() {
        ArrayList<Individual> population = hybridInitialisation.run(0);
        assertEquals(0, population.size(), "Population should be empty for populationSize = 0");
    }

    // Helper method to check if a tour follows Nearest Neighbor logic
    private boolean isNearestNeighbourTour(int[] tour) {
        boolean[] visited = new boolean[NUM_CITIES];
        visited[tour[0]] = true;

        for (int i = 0; i < tour.length - 1; i++) {
            int currentCity = tour[i];
            int nextCity = tour[i + 1];
            visited[nextCity] = true;

            double distanceToNext = distanceMatrix[currentCity][nextCity];
            for (int j = 0; j < NUM_CITIES; j++) {
                if (!visited[j]) {
                    double distanceToOther = distanceMatrix[currentCity][j];
                    if (distanceToOther < distanceToNext) {
                        return false; // Found a closer unvisited city
                    }
                }
            }
        }
        return true;
    }
}
