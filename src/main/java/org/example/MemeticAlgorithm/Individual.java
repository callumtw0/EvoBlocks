package org.example.MemeticAlgorithm;

import java.util.Arrays;
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
public class Individual {

    private int[] tour;
    private double fitness;
    double[][] distanceMatrix;
    private double distance;

    public Individual(int[] tour, double[][] distanceMatrix){
        this.tour = tour.clone();
        this.distanceMatrix = distanceMatrix;
        updateFitness();
    }

    public void setTour(int[] tour) {
        this.tour = tour;
        updateFitness();
    }

    public int[] getTour() {
        return tour;
    }
    public int getNumberOfCities(){
        return tour.length;
    }

    public void updateFitness() {
        double totalDistance = 0;

        for (int i = 0; i < tour.length - 1; i++) {
            totalDistance += distanceMatrix[tour[i]][tour[i + 1]];
        }

        // Add the distance to return to the starting city
        totalDistance += distanceMatrix[tour[tour.length - 1]][tour[0]];

        distance = totalDistance;
        fitness = 1/totalDistance;
    }
    private boolean isValidTour(int[] tour) {
        boolean[] visited = new boolean[tour.length];
        for (int city : tour) {
            if (city < 0 || city >= tour.length || visited[city]) {
                return false;
            }
            visited[city] = true;
        }
        return true;
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public double getFitness() {
        return fitness;
    }
    public double getDistance() {return distance;}
    @Override
    public String toString() {
        return "Tour: " + Arrays.toString(tour) + ", Fitness: " + fitness;
    }

}
