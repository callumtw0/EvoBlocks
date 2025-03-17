package org.example.Heuristics.SelectionHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Random;

public class StochasticUniversalSampling implements SelectionHeuristic{
    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population, int number) {
        ArrayList<Individual> selected = new ArrayList<>();
        double totalFitness = population.stream().mapToDouble(Individual::getFitness).sum();
        // Generate evenly spaced selection points
        double spacing = totalFitness / number;
        double start = new Random().nextDouble() * spacing;
        double[] pointers = new double[number];
        for (int i = 0; i < number; i++) {
            pointers[i] = start + i * spacing;
        }

        // Perform selection
        int index = 0;
        double cumulativeFitness = population.get(0).getFitness();
        for (double pointer : pointers) {
            while (cumulativeFitness < pointer) {
                index++;
                cumulativeFitness += population.get(index).getFitness();
            }
            selected.add(population.get(index));
        }

        return selected;
    }
}
