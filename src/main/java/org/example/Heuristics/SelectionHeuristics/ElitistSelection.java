package org.example.Heuristics.SelectionHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Comparator;

public class ElitistSelection implements SelectionHeuristic {
    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population, int number) {
        // Sort population from best (highest fitness) to worst (lowest fitness)
        population.sort(Comparator.comparingDouble(Individual::getFitness).reversed());

        // Select the top numSelections individuals
        return new ArrayList<>(population.subList(0, number));
    }
}
