package Heursitics.Initialisation;


import org.example.Heuristics.InitialisationHeuristics.NearestNeighbourInitialisation;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class NearestNeighbourInitialisationTest {

    private NearestNeighbourInitialisation nnInitialisation;
    private double[][] distanceMatrix;
    private final int NUM_CITIES = 5; // Small TSP instance for testing

    @BeforeEach
    public void setUp() {
        // Create a simple distance matrix: cities in a straight line
        // City 0: (0,0), City 1: (1,0), City 2: (2,0), ..., City 4: (4,0)
        distanceMatrix = new double[NUM_CITIES][NUM_CITIES];
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                distanceMatrix[i][j] = Math.abs(i - j); // Distance = |i - j|
            }
        }

        // Initialize NearestNeighbourInitialisation with an empty selectionparam map (unused)
        Map<String, Double> selectionparam = new HashMap<>();
        nnInitialisation = new NearestNeighbourInitialisation(distanceMatrix, selectionparam);
    }

    @Test
    public void testRunReturnsCorrectPopulationSize() {
        int populationSize = 10;
        ArrayList<Individual> population = nnInitialisation.run(populationSize);
        assertEquals(populationSize, population.size(), "Population size should match the requested size");
    }

    @Test
    public void testRunGeneratesValidTours() {
        int populationSize = 5;
        ArrayList<Individual> population = nnInitialisation.run(populationSize);

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
    public void testRunFollowsNearestNeighbourLogic() {
        int populationSize = 1;
        ArrayList<Individual> population = nnInitialisation.run(populationSize);
        Individual individual = population.get(0);
        int[] tour = individual.getTour();

        // Verify Nearest Neighbor logic: at each step, the next city should be the closest unvisited city
        boolean[] visited = new boolean[NUM_CITIES];
        visited[tour[0]] = true;

        for (int i = 0; i < tour.length - 1; i++) {
            int currentCity = tour[i];
            int nextCity = tour[i + 1];
            visited[nextCity] = true;

            // Find the closest unvisited city from currentCity
            double distanceToNext = distanceMatrix[currentCity][nextCity];
            for (int j = 0; j < NUM_CITIES; j++) {
                if (!visited[j]) {
                    double distanceToOther = distanceMatrix[currentCity][j];
                    assertTrue(distanceToNext <= distanceToOther,
                            "Next city should be the closest unvisited city");
                }
            }
        }
    }

    @Test
    public void testRunCalculatesFitnessCorrectly() {
        int populationSize = 1;
        ArrayList<Individual> population = nnInitialisation.run(populationSize);
        Individual individual = population.get(0);
        int[] tour = individual.getTour();

        // Calculate expected distance manually
        double expectedDistance = 0.0;
        for (int i = 0; i < tour.length - 1; i++) {
            expectedDistance += distanceMatrix[tour[i]][tour[i + 1]];
        }
        expectedDistance += distanceMatrix[tour[tour.length - 1]][tour[0]]; // Return to start

        double expectedFitness = 1.0 / expectedDistance;
        assertEquals(expectedDistance, individual.getDistance(), 0.001, "Distance should match expected value");
        assertEquals(expectedFitness, individual.getFitness(), 0.001, "Fitness should be 1/distance");
    }

    @Test
    public void testRunWithZeroPopulationSize() {
        ArrayList<Individual> population = nnInitialisation.run(0);
        assertEquals(0, population.size(), "Population should be empty for populationSize = 0");
    }

    @Test
    public void testRunGeneratesDifferentStartingCities() {
        int populationSize = 10;
        ArrayList<Individual> population = nnInitialisation.run(populationSize);

        // Check that at least some tours have different starting cities
        Set<Integer> startingCities = new HashSet<>();
        for (Individual individual : population) {
            startingCities.add(individual.getTour()[0]);
        }
        assertTrue(startingCities.size() > 1, "Tours should have different starting cities due to randomness");
    }
}