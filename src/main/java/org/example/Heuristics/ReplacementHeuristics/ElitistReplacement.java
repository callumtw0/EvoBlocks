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
public class ElitistReplacement implements ReplacementHeuristic {
    double elitismRate;

    public ElitistReplacement(Map<String,Double> param) {
        this.elitismRate = param.get("ElitismRate");

    }

    @Override
    public ArrayList<Individual> recombine(ArrayList<Individual> parents, ArrayList<Individual> offspring) {
        int elitismCount = (int) (parents.size() * elitismRate);

        // Sort parents by fitness (descending)
        parents.sort(Comparator.comparingDouble(Individual::getFitness).reversed());

        // Select the top N elites
        ArrayList<Individual> nextGeneration = new ArrayList<>(parents.subList(0, elitismCount));

        // Fill remaining spots with best offspring
        offspring.sort(Comparator.comparingDouble(Individual::getFitness).reversed());
        nextGeneration.addAll(offspring.subList(0, parents.size() - elitismCount));

        return nextGeneration;
    }
}
