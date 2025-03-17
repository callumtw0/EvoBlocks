package org.example.Heuristics.InitialisationHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Map;

public class HybridInitialisation implements InitialisationHeuristic{
    double[][] distanceMatrix;
    int numCities;
    double hybridRatio;
    Map<String, Double> selectionparam;

    public HybridInitialisation(double[][] distanceMatrix, Map<String, Double> selectionparam){
        this.distanceMatrix = distanceMatrix;
        numCities = distanceMatrix.length;
        hybridRatio = selectionparam.get("HybridRatio");
        this.selectionparam = selectionparam;
    }

    @Override
    public ArrayList<Individual> run(int populationSize) {
        ArrayList<Individual> population = new ArrayList<>();

        int secondHalf = (int) (populationSize * hybridRatio);
        int firstHalf = populationSize - secondHalf;

        // Use Random and Nearest Neighbour Initialisation
        InitialisationHeuristic randomInit = new RandomInitialisation(distanceMatrix, selectionparam);
        InitialisationHeuristic nnInit = new NearestNeighbourInitialisation(distanceMatrix, selectionparam);

        // Fill half the population with random individuals
        ArrayList<Individual> randomPopulation = randomInit.run(firstHalf);

        // Fill the other half with nearest neighbour individuals
        ArrayList<Individual> NNPopulation = nnInit.run(secondHalf);

        population.addAll(randomPopulation);
        population.addAll(NNPopulation);

        return population;
    }
}

