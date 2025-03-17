package org.example.Heuristics.ReplacementHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;

public class GenerationalRepacement implements ReplacementHeuristic {

    @Override
    public ArrayList<Individual> recombine(ArrayList<Individual> parents, ArrayList<Individual> offspring) {
        return new ArrayList<>(offspring); // Completely replace parents
    }
}
