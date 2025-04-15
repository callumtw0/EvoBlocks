package Heursitics.Replacement;

import org.example.MemeticAlgorithm.Individual;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ReplacementHeuristicTest {
    protected double[][] distanceMatrix;
    protected Map<String, Double> params;

    @BeforeEach
    public void setUp() {
        distanceMatrix = new double[][] {
                {0, 10, 1, 1},
                {10, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}
        };
        params = new HashMap<>();
    }

    protected ArrayList<Individual> createParents() {
        ArrayList<Individual> parents = new ArrayList<>();
        // Fitness values: -12, -22, -4, -3
        parents.add(new Individual(new int[]{0, 1, 2, 3}, distanceMatrix)); // Distance: 10+1+1 = 12
        parents.add(new Individual(new int[]{0, 1, 3, 2}, distanceMatrix)); // Distance: 10+1+11 = 22
        parents.add(new Individual(new int[]{0, 2, 3, 1}, distanceMatrix)); // Distance: 1+1+2 = 4
        parents.add(new Individual(new int[]{0, 3, 2, 1}, distanceMatrix)); // Distance: 1+1+1 = 3
        return parents;
    }

    protected ArrayList<Individual> createOffspring() {
        ArrayList<Individual> offspring = new ArrayList<>();
        // Fitness values: -13, -5, -3
        offspring.add(new Individual(new int[]{1, 0, 2, 3}, distanceMatrix)); // Distance: 10+1+2 = 13
        offspring.add(new Individual(new int[]{0, 2, 1, 3}, distanceMatrix)); // Distance: 1+1+3 = 5
        offspring.add(new Individual(new int[]{2, 3, 0, 1}, distanceMatrix)); // Distance: 1+1+1 = 3
        return offspring;
    }
}
