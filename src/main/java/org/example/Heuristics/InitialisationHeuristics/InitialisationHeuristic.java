package org.example.Heuristics.InitialisationHeuristics;



import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;

public interface InitialisationHeuristic {
    ArrayList<Individual> run(int populationSize);
}
