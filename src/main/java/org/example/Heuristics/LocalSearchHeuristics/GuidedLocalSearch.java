package org.example.Heuristics.LocalSearchHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GuidedLocalSearch implements LocalSearchHeuristic{
    private double lambda; // Controls how much penalty affects cost
    private final Map<String, Double> penalties = new HashMap<>();


    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population) {
        penalties.clear();
        for(Individual individual : population) {
            run(individual);
        }
        return population;
    }

    @Override
    public Individual run(Individual individual) {
        Random random = new Random(42); // Use a fixed seed for testing
        if (random.nextDouble() > 0.2) return individual;
//        penalties = new HashMap<>();
        double[][] distanceMatrix = individual.getDistanceMatrix();

        int[] tour = individual.getTour();
        boolean improved = true;
        int iterations = 0;
        this.lambda = 0.1 * calculateAverageFitness(distanceMatrix);

        while (improved && iterations < 500) {
            improved = false;
            int[] bestTour = tour.clone();
            double bestFitness = computeTotalFitnessWithPenalties(tour, distanceMatrix);

            for (int i = 1; i < tour.length - 1; i++) {
                for (int j = i + 1; j < tour.length; j++) {
                    double newFitness = computeNewFitnessWithPenalties(tour, i, j, distanceMatrix);

                    if (newFitness > bestFitness + 1e-6) {
                        reverseSegment(tour, i, j);
                        bestFitness = newFitness;
                        bestTour = tour.clone();
                        improved = true;
                        addPenalty(tour[i], tour[j]);
                    }
                }
            }
            individual.setTour(bestTour);
//            System.out.println(individual.getFitness());
            iterations++;
        }
        return individual;
    }

    private void addPenalty(int city1, int city2) {
        String key;
        if (city1 < city2) {
            key = city1 + "-" + city2;
        }
        else {
            key = city2 + "-" + city1;
        }
        penalties.put(key, penalties.getOrDefault(key, 0.0) + 1.0);
    }

    private double getPenalty(int city1, int city2) {
        if (city1 < city2) {
            return penalties.getOrDefault(city1 + "-" + city2, 0.0);
        }
        else {
            return penalties.getOrDefault(city2 + "-" + city1, 0.0);
        }
    }

    private void reverseSegment(int[] tour, int i, int j) {
        while (i < j) {
            int temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;
            i++;
            j--;
        }
    }
    private double calculateAverageFitness(double[][] distanceMatrix) {
        double total = 0;
        int count = 0;
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = i + 1; j < distanceMatrix[i].length; j++) {
                total += distanceMatrix[i][j];
                count++;
            }
        }
        return count / total;
    }

    private double computeNewFitnessWithPenalties(int[] tour, int i, int j, double[][] distanceMatrix) {
        int prev1 = (i - 1 + tour.length) % tour.length;
        int next1 = (i + 1) % tour.length;
        int prev2 = (j - 1 + tour.length) % tour.length;
        int next2 = (j + 1) % tour.length;

        // **Old edge distances + penalties**
        double oldDistance = distanceMatrix[tour[prev1]][tour[i]] +
                distanceMatrix[tour[i]][tour[next1]] +
                distanceMatrix[tour[prev2]][tour[j]] +
                distanceMatrix[tour[j]][tour[next2]];

        double oldPenalty = lambda * (getPenalty(tour[prev1], tour[i]) +
                getPenalty(tour[i], tour[next1]) +
                getPenalty(tour[prev2], tour[j]) +
                getPenalty(tour[j], tour[next2]));

        // **New edge distances after swap + penalties**
        double newDistance = distanceMatrix[tour[prev1]][tour[j]] +
                distanceMatrix[tour[j]][tour[next1]] +
                distanceMatrix[tour[prev2]][tour[i]] +
                distanceMatrix[tour[i]][tour[next2]];

        double newPenalty = lambda * (getPenalty(tour[prev1], tour[j]) +
                getPenalty(tour[j], tour[next1]) +
                getPenalty(tour[prev2], tour[i]) +
                getPenalty(tour[i], tour[next2]));

        return (1 / (newDistance + newPenalty)) - (1 / (oldDistance + oldPenalty));
    }
    private double computeTotalFitnessWithPenalties(int[] tour, double[][] distanceMatrix) {
        double totalDistance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            totalDistance += distanceMatrix[tour[i]][tour[i + 1]] + lambda * getPenalty(tour[i], tour[i + 1]);
        }
        totalDistance += distanceMatrix[tour[tour.length - 1]][tour[0]] + lambda * getPenalty(tour[tour.length - 1], tour[0]);
        return 1 / totalDistance;  // ✅ Fitness now accounts for past penalties
    }
}
