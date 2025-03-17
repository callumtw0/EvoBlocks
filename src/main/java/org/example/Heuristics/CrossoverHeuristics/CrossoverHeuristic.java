package org.example.Heuristics.CrossoverHeuristics;



import org.example.Heuristics.Heuristic;
import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;

public abstract class CrossoverHeuristic implements Heuristic {
    int populationSize;
    SelectionHeuristic selectionHeuristic;
    double[][] distanceMatrix;

    public CrossoverHeuristic(SelectionHeuristic selectionHeuristic, int populationSize){
        this.populationSize = populationSize;
        this.selectionHeuristic = selectionHeuristic;

    }

    public Individual run(ArrayList<Individual> population){

        distanceMatrix = population.get(0).getDistanceMatrix();

            ArrayList<Individual> parents = selectionHeuristic.run(population,2);
            Individual parent1 = parents.get(0);
            Individual parent2 = parents.get(1);
            int[] offspringTour = generateOffspring(parent1,parent2);

        return (new Individual(offspringTour,distanceMatrix));
    }
    public Individual run(ArrayList<Individual> population, Individual parent1){

        distanceMatrix = population.get(0).getDistanceMatrix();

            ArrayList<Individual> parents = selectionHeuristic.run(population,1);
            Individual parent2 = parents.get(0);
            int[] offspringTour = generateOffspring(parent1,parent2);
            return (new Individual(offspringTour,distanceMatrix));


    }
    abstract int[] generateOffspring(Individual parent1, Individual parent2);
}
