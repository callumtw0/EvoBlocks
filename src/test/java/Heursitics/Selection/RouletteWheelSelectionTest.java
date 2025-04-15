package Heursitics.Selection;

import Heursitics.TestUtils;
import org.example.Heuristics.SelectionHeuristics.RouletteWheelSelection;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class RouletteWheelSelectionTest {

    private RouletteWheelSelection rouletteWheelSelection;
    private ArrayList<Individual> population;
    private final int NUM_CITIES = 10;
    private final int POPULATION_SIZE = 50;

    @BeforeEach
    public void setUp() {
        rouletteWheelSelection = new RouletteWheelSelection();
        population = TestUtils.createTestPopulation(POPULATION_SIZE, NUM_CITIES);
    }

    @Test
    public void testRunReturnsCorrectNumberOfIndividuals() {
        int number = 3;
        ArrayList<Individual> selected = rouletteWheelSelection.run(population, number);
        assertEquals(number, selected.size(), "Selected population size should match the requested number");
    }

    @Test
    public void testRunPrefersHigherFitnessIndividuals() {
        int number = 5;
        // Sort population by fitness (highest to lowest)
        ArrayList<Individual> sortedPopulation = new ArrayList<>(population);
        sortedPopulation.sort(Comparator.comparingDouble(Individual::getFitness).reversed());

        // Run selection multiple times to estimate selection probabilities
        Map<Individual, Integer> selectionCounts = new HashMap<>();
        int numTrials = 1000;
        for (int trial = 0; trial < numTrials; trial++) {
            ArrayList<Individual> selected = rouletteWheelSelection.run(population, number);
            for (Individual individual : selected) {
                selectionCounts.put(individual, selectionCounts.getOrDefault(individual, 0) + 1);
            }
        }

        // Check that higher-fitness individuals are selected more often
        for (int i = 0; i < POPULATION_SIZE / 2; i++) {
            Individual better = sortedPopulation.get(i);
            Individual worse = sortedPopulation.get(POPULATION_SIZE - 1 - i);
            int betterCount = selectionCounts.getOrDefault(better, 0);
            int worseCount = selectionCounts.getOrDefault(worse, 0);
            assertTrue(betterCount >= worseCount,
                    "Higher-fitness individual should be selected at least as often as lower-fitness");
        }
    }

    @Test
    public void testRunWithZeroNumber() {
        ArrayList<Individual> selected = rouletteWheelSelection.run(population, 0);
        assertEquals(0, selected.size(), "Selected population should be empty for number = 0");
    }

}