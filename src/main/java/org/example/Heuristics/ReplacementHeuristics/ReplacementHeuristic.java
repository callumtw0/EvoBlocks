package org.example.Heuristics.ReplacementHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;

public interface ReplacementHeuristic  {

    public ArrayList<Individual> recombine(ArrayList<Individual> parents, ArrayList<Individual> offspring);
}
