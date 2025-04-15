package Heursitics.Crossover;

import Heursitics.TestUtils;
import org.example.Heuristics.CrossoverHeuristics.CycleCrossover;
import org.example.Heuristics.SelectionHeuristics.ElitistSelection;
import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;

import org.example.Heuristics.SelectionHeuristics.ElitistSelection;
import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;

public class CycleCrossoverTest {

    private CycleCrossover cycleCrossover;
    private SelectionHeuristic selectionHeuristic;
    private double[][] distanceMatrix;
    private final int NUM_CITIES = 5;

    @BeforeEach
    public void setUp() {
        selectionHeuristic = new ElitistSelection();
        cycleCrossover = new CycleCrossover(selectionHeuristic, 10);
        distanceMatrix = TestUtils.createDistanceMatrix(NUM_CITIES);
    }

    @Test
    public void testGenerateOffspringProducesValidTour() {
        int[] tour1 = new int[]{0, 1, 2, 3, 4};
        int[] tour2 = new int[]{1, 2, 3, 4, 0};
        Individual parent1 = TestUtils.createIndividual(tour1, distanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour2, distanceMatrix);

        int[] offspringTour = cycleCrossover.generateOffspring(parent1, parent2);
        assertEquals(NUM_CITIES, offspringTour.length, "Offspring tour should have correct length");

        HashSet<Integer> cities = new HashSet<>();
        for (int city : offspringTour) {
            assertTrue(city >= 0 && city < NUM_CITIES, "City index should be within valid range");
            assertFalse(cities.contains(city), "Offspring tour should have no duplicates");
            cities.add(city);
        }
        assertEquals(NUM_CITIES, cities.size(), "Offspring tour should visit all cities");
    }

    @Test
    public void testGenerateOffspringPreservesCycle() {
        int[] tour1 = new int[]{0, 1, 2, 3, 4};
        int[] tour2 = new int[]{1, 2, 3, 4, 0};
        Individual parent1 = TestUtils.createIndividual(tour1, distanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour2, distanceMatrix);

        int[] offspringTour = cycleCrossover.generateOffspring(parent1, parent2);
        assertArrayEquals(new int[]{0, 1, 2, 3, 4}, offspringTour,
                "Offspring should match parent1 due to single cycle");
    }

    @Test
    public void testGenerateOffspringWithMultipleCycles() {
        int[] tour1 = new int[]{0, 1, 2, 3, 4};
        int[] tour2 = new int[]{0, 2, 1, 4, 3};
        Individual parent1 = TestUtils.createIndividual(tour1, distanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour2, distanceMatrix);

        int[] offspringTour = cycleCrossover.generateOffspring(parent1, parent2);
        int[] expected = new int[]{0, 2, 1, 4, 3};
        assertArrayEquals(expected, offspringTour,
                "Offspring should preserve cycle from parent1 and fill with parent2");
    }

    @Test
    public void testGenerateOffspringWithIdenticalParents() {
        int[] tour1 = new int[]{0, 1, 2, 3, 4};
        Individual parent1 = TestUtils.createIndividual(tour1, distanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour1.clone(), distanceMatrix);

        int[] offspringTour = cycleCrossover.generateOffspring(parent1, parent2);
        assertArrayEquals(tour1, offspringTour,
                "Offspring should match parents when they are identical");
    }

    @Test
    public void testGenerateOffspringWithSmallTour() {
        double[][] smallDistanceMatrix = TestUtils.createDistanceMatrix(1);
        int[] tour1 = new int[]{0};
        int[] tour2 = new int[]{0};
        Individual parent1 = TestUtils.createIndividual(tour1, smallDistanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour2, smallDistanceMatrix);

        int[] offspringTour = cycleCrossover.generateOffspring(parent1, parent2);
        assertArrayEquals(tour1, offspringTour,
                "Offspring should handle single-city tours correctly");
    }
}