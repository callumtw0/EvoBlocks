package org.example.Heuristics.LocalSearchHeuristics;



import org.example.Heuristics.Heuristic;
import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;

public interface LocalSearchHeuristic extends Heuristic {
    ArrayList<Individual> run(ArrayList<Individual> population);
    Individual run(Individual individual);

}
