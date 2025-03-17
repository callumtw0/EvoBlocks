package org.example.Heuristics.MutationHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class InversionMutation implements MutationHeuristic{
    double mutationRate;

    public InversionMutation(Map<String,Double> param) {
        this.mutationRate = param.get("MutationRate");
    }

    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population) {
        for(Individual individual : population) {
            run(individual);

        }
        return population;
    }

    @Override
    public Individual run(Individual individual) {
        Random rand = new Random();
        if(rand.nextFloat() < mutationRate) {
            int numCities = individual.getTour().length;
            int[] tour = individual.getTour();

            int i = rand.nextInt(numCities);
            int j = rand.nextInt(numCities);
            while (i == j) {
                j = rand.nextInt(numCities);
            }
            if (i > j) {
                int temp = i;
                i = j;
                j = temp;
            }
            // Reverse the segment between i and j
            while (i < j) {
                int temp = tour[i];
                tour[i] = tour[j];
                tour[j] = temp;
                i++;
                j--;
            }
            individual.setTour(tour);
        }
        return individual;
    }
}
