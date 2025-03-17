package org.example.Heuristics.SelectionHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class TournamentSelection implements SelectionHeuristic {
    private double tournamentSize;

    public TournamentSelection(Map<String,Double> param) {
        this.tournamentSize = param.get("TournamentSize");
    }
    public TournamentSelection(){
        this.tournamentSize = 4;
    }
    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population, int number) {
        ArrayList<Individual> selected = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < number; i++) {
            ArrayList<Individual> tournament = new ArrayList<>();

            // Select random individuals for the tournament
            for (int j = 0; j < tournamentSize; j++) {
                int randomIndex = random.nextInt(population.size());
                tournament.add(population.get(randomIndex));
            }

            // Select the best from the tournament
            tournament.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
            selected.add(tournament.get(0));
        }
        return selected;
    }
}
