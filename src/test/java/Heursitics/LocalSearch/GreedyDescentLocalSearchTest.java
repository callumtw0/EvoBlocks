package Heursitics.LocalSearch;

import Heursitics.TestUtils;
import org.example.Heuristics.LocalSearchHeuristics.GreedyDescentLocalSearch;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class GreedyDescentLocalSearchTest {
    private Map<String, Double> params;
    private double[][] distanceMatrix;

    @BeforeEach
    public void setUp() {
        params = new HashMap<>();
        params.put("LocalSearchProbability", 1.0);
        distanceMatrix = new double[][] {
                {0, 10, 1, 1},
                {10, 0, 1, 5},
                {1, 1, 0, 1},
                {1, 5, 1, 0}
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
        GreedyDescentLocalSearch localSearch = new GreedyDescentLocalSearch(params);
        int[] tour = new int[]{0, 1, 2, 3};
        Individual individual = new Individual(tour, distanceMatrix);

        double initialDistance = calculateDistance(tour, distanceMatrix);
        localSearch.run(individual);
        int[] newTour = individual.getTour();

        // Debug: Print the tour
        System.out.println("Actual tour: " + Arrays.toString(newTour));

        // Best achievable distance with current strategy is 7 (e.g., [3, 1, 2, 0])
        double bestAchievableDistance = 7.0;
        double newDistance = calculateDistance(newTour, distanceMatrix);

        assertEquals(bestAchievableDistance, newDistance, 0.001, "Tour should achieve best possible distance with this heuristic");
        assertTrue(newDistance < initialDistance, "Distance should improve");
    }

    @Test
    public void testSmallTour() {
        double[][] smallDistanceMatrix = TestUtils.createDistanceMatrix(2);
        GreedyDescentLocalSearch localSearch = new GreedyDescentLocalSearch(params);
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
        GreedyDescentLocalSearch localSearch = new GreedyDescentLocalSearch(params);
        int[] tour = new int[]{0, 1, 2, 3};
        Individual individual = new Individual(tour, distanceMatrix);

        double initialFitness = individual.getFitness();
        localSearch.run(individual);
        int[] newTour = individual.getTour();

        assertArrayEquals(new int[]{0, 1, 2, 3}, newTour, "Tour should not change with searchRate = 0");
        assertEquals(initialFitness, individual.getFitness(), "Fitness should not change");
    }
}