package org.example.Heuristics.LocalSearchHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
/*
 * Copyright (c) 2025 Callum Welsford
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class GreedyDescentLocalSearch implements LocalSearchHeuristic{

    public double searchRate;
    public GreedyDescentLocalSearch(Map<String,Double> param ) {searchRate = param.get("LocalSearchProbability");}

    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population) {
        for(Individual individual : population) {
            run(individual);
        }
        return population;
    }

    @Override
    public Individual run(Individual individual) {
        Random random = new Random(42); // Use a fixed seed for testing
        if (random.nextDouble() > searchRate) return individual;
        int[] tour = individual.getTour();
        double[][] distanceMatrix = individual.getDistanceMatrix();
        boolean improved = true;
        while (improved) {
            improved = false;
            int worstEdgeIndex = findWorstEdge(tour, distanceMatrix);

            for (int i = 0; i < tour.length; i++) {
                if (i == worstEdgeIndex || i + 1 == worstEdgeIndex) continue; // Skip swapping adjacent worst edges

                double oldFitness = individual.getFitness();
                swapCities(tour, worstEdgeIndex, i);
                individual.setTour(tour);

                if (individual.getFitness() <= oldFitness) {
                    swapCities(tour, worstEdgeIndex, i); // Undo swap if no improvement
                    individual.setTour(tour);
                } else {
                    improved = true;
//                    individual.setTour(tour);
                }
            }
        }
        return individual;
    }

    private int findWorstEdge(int[] tour, double[][] distanceMatrix) {
        int worstIndex = 0;
        double maxDistance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            double dist = distanceMatrix[tour[i]][tour[i + 1]];
            if (dist > maxDistance) {
                maxDistance = dist;
                worstIndex = i;
            }
        }
        return worstIndex;
    }
    private double calculateDistance(int[] tour, double[][] distanceMatrix) {
        double distance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            distance += distanceMatrix[tour[i]][tour[i + 1]];
        }
        return distance;
    }
    private void swapCities(int[] tour, int i, int j) {
        int temp = tour[i];
        tour[i] = tour[j];
        tour[j] = temp;
    }
}
