package Heursitics.LocalSearch;

import Heursitics.TestUtils;
import org.example.Heuristics.LocalSearchHeuristics.twoOptLocalSearch;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class twoOptLocalSearchTest {
    private Map<String, Double> params;
    private double[][] distanceMatrix;

    @BeforeEach
    public void setUp() {
        params = new HashMap<>();
        params.put("LocalSearchProbability", 1.0);
        distanceMatrix = new double[][] {
                {0, 10, 1, 1},
                {10, 0, 1, 1},
                {1, 1, 0, 10},
                {1, 1, 10, 0}
        };
    }

    private double calculateDistance(int[] tour, double[][] distanceMatrix) {
        double distance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            distance += distanceMatrix[tour[i]][tour[i + 1]];
        }
        return distance;
    }

    @Test
    public void testImprovesTour() {
        twoOptLocalSearch localSearch = new twoOptLocalSearch(params);
        int[] tour = new int[]{0, 1, 2, 3};
        Individual individual = new Individual(tour, distanceMatrix);

        double initialDistance = calculateDistance(tour, distanceMatrix); // 21
        localSearch.run(individual);
        int[] newTour = individual.getTour();

        // Best tour distance is 3 (e.g., [0, 2, 1, 3] or [0, 3, 1, 2])
        double optimalDistance = 3.0;
        double newDistance = calculateDistance(newTour, distanceMatrix);

        // Currently assumes fitness = -distance (higher is better)
        // If fitness = 1/distance, the logic (newFitness > oldFitness) still holds
        assertEquals(optimalDistance, newDistance, 0.001, "Tour should achieve optimal distance");
        assertTrue(newDistance < initialDistance, "Distance should improve");
    }

    @Test
    public void testSmallTour() {
        double[][] smallDistanceMatrix = TestUtils.createDistanceMatrix(2);
        twoOptLocalSearch localSearch = new twoOptLocalSearch(params);
        int[] tour = new int[]{0, 1};
        Individual individual = new Individual(tour, smallDistanceMatrix);

        double initialFitness = individual.getFitness();
        localSearch.run(individual);
        int[] newTour = individual.getTour();

        assertArrayEquals(new int[]{0, 1}, newTour, "Small tour should not change");
        assertEquals(initialFitness, individual.getFitness(), "Fitness should not change");
    }

    @Test
    public void testSearchRateZero() {
        params.put("LocalSearchProbability", 0.0);
        twoOptLocalSearch localSearch = new twoOptLocalSearch(params);
        int[] tour = new int[]{0, 1, 2, 3};
        Individual individual = new Individual(tour, distanceMatrix);

        double initialFitness = individual.getFitness();
        localSearch.run(individual);
        int[] newTour = individual.getTour();

        assertArrayEquals(new int[]{0, 1, 2, 3}, newTour, "Tour should not change with searchRate = 0");
        assertEquals(initialFitness, individual.getFitness(), "Fitness should not change");
    }
}