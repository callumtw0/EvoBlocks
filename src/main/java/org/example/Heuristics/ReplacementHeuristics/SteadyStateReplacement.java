package org.example.Heuristics.ReplacementHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
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
public class SteadyStateReplacement implements ReplacementHeuristic{
    private double replacementRate; // Fraction of parents to replace

    public SteadyStateReplacement(Map<String,Double> param) {
        this.replacementRate = param.get("ReplacementRate");
    }
    @Override
    public ArrayList<Individual> recombine(ArrayList<Individual> parents, ArrayList<Individual> offspring) {
        int replaceCount = (int) (parents.size() * replacementRate);
        ArrayList<Individual> population = new ArrayList<>(parents);;
        // Sort offspring by fitness (descending)
        offspring.sort(Comparator.comparingDouble(Individual::getFitness).reversed());

        // Select the best offspring
        ArrayList<Individual> newIndividuals = new ArrayList<>(offspring.subList(0, replaceCount));

        // Replace worst individuals in parent population
        population.sort(Comparator.comparingDouble(Individual::getFitness));
        population.subList(0, replaceCount).clear();
        population.addAll(newIndividuals);

        return population;
    }
}
