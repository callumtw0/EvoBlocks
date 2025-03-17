package org.example.Heuristics.ReplacementHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class ElitistReplacement implements ReplacementHeuristic {
    double elitismRate;

    public ElitistReplacement(Map<String,Double> param) {
        this.elitismRate = param.get("ElitismRate");

    }

    @Override
    public ArrayList<Individual> recombine(ArrayList<Individual> parents, ArrayList<Individual> offspring) {
        int elitismCount = (int) (parents.size() * elitismRate);

        // Sort parents by fitness (descending)
        parents.sort(Comparator.comparingDouble(Individual::getFitness).reversed());

        // Select the top N elites
        ArrayList<Individual> nextGeneration = new ArrayList<>(parents.subList(0, elitismCount));

        // Fill remaining spots with best offspring
        offspring.sort(Comparator.comparingDouble(Individual::getFitness).reversed());
        nextGeneration.addAll(offspring.subList(0, parents.size() - elitismCount));

        return nextGeneration;
    }
}
