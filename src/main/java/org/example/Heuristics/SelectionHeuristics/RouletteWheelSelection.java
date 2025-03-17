package org.example.Heuristics.SelectionHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Random;

public class RouletteWheelSelection implements SelectionHeuristic {
    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population, int number) {
        ArrayList<Individual> selected = new ArrayList<>();
        double totalFitness = population.stream().mapToDouble(Individual::getFitness).sum();
        Random rand = new Random();

        for (int i = 0; i < number; i++) {
            double random = rand.nextDouble() * totalFitness;
            double cumulativeFitness = 0;
            for (Individual individual : population) {
                cumulativeFitness += individual.getFitness();
                if (cumulativeFitness >= random) {
                    selected.add(individual);
                    break;
                }
            }
        }
        return selected;
    }
}
