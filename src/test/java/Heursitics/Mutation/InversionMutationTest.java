package Heursitics.Mutation;


import Heursitics.TestUtils;
import org.example.Heuristics.MutationHeuristics.InversionMutation;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class InversionMutationTest {
    private double[][] distanceMatrix;
    private final int NUM_CITIES = 5;
    private Map<String, Double> params;

    @BeforeEach
    public void setUp() {
        distanceMatrix = TestUtils.createDistanceMatrix(NUM_CITIES);
        params = new HashMap<>();
        params.put("MutationRate", 1.0);
    }

    private InversionMutation createInversionMutation() {
        return new InversionMutation(params, new Random(1));
    }

    @Test
    public void testMutationProducesValidTour() {
        InversionMutation mutation = createInversionMutation();
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
    public void testMutationReversesSegment() {
        InversionMutation mutation = createInversionMutation();
        int[] tour = new int[]{0, 1, 2, 3, 4};
        int[] originalTour = tour.clone();
        Individual individual = TestUtils.createIndividual(tour, distanceMatrix);

        // Compute the indices that InversionMutation will use
        Random rand = new Random(1);
        rand.nextFloat(); // 0.7308782 (matches the check in InversionMutation)
        int i = rand.nextInt(5); // 3
        int j = rand.nextInt(5); // 2
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        } // i = 2, j = 3

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        // Check the segment [i, j] is reversed
        for (int k = i; k <= j; k++) {
            assertEquals(originalTour[j - (k - i)], mutatedTour[k], "Segment should be reversed");
        }
        // Check positions before i and after j are unchanged
        for (int k = 0; k < i; k++) {
            assertEquals(originalTour[k], mutatedTour[k], "Positions before i should be unchanged");
        }
        for (int k = j + 1; k < NUM_CITIES; k++) {
            assertEquals(originalTour[k], mutatedTour[k], "Positions after j should be unchanged");
        }
    }

    @Test
    public void testNoMutationWhenRateIsZero() {
        params.put("MutationRate", 0.0);
        InversionMutation mutation = createInversionMutation();
        int[] tour = new int[]{0, 1, 2, 3, 4};
        Individual individual = TestUtils.createIndividual(tour, distanceMatrix);

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        assertArrayEquals(tour, mutatedTour, "Tour should not change when mutation rate is 0");
    }

    @Test
    public void testMutationWithSmallTour() {
        double[][] smallDistanceMatrix = TestUtils.createDistanceMatrix(2);
        InversionMutation mutation = createInversionMutation();
        int[] tour = new int[]{0, 1};
        Individual individual = TestUtils.createIndividual(tour, smallDistanceMatrix);

        mutation.run(individual);
        int[] mutatedTour = individual.getTour();

        // i = 1, j = 0, swap to i = 0, j = 1
        // Segment [0, 1] -> [1, 0]
        int[] expected = new int[]{1, 0};
        assertArrayEquals(expected, mutatedTour, "Should handle small tours correctly");
    }
}