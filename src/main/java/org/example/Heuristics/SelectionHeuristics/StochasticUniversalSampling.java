package org.example.Heuristics.SelectionHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
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
public class StochasticUniversalSampling implements SelectionHeuristic{
    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population, int number) {
        ArrayList<Individual> selected = new ArrayList<>();
        double totalFitness = population.stream().mapToDouble(Individual::getFitness).sum();
        // Generate evenly spaced selection points
        double spacing = totalFitness / number;
        double start = new Random().nextDouble() * spacing;
        double[] pointers = new double[number];
        for (int i = 0; i < number; i++) {
            pointers[i] = start + i * spacing;
        }

        // Perform selection
        int index = 0;
        double cumulativeFitness = population.get(0).getFitness();
        for (double pointer : pointers) {
            while (cumulativeFitness < pointer) {
                index++;
                cumulativeFitness += population.get(index).getFitness();
            }
            selected.add(population.get(index));
        }

        return selected;
    }
}
