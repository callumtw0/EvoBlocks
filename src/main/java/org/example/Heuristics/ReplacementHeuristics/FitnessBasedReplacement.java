package org.example.Heuristics.ReplacementHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Comparator;

public class FitnessBasedReplacement implements ReplacementHeuristic{


    @Override
    public ArrayList<Individual> recombine(ArrayList<Individual> parents, ArrayList<Individual> offspring) {
        // Combine parents and offspring
        ArrayList<Individual> combinedPopulation = new ArrayList<>();
        combinedPopulation.addAll(parents);
        combinedPopulation.addAll(offspring);

        // Sort by fitness and keep the best individuals
        combinedPopulation.sort(Comparator.comparingDouble(Individual::getFitness).reversed());

        return new ArrayList<>(combinedPopulation.subList(0, parents.size()));
    }
}
