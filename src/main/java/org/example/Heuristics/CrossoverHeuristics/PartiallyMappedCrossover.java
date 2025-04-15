package org.example.Heuristics.CrossoverHeuristics;


import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.Arrays;
import java.util.HashMap;
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
public class PartiallyMappedCrossover extends CrossoverHeuristic{
    int populationSize;
    SelectionHeuristic selectionHeuristic;
    private Random rand;

    public PartiallyMappedCrossover(SelectionHeuristic selectionHeuristic, int populationSize, Random random) {
        super(selectionHeuristic, populationSize);
        this.rand = random != null ? random : new Random();
    }

    public PartiallyMappedCrossover(SelectionHeuristic selectionHeuristic, int populationSize) {
        this(selectionHeuristic, populationSize, new Random());
    }


    @Override
    public int[] generateOffspring(Individual parent1, Individual parent2){
        int numCities = parent1.getNumberOfCities();
        int[] offspringTour = new int[numCities];
        Arrays.fill(offspringTour, -1); // Mark empty spots

//        rand = new Random();
        int point1 = rand.nextInt(numCities);
        int point2 = rand.nextInt(numCities - point1) + point1;

        // Copy the segment from Parent 1
        HashMap<Integer, Integer> mapping = new HashMap<>();
        for (int i = point1; i <= point2; i++) {
            offspringTour[i] = parent1.getTour()[i];
//                mapping.put(parent2.getTour()[i], parent1.getTour()[i]);
            mapping.put(parent1.getTour()[i], parent2.getTour()[i]);
        }
        // Copy remaining elements from Parent 2, resolving conflicts
        for (int i = 0; i < numCities; i++) {
            if (offspringTour[i] == -1) { // If not filled yet
                int candidate = parent2.getTour()[i];

                // Resolve conflicts using mapping
                while (mapping.containsKey(candidate)) {
                    candidate = mapping.get(candidate);

                }

                offspringTour[i] = candidate;
            }
        }
        return offspringTour;
    }
}
