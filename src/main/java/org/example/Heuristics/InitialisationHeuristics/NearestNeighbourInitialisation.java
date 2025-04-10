package org.example.Heuristics.InitialisationHeuristics;



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
public class NearestNeighbourInitialisation implements InitialisationHeuristic{
    double[][] distanceMatrix;
    int numCities;

    public NearestNeighbourInitialisation(double[][] distanceMatrix, Map<String, Double> selectionparam){
        this.distanceMatrix = distanceMatrix;
        numCities = distanceMatrix.length;
    }

    @Override
    public ArrayList<Individual> run(int populationSize) {
        ArrayList<Individual> population = new ArrayList<>();

        Random rand = new Random();

        while (population.size() < populationSize) {
            int startCity = rand.nextInt(numCities); // Choose a random starting city
            int[] tour = constructNearestNeighbourTour(startCity);
            population.add(new Individual(tour, distanceMatrix));
        }

        return population;
    }

    private int[] constructNearestNeighbourTour(int startCity) {
        boolean[] visited = new boolean[numCities];
        int[] tour = new int[numCities];
        int currentCity = startCity;

        tour[0] = currentCity;
        visited[currentCity] = true;

        for (int i = 1; i < numCities; i++) {
            int nearestCity = -1;
            double shortestDistance = Double.MAX_VALUE;

            for (int j = 0; j < numCities; j++) {
                if (!visited[j] && distanceMatrix[currentCity][j] < shortestDistance) {
                    nearestCity = j;
                    shortestDistance = distanceMatrix[currentCity][j];
                }
            }

            currentCity = nearestCity;
            tour[i] = currentCity;
            visited[currentCity] = true;
        }

        return tour;
    }


}
