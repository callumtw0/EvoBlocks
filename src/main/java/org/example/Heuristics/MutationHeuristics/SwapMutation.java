package org.example.Heuristics.MutationHeuristics;



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
public class SwapMutation implements MutationHeuristic {
    double mutationRate;
    private final Random rand; // Added for deterministic testing

    public SwapMutation(Map<String, Double> param, Random rand) {
        this.mutationRate = param.get("MutationRate");
        this.rand = rand != null ? rand : new Random();
    }

    public SwapMutation(Map<String, Double> param) {
        this(param, new Random());
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
        if(rand.nextDouble() < mutationRate) {
            int numCities = individual.getTour().length;
            int[] tour = individual.getTour();

            int i = rand.nextInt(numCities);
            int j = rand.nextInt(numCities);
            while (i == j) {
                j = rand.nextInt(numCities);
            }

            // Swap two cities
            int temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;

            individual.setTour(tour);

        }
    return individual;
    }
}
