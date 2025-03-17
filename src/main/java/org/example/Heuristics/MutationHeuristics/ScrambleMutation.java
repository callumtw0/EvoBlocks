package org.example.Heuristics.MutationHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.*;

public class ScrambleMutation implements MutationHeuristic {
    double mutationRate;

    public ScrambleMutation(Map<String,Double> param) {
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
            // Convert selected segment to list and shuffle
            List<Integer> segment = new ArrayList<>();
            for (int k = i; k <= j; k++) {
                segment.add(tour[k]);
            }
            Collections.shuffle(segment);

            // Insert back shuffled values
            for (int k = i; k <= j; k++) {
                tour[k] = segment.get(k - i);
            }

            individual.setTour(tour);

        }
        return individual;
    }
}
