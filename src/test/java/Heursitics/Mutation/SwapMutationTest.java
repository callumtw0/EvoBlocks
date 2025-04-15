package Heursitics.Mutation;


import Heursitics.TestUtils;
import org.example.Heuristics.MutationHeuristics.SwapMutation;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class SwapMutationTest {
    private double[][] distanceMatrix;
    private final int NUM_CITIES = 5;
    private Map<String, Double> params;

    @BeforeEach
    public void setUp() {
        distanceMatrix = TestUtils.createDistanceMatrix(NUM_CITIES);
        params = new HashMap<>();
        params.put("MutationRate", 1.0);
    }

    private SwapMutation createSwapMutation() {
        return new SwapMutation(params, new Random(1));
    }

    @Test
    public void testMutationProducesValidTour() {
        SwapMutation mutation = createSwapMutation();
        int[] tour = new int[]{0, 1, 2, 3, 4};
        Individual individual = TestUtils.createIndividual(tour, distanceMatrix);

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        assertEquals(NUM_CITIES, mutatedTour.length, "Mutated tour should have correct length");

        HashSet<Integer> cities = new HashSet<>();
        for (int city : mutatedTour) {
            assertTrue(city >= 0 && city < NUM_CITIES, "City index should be within valid range");
            assertFalse(cities.contains(city), "Mutated tour should have no duplicates");
            cities.add(city);
        }
        assertEquals(NUM_CITIES, cities.size(), "Mutated tour should visit all cities");
    }

    @Test
    public void testMutationWithKnownIndices() {
        SwapMutation mutation = createSwapMutation();
        int[] tour = new int[]{0, 1, 2, 3, 4};
        Individual individual = TestUtils.createIndividual(tour, distanceMatrix);

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        // i = 1, j = 2
        int[] expected = new int[]{0, 1, 3, 2, 4};
        assertArrayEquals(expected, mutatedTour, "Mutated tour should match expected swap");
    }

    @Test
    public void testNoMutationWhenRateIsZero() {
        params.put("MutationRate", 0.0);
        SwapMutation mutation = createSwapMutation();
        int[] tour = new int[]{0, 1, 2, 3, 4};
        Individual individual = TestUtils.createIndividual(tour, distanceMatrix);

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        assertArrayEquals(tour, mutatedTour, "Tour should not change when mutation rate is 0");
    }

    @Test
    public void testMutationWithSmallTour() {
        double[][] smallDistanceMatrix = TestUtils.createDistanceMatrix(2);
        SwapMutation mutation = createSwapMutation();
        int[] tour = new int[]{0, 1};
        Individual individual = TestUtils.createIndividual(tour, smallDistanceMatrix);

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        // i = 1, j = 0
        // Swap: [1, 0]
        int[] expected = new int[]{1, 0};
        assertArrayEquals(expected, mutatedTour, "Should handle small tours correctly");
    }
}