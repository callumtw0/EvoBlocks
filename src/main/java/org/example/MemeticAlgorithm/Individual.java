package org.example.MemeticAlgorithm;

import java.util.Arrays;

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
    public int getNumberoOfCities(){
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
