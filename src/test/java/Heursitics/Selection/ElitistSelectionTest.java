package Heursitics.Selection;


import Heursitics.TestUtils;
import org.example.Heuristics.SelectionHeuristics.ElitistSelection;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

public class ElitistSelectionTest {

    private ElitistSelection elitistSelection;
    private ArrayList<Individual> population;
    private final int NUM_CITIES = 5;
    private final int POPULATION_SIZE = 10;

    @BeforeEach
    public void setUp() {
        elitistSelection = new ElitistSelection();
        population = TestUtils.createTestPopulation(POPULATION_SIZE, NUM_CITIES);
    }

    @Test
    public void testRunReturnsCorrectNumberOfIndividuals() {
        int number = 3;
        ArrayList<Individual> selected = elitistSelection.run(population, number);
        assertEquals(number, selected.size(), "Selected population size should match the requested number");
    }

    @Test
    public void testRunSelectsBestIndividuals() {
        int number = 3;
        // Sort population by fitness (highest to lowest) to get expected best individuals
        ArrayList<Individual> sortedPopulation = new ArrayList<>(population);
        sortedPopulation.sort(Comparator.comparingDouble(Individual::getFitness).reversed());

        ArrayList<Individual> selected = elitistSelection.run(population, number);
        for (int i = 0; i < number; i++) {
            assertEquals(sortedPopulation.get(i), selected.get(i),
                    "Selected individual should be the " + (i + 1) + "th best");
        }
    }

    @Test
    public void testRunWithZeroNumber() {
        ArrayList<Individual> selected = elitistSelection.run(population, 0);
        assertEquals(0, selected.size(), "Selected population should be empty for number = 0");
    }

}