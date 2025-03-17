package org.example.Heuristics.LocalSearchHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Arrays;

public class ThreeOptLocalSearch implements LocalSearchHeuristic{
    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population) {
        for(Individual individual : population) {
            run(individual);
        }
        return population;
    }

    @Override
    public Individual run(Individual individual) {
        int[] tour = individual.getTour();
        boolean improved = true;

        while (improved) {
            improved = false;
            int[] bestTour = Arrays.copyOf(tour, tour.length);
            double bestFitness = individual.getFitness();

            for (int i = 0; i < tour.length - 4; i++) {
                for (int j = i + 2; j < tour.length - 2; j++) {
                    // 🔹 **First, try 2-Opt before moving to 3-Opt**
                    int[] twoOptTour = reverseSegment(tour, i, j);
                    double twoOptFitness = computeFitness(twoOptTour, individual.getDistanceMatrix());
                    if (twoOptFitness > bestFitness) {
                        bestTour = twoOptTour;
                        bestFitness = twoOptFitness;
                        improved = true;
                        continue; // Skip deeper moves if a 2-opt move works
                    }

                    for (int k = j + 2; k < tour.length; k++) {
                        int[][] possibleMoves = generateThreeOptMoves(tour, i, j, k);

                        for (int[] candidate : possibleMoves) {
                            double candidateFitness = computeFitness(candidate, individual.getDistanceMatrix());
                            if (candidateFitness > bestFitness) {
                                bestFitness = candidateFitness;
                                bestTour = candidate;
                                improved = true;
                            }
                        }
                    }
                }
            }

            // ✅ Only update the tour when a real improvement is found
            if (improved) {
                individual.setTour(bestTour);
            }
        }
        return individual;
    }

    private int[][] generateThreeOptMoves(int[] tour, int i, int j, int k) {
        int[][] moves = new int[7][tour.length];
        moves[0] = swapEdges(tour, i, j);  // Swap first segment
        moves[1] = swapEdges(tour, j, k);  // Swap second segment
        moves[2] = swapEdges(tour, i, k);  // Swap third segment
        moves[3] = reverseSegment(tour, i, j);  // Reverse first segment
        moves[4] = reverseSegment(tour, j, k);  // Reverse second segment
        moves[5] = reverseSegment(tour, i, k);  // Reverse third segment
        moves[6] = tour;  // Original tour as a fallback
        return moves;
    }

    private int[] swapEdges(int[] tour, int i, int j) {
        int[] newTour = Arrays.copyOf(tour, tour.length);
        int temp = newTour[i];
        newTour[i] = newTour[j];
        newTour[j] = temp;
        return newTour;
    }
    private int[] reverseSegment(int[] tour, int i, int j) {
        int[] newTour = Arrays.copyOf(tour, tour.length);
        while (i < j) {
            int temp = newTour[i];
            newTour[i] = newTour[j];
            newTour[j] = temp;
            i++;
            j--;
        }
        return newTour;
    }
    private double computeFitness(int[] tour, double[][] distanceMatrix) {
        double totalDistance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            totalDistance += distanceMatrix[tour[i]][tour[i + 1]];
        }
        totalDistance += distanceMatrix[tour[tour.length - 1]][tour[0]];
        return 1 / totalDistance; // Higher fitness = shorter distance
    }

}
