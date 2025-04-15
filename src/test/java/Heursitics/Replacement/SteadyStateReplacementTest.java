package Heursitics.Replacement;

import org.example.Heuristics.ReplacementHeuristics.SteadyStateReplacement;
import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SteadyStateReplacementTest {
    private double[][] distanceMatrix;
    private Map<String, Double> params;

    @BeforeEach
    public void setUp() {
        params = new HashMap<>();
        distanceMatrix = new double[][] {
                {0, 10, 1, 1},
                {10, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}
        };
    }

    @Test
    public void testRecombine() {
        params.put("ReplacementRate", 0.5); // Replace 2 parents
        SteadyStateReplacement replacement = new SteadyStateReplacement(params);
        ArrayList<Individual> parents = createParents();
        ArrayList<Individual> offspring = createOffspring();

        ArrayList<Individual> nextGen = replacement.recombine(parents, offspring);

        assertEquals(4, nextGen.size(), "Next generation should maintain population size");
        assertEquals(parents.size(), offspring.size(), "Offspring size should match parent size");
        // Keep 2 parents (1/13), add top 2 offspring (1/4, 1/4)
        List<Double> expectedFitness = Arrays.asList(1.0/13, 1.0/13, 1.0/4, 1.0/4);
        List<Double> actualFitness = nextGen.stream().map(Individual::getFitness).sorted().collect(Collectors.toList());
        Collections.sort(expectedFitness);
        assertEquals(expectedFitness, actualFitness, "Should replace worst parents with best offspring");
    }

    private ArrayList<Individual> createParents() {
        ArrayList<Individual> parents = new ArrayList<>();
        parents.add(new Individual(new int[]{0, 1, 2, 3}, distanceMatrix)); // Distance: 13
        parents.add(new Individual(new int[]{0, 1, 3, 2}, distanceMatrix)); // Distance: 13
        parents.add(new Individual(new int[]{0, 2, 3, 1}, distanceMatrix)); // Distance: 13
        parents.add(new Individual(new int[]{0, 3, 2, 1}, distanceMatrix)); // Distance: 13
        return parents;
    }

    private ArrayList<Individual> createOffspring() {
        ArrayList<Individual> offspring = new ArrayList<>();
        offspring.add(new Individual(new int[]{1, 0, 2, 3}, distanceMatrix)); // Distance: 13
        offspring.add(new Individual(new int[]{0, 2, 1, 3}, distanceMatrix)); // Distance: 4
        offspring.add(new Individual(new int[]{2, 3, 0, 1}, distanceMatrix)); // Distance: 13
        offspring.add(new Individual(new int[]{3, 1, 2, 0}, distanceMatrix)); // Distance: 4
        return offspring;
    }
}