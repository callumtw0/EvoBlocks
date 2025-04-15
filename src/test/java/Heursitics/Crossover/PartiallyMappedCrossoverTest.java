package Heursitics.Crossover;


import Heursitics.TestUtils;
import org.example.Heuristics.CrossoverHeuristics.PartiallyMappedCrossover;
import org.example.Heuristics.SelectionHeuristics.ElitistSelection;
import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class PartiallyMappedCrossoverTest {

    private SelectionHeuristic selectionHeuristic;
    private double[][] distanceMatrix;
    private final int NUM_CITIES = 5;

    @BeforeEach
    public void setUp() {
        selectionHeuristic = new ElitistSelection();
        distanceMatrix = TestUtils.createDistanceMatrix(NUM_CITIES);
    }

    private PartiallyMappedCrossover createPmxCrossover() {
        return new PartiallyMappedCrossover(selectionHeuristic, 10, new Random(12345));
    }

    @Test
    public void testGenerateOffspringProducesValidTour() {
        PartiallyMappedCrossover pmxCrossover = createPmxCrossover();
        int[] tour1 = new int[]{0, 1, 2, 3, 4};
        int[] tour2 = new int[]{4, 3, 2, 1, 0};
        Individual parent1 = TestUtils.createIndividual(tour1, distanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour2, distanceMatrix);

        int[] offspringTour = pmxCrossover.generateOffspring(parent1, parent2);
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
    public void testGenerateOffspringAppliesMapping() {
        PartiallyMappedCrossover pmxCrossover = createPmxCrossover();
        int[] tour1 = new int[]{0, 1, 2, 3, 4};
        int[] tour2 = new int[]{4, 3, 2, 1, 0};
        Individual parent1 = TestUtils.createIndividual(tour1, distanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour2, distanceMatrix);

        int[] offspringTour = pmxCrossover.generateOffspring(parent1, parent2);
        // With seed 12345, point1 = 2, point2 = 4
        // Copy [2, 3, 4] -> [-1, -1, 2, 3, 4]
        // Mapping: 2 -> 2, 3 -> 1, 4 -> 0
        // Index 0: parent2[0] = 4 -> 0, Index 1: parent2[1] = 3 -> 1
        int[] expected = new int[]{0, 1, 2, 3, 4};
        assertArrayEquals(expected, offspringTour,
                "Offspring should apply PMX mapping correctly");
    }

    @Test
    public void testGenerateOffspringWithIdenticalParents() {
        PartiallyMappedCrossover pmxCrossover = createPmxCrossover();
        int[] tour1 = new int[]{0, 1, 2, 3, 4};
        Individual parent1 = TestUtils.createIndividual(tour1, distanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour1.clone(), distanceMatrix);

        int[] offspringTour = pmxCrossover.generateOffspring(parent1, parent2);
        assertArrayEquals(tour1, offspringTour,
                "Offspring should match parents when they are identical");
    }

    @Test
    public void testGenerateOffspringWithSmallTour() {
        double[][] smallDistanceMatrix = TestUtils.createDistanceMatrix(1);
        PartiallyMappedCrossover pmxCrossover = createPmxCrossover();
        int[] tour1 = new int[]{0};
        int[] tour2 = new int[]{0};
        Individual parent1 = TestUtils.createIndividual(tour1, smallDistanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour2, smallDistanceMatrix);

        int[] offspringTour = pmxCrossover.generateOffspring(parent1, parent2);
        assertArrayEquals(tour1, offspringTour,
                "Offspring should handle single-city tours correctly");
    }

    @Test
    public void testGenerateOffspringPreservesSomeCitiesFromParent1() {
        PartiallyMappedCrossover pmxCrossover = createPmxCrossover();
        int[] tour1 = new int[]{0, 1, 2, 3, 4};
        int[] tour2 = new int[]{4, 3, 2, 1, 0};
        Individual parent1 = TestUtils.createIndividual(tour1, distanceMatrix);
        Individual parent2 = TestUtils.createIndividual(tour2, distanceMatrix);

        int[] offspringTour = pmxCrossover.generateOffspring(parent1, parent2);
        boolean preservesSomePosition = false;
        for (int i = 0; i < NUM_CITIES; i++) {
            if (offspringTour[i] == tour1[i]) {
                preservesSomePosition = true;
                break;
            }
        }
        assertTrue(preservesSomePosition, "Offspring should preserve some cities in the same position as parent1");
    }
}