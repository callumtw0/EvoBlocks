package org.example.Heuristics.CrossoverHeuristics;



import org.example.Heuristics.Heuristic;
import org.example.Heuristics.SelectionHeuristics.SelectionHeuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
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
