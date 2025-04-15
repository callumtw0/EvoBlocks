package Heursitics.Mutation;

import Heursitics.TestUtils;
import org.example.Heuristics.MutationHeuristics.DisplacementMutation;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class DisplacementMutationTest {
    private double[][] distanceMatrix;
    private final int NUM_CITIES = 5;
    private Map<String, Double> params;

    @BeforeEach
    public void setUp() {
        distanceMatrix = TestUtils.createDistanceMatrix(NUM_CITIES);
        params = new HashMap<>();
        params.put("MutationRate", 1.0); // Force mutation to occur
    }

    private DisplacementMutation createDisplacementMutation() {
        return new DisplacementMutation(params, new Random(12345));
    }

    @Test
    public void testMutationProducesValidTour() {
        DisplacementMutation mutation = createDisplacementMutation();
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
    public void testMutationWithKnownSegmentAndInsertPosition() {
        DisplacementMutation mutation = createDisplacementMutation();
        int[] tour = new int[]{0, 1, 2, 3, 4};
        Individual individual = TestUtils.createIndividual(tour, distanceMatrix);

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        // Random(12345):
        // rand.nextFloat() = 0.723 (less than 1.0, so mutation occurs)
        // i = rand.nextInt(5) = 1
        // j = rand.nextInt(5) = 2
        // Segment [1, 2]
        // Remaining tour: [0, 3, 4]
        // segmentLength = 2
        // insertPos = rand.nextInt(5 - 2) = rand.nextInt(3) = 1
        // Insert [1, 2] at position 1: [0, 1, 2, 3, 4]
        int[] expected = new int[]{0, 1, 2, 3, 4};
        assertArrayEquals(expected, mutatedTour, "Mutated tour should match expected displacement");
    }

    @Test
    public void testNoMutationWhenRateIsZero() {
        params.put("MutationRate", 0.0);
        DisplacementMutation mutation = createDisplacementMutation();
        int[] tour = new int[]{0, 1, 2, 3, 4};
        Individual individual = TestUtils.createIndividual(tour, distanceMatrix);

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        assertArrayEquals(tour, mutatedTour, "Tour should not change when mutation rate is 0");
    }

    @Test
    public void testMutationWithSmallTour() {
        double[][] smallDistanceMatrix = TestUtils.createDistanceMatrix(2);
        DisplacementMutation mutation = createDisplacementMutation();
        int[] tour = new int[]{0, 1};
        Individual individual = TestUtils.createIndividual(tour, smallDistanceMatrix);

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        // numCities = 2
        // i = rand.nextInt(2) = 1 (already computed above)
        // j = rand.nextInt(2) = 0
        // Swap i and j: i = 0, j = 1
        // Segment [0, 1]
        // Remaining tour: []
        // segmentLength = 2
        // numCities <= segmentLength, so insertPos = 0
        // New tour: [0, 1]
        int[] expected = new int[]{0, 1};
        assertArrayEquals(expected, mutatedTour, "Should handle small tours correctly");
    }
}