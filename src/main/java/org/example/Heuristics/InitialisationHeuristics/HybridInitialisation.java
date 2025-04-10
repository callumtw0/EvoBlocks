package org.example.Heuristics.InitialisationHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
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

