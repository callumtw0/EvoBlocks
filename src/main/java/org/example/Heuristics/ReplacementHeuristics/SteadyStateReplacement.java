package org.example.Heuristics.ReplacementHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class SteadyStateReplacement implements ReplacementHeuristic{
    private double replacementRate; // Fraction of parents to replace

    public SteadyStateReplacement(Map<String,Double> param) {
        this.replacementRate = param.get("ReplacementRate");
    }
    @Override
    public ArrayList<Individual> recombine(ArrayList<Individual> parents, ArrayList<Individual> offspring) {
        int replaceCount = (int) (parents.size() * replacementRate);

        // Sort offspring by fitness (descending)
        offspring.sort(Comparator.comparingDouble(Individual::getFitness).reversed());

        // Select the best offspring
        ArrayList<Individual> newIndividuals = new ArrayList<>(offspring.subList(0, replaceCount));

        // Replace worst individuals in parent population
        parents.sort(Comparator.comparingDouble(Individual::getFitness));
        parents.subList(0, replaceCount).clear();
        parents.addAll(newIndividuals);

        return parents;
    }
}
