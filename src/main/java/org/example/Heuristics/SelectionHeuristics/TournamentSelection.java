package org.example.Heuristics.SelectionHeuristics;


import org.example.MemeticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
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
public class TournamentSelection implements SelectionHeuristic {
    private double tournamentSize;

    public TournamentSelection(Map<String,Double> param) {
        this.tournamentSize = param.get("TournamentSize");
    }
    public TournamentSelection(){
        this.tournamentSize = 4;
    }
    @Override
    public ArrayList<Individual> run(ArrayList<Individual> population, int percentage) {
        ArrayList<Individual> selected = new ArrayList<>();
        Random random = new Random();
        int number = (int) (population.size() * (((double) percentage)/100));

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
