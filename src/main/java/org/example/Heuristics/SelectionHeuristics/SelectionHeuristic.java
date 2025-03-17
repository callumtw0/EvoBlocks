package org.example.Heuristics.SelectionHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;

public interface SelectionHeuristic  {
    ArrayList<Individual> run(ArrayList<Individual> population, int number);
}
