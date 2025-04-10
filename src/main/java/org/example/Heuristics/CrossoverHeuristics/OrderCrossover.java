package org.example.Heuristics.CrossoverHeuristics;



import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.Arrays;
import java.util.HashSet;
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
public class OrderCrossover extends CrossoverHeuristic{
    int populationSize;
    SelectionHeuristic selectionHeuristic;
    double[][] distanceMatrix;

    public OrderCrossover(SelectionHeuristic selectionHeuristic, int populationSize) {
        super(selectionHeuristic, populationSize);
    }

    @Override
    int[] generateOffspring(Individual parent1, Individual parent2){
        int numCities = parent1.getNumberoOfCities();
        int[] offspringTour = new int[numCities];
        Arrays.fill(offspringTour, -1); // Mark empty spots

        Random random = new Random();
        int point1 = random.nextInt(numCities);
        int point2 = random.nextInt(numCities - point1) + point1;

        // Copy the crossover segment from Parent 1
        HashSet<Integer> usedCities = new HashSet<>();
        for (int i = point1; i <= point2; i++) {
            offspringTour[i] = parent1.getTour()[i];
            usedCities.add(parent1.getTour()[i]);
        }

        // Fill remaining elements from Parent 2, maintaining order
        int index = (point2 + 1) % numCities;
        for (int i = 0; i < numCities; i++) {
            int city = parent2.getTour()[i];
            if (!usedCities.contains(city)) {
                offspringTour[index] = city;
                usedCities.add(city);
                index = (index + 1) % numCities; // Wrap around if needed
            }
        }
        return offspringTour;
    }
}
