package org.example.Heuristics.MutationHeuristics;



import org.example.Heuristics.Heuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;

public interface MutationHeuristic extends Heuristic {
    ArrayList<Individual> run(ArrayList<Individual> population);
    Individual run(Individual individual);
}
