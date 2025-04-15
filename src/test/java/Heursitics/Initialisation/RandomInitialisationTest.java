package Heursitics.Initialisation;

import org.example.Heuristics.InitialisationHeuristics.RandomInitialisation;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class RandomInitialisationTest {

    private RandomInitialisation randomInitialisation;
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

        // Initialize RandomInitialisation with an empty selectionparam map (unused)
        Map<String, Double> selectionparam = new HashMap<>();
        randomInitialisation = new RandomInitialisation(distanceMatrix, selectionparam);
    }

    @Test
    public void testRunReturnsCorrectPopulationSize() {
        int populationSize = 10;
        ArrayList<Individual> population = randomInitialisation.run(populationSize);
        assertEquals(populationSize, population.size(), "Population size should match the requested size");
    }

    @Test
    public void testRunGeneratesValidTours() {
        int populationSize = 5;
        ArrayList<Individual> population = randomInitialisation.run(populationSize);

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
    public void testRunGeneratesRandomTours() {
        int populationSize = 10;
        ArrayList<Individual> population1 = randomInitialisation.run(populationSize);
        ArrayList<Individual> population2 = randomInitialisation.run(populationSize);

        // Check that at least some tours differ between the two populations
        boolean toursDiffer = false;
        for (int i = 0; i < populationSize; i++) {
            int[] tour1 = population1.get(i).getTour();
            int[] tour2 = population2.get(i).getTour();
            if (!Arrays.equals(tour1, tour2)) {
                toursDiffer = true;
                break;
            }
        }
        assertTrue(toursDiffer, "Tours should be randomly shuffled and differ between runs");
    }

    @Test
    public void testRunCalculatesFitnessCorrectly() {
        int populationSize = 1;
        ArrayList<Individual> population = randomInitialisation.run(populationSize);
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
    public void testShuffleArrayProducesValidPermutation() {
        int[] array = new int[NUM_CITIES];
        for (int i = 0; i < NUM_CITIES; i++) {
            array[i] = i;
        }

        // Create a new instance to access shuffleArray (since it's private)
        Map<String, Double> selectionparam = new HashMap<>();
        RandomInitialisation testInstance = new RandomInitialisation(distanceMatrix, selectionparam);

        // Use reflection to access the private shuffleArray method
        // Alternatively, we could modify the method to be package-private for testing
        // For simplicity, let's assume we run the shuffle multiple times via run()
        ArrayList<Individual> population = testInstance.run(1);
        int[] shuffledTour = population.get(0).getTour();

        // Check that the shuffled tour is a permutation
        Set<Integer> cities = new HashSet<>();
        for (int city : shuffledTour) {
            assertTrue(city >= 0 && city < NUM_CITIES, "City index should be within valid range");
            cities.add(city);
        }
        assertEquals(NUM_CITIES, cities.size(), "Shuffled tour should contain all cities exactly once");

        // Check randomness by running multiple shuffles
        Set<String> tourSignatures = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            population = testInstance.run(1);
            int[] tour = population.get(0).getTour();
            tourSignatures.add(Arrays.toString(tour));
        }
        assertTrue(tourSignatures.size() > 1, "Multiple shuffles should produce different tours");
    }

    @Test
    public void testRunWithZeroPopulationSize() {
        ArrayList<Individual> population = randomInitialisation.run(0);
        assertEquals(0, population.size(), "Population should be empty for populationSize = 0");
    }
}