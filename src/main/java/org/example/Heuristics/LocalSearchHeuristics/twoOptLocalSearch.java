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
public class twoOptLocalSearch implements LocalSearchHeuristic{
    public double searchRate;
    public twoOptLocalSearch(Map<String,Double> param ) {searchRate = param.get("LocalSearchProbability");}


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

        while (improved){
            improved = false;
            for (int i = 1; i < tour.length - 2; i++) {
                for (int j = i + 1; j < tour.length; j++) {
                    double oldFitness = individual.getFitness(); // Get current fitness

                    // Apply the 2-opt swap
                    reverseSegment(tour, i, j);
                    individual.setTour(tour); // Updates fitness

                    // If no improvement, undo the change
                    if (individual.getFitness() <= oldFitness) {
                        reverseSegment(tour, i, j); // Revert swap
                        individual.setTour(tour); // Updates fitness
                    } else {
                        improved = true;
                    }
                }
            }
        }
        individual.setTour(tour);
        return individual;
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
}
