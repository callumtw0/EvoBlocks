package org.example.Heuristics.MutationHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Arrays;
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
public class DisplacementMutation implements MutationHeuristic{

    double mutationRate;

    public DisplacementMutation(Map<String,Double> param) {
        this.mutationRate = param.get("MutationRate");
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
        Random rand = new Random();
        if(rand.nextFloat() < mutationRate) {
            int numCities = individual.getTour().length;
            int[] tour = individual.getTour();

            int i = rand.nextInt(numCities);
            int j = rand.nextInt(numCities);
            while (i == j) {
                j = rand.nextInt(numCities);
            }
            if (i > j) {
                int temp = i;
                i = j;
                j = temp;
            }

            // Extract the segment
            int[] segment = Arrays.copyOfRange(tour, i, j + 1);
            int segmentLength = j - i + 1;

            int[] newTour = new int[numCities];
            int index = 0;
            // create a new tour without the segment
            for (int k = 0; k < numCities; k++) {
                if (k < i || k > j) {
                    newTour[index++] = tour[k];
                }
            }
            int insertPos;
            if (numCities <= segmentLength) {
                insertPos = 0;
            } else {
                insertPos = rand.nextInt(numCities - segmentLength);
            }

            System.arraycopy(newTour, insertPos, newTour, insertPos + segmentLength, numCities - segmentLength - insertPos);
            System.arraycopy(segment, 0, newTour, insertPos, segmentLength);

            individual.setTour(newTour);
        }
        return individual;
    }
}
