package org.example.Heuristics.SelectionHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Comparator;
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
