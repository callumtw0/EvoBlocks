package org.example.Heuristics.SelectionHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class RankBasedSelection implements SelectionHeuristic {

    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population, int number) {
        ArrayList<Individual> selected = new ArrayList<>();
        int populationSize = population.size();

        // Sort population in descending order of fitness (best first)
        population.sort(Comparator.comparingDouble(Individual::getFitness).reversed());

        // Compute total rank sum
        double totalRankSum = (populationSize * (populationSize + 1)) / 2.0; // Sum of first N natural numbers

        // Assign rank-based selection probabilities
        double[] probabilities = new double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            int rank = populationSize - i; // Highest fitness gets highest rank (N), lowest gets rank 1
            probabilities[i] = rank / totalRankSum;
        }

        // Perform Roulette-Wheel Selection based on rank probabilities
        Random rand = new Random();
        for (int i = 0; i < number; i++) {
            double random = rand.nextDouble(); // Random number between 0 and 1
            double cumulativeProbability = 0;

            for (int j = 0; j < populationSize; j++) {
                cumulativeProbability += probabilities[j];
                if (cumulativeProbability >= random) {
                    selected.add(population.get(j));
                    break;
                }
            }
        }

        return selected;    }
}
