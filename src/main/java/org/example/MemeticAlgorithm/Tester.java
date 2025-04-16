package org.example.MemeticAlgorithm;


import javafx.application.Platform;
import javafx.concurrent.Task;
import org.example.GUI_FX.HeuristicData;
import org.example.GUI_FX.RightPanel;
import org.example.Heuristics.CrossoverHeuristics.*;
import org.example.Heuristics.InitialisationHeuristics.HybridInitialisation;
import org.example.Heuristics.InitialisationHeuristics.InitialisationHeuristic;
import org.example.Heuristics.InitialisationHeuristics.NearestNeighbourInitialisation;
import org.example.Heuristics.InitialisationHeuristics.RandomInitialisation;
import org.example.Heuristics.LocalSearchHeuristics.GreedyDescentLocalSearch;
import org.example.Heuristics.LocalSearchHeuristics.LocalSearchHeuristic;
import org.example.Heuristics.LocalSearchHeuristics.twoOptLocalSearch;
import org.example.Heuristics.MutationHeuristics.*;
import org.example.Heuristics.ReplacementHeuristics.*;
import org.example.Heuristics.SelectionHeuristics.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.example.MemeticAlgorithm.TSPUtils.*;

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
public class Tester {
    TSPLIB_parser parser = new TSPLIB_parser();
    Map<Integer, double[]> map;
    ArrayList<Individual> population = new ArrayList<>();
    double[][] distanceMatrix;
    int numCities;
    double optimalDistance;


    InitialisationHeuristic initialisationHeuristic;
    SelectionHeuristic selectionHeuristic;
    CrossoverHeuristic crossoverHeuristic;
    MutationHeuristic mutationHeuristic;
    LocalSearchHeuristic localSearchHeuristic;
    ReplacementHeuristic replacementHeuristic;

    private RightPanel rightPanel;
    private volatile boolean isRunning = false;
    private volatile boolean isPaused = false;
    private Task<Void> currentTask;// Flag to control thread execution

    public Tester(RightPanel rightPanel) {
        this.rightPanel = rightPanel;
    }

    public void runForIDE(List<HeuristicData> heuristics, String selectedTspFile, Integer numGenerations, Integer populationSize, List<Map<String, Double>> heuristicParameters) throws IOException {
        // Stop any existing task
        isRunning = false;
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
            try {
                Thread.sleep(100); // Give a moment for cancellation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset graphs and wait for completion
        rightPanel.resetGraph();

        isRunning = true;

        currentTask = new Task<>() {
            @Override
            protected Void call() throws IOException {

                ArrayList<Individual> offspringPopulation = new ArrayList<>();
                isRunning = true;
                map = parser.parse(selectedTspFile);
                distanceMatrix = TSPUtils.computeDistanceMatrix(map);
                numCities = map.size();
                optimalDistance = parser.parseOptimalTour(selectedTspFile,distanceMatrix);
                try {
                    String initialisation = heuristics.getFirst().getName().toLowerCase(); // Get the constant value
                    Map<String,Double> initialisationParam = heuristicParameters.getFirst();

                    switch (initialisation) {
                        case RANDOM_INITIALISATION:
                            initialisationHeuristic = new RandomInitialisation(distanceMatrix, initialisationParam);
                            break;
                        case NN_INITIALISATION:
                            initialisationHeuristic = new NearestNeighbourInitialisation(distanceMatrix, initialisationParam);
                            break;
                        case HYBRID_INITIALISATION:
                            initialisationHeuristic = new HybridInitialisation(distanceMatrix, initialisationParam);
                            break;
                    }
                    heuristics.removeFirst();
                    heuristicParameters.removeFirst();

                    String replacement = heuristics.getFirst().getName().toLowerCase(); // Get the constant value
                    Map<String,Double> replacementParam = heuristicParameters.getFirst();
                    switch (replacement) {
                        case ELITIST_REPLACEMENT:
                            replacementHeuristic = new ElitistReplacement(replacementParam);
                            break;
                        case FITNESS_BASED_REPLACEMENT:
                            replacementHeuristic = new FitnessBasedReplacement();
                            break;
                        case GENERATIONAL_REPLACEMENT:
                            replacementHeuristic = new GenerationalReplacement();
                            break;
                        case STEADY_STATE_REPLACEMENT:
                            replacementHeuristic = new SteadyStateReplacement(replacementParam);
                            break;
                    }
                    heuristics.removeFirst();
                    heuristicParameters.removeFirst();
                    population = initialisationHeuristic.run(populationSize);

                    // Collect heuristic names for display
                    StringBuilder heuristicsUsed = new StringBuilder();
                    for (HeuristicData heuristic : heuristics) {
                        if (heuristicsUsed.length() > 0) {
                            heuristicsUsed.append(", ");
                        }
                        heuristicsUsed.append(heuristic.getName());
                    }
                    heuristicsUsed.insert(0, initialisation + ", ");
                    heuristicsUsed.append(", ").append(replacement);

                    int ranHeuristics;
                    for (int gen = 0; gen < numGenerations && !isCancelled() && isRunning; gen++) {
                        while (isPaused && !isCancelled() && isRunning) {
                            synchronized (Tester.this) {
                                try {
                                    Tester.this.wait(); // Wait until resumed
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        }
                        if (isCancelled() || !isRunning) break;
                        ranHeuristics = 0;
                        offspringPopulation = (ArrayList<Individual>) population.clone();
                        int pos = 0;
                        for (HeuristicData heuristic : heuristics){
                            if (Objects.equals(heuristic.getCategory(), "Mutation")) {
                                String mutation = heuristic.getName().toLowerCase(); // Get the constant value
                                Map<String,Double> mutationParam = heuristicParameters.get(ranHeuristics);
                                MutationHeuristic mutationHeuristic = switch (mutation) {
                                    case SWAP_MUTATION -> new SwapMutation(mutationParam);
                                    case INVERSION_MUTATION -> new InversionMutation(mutationParam);
                                    case SCRAMBLE_MUTATION -> new ScrambleMutation(mutationParam);
                                    case DISPLACEMENT_MUTATION -> new DisplacementMutation(mutationParam);
                                    default -> throw new IllegalStateException("Unexpected value: " + heuristic.getName());
                                };
                                offspringPopulation = mutationHeuristic.run(offspringPopulation);
                                ranHeuristics++;
                            } else if (Objects.equals(heuristic.getCategory(), "Local Search")) {
                                String localSearch = heuristic.getName().toLowerCase(); // Get the constant value
                                Map<String,Double> searchParam = heuristicParameters.get(ranHeuristics);
                                LocalSearchHeuristic localSearchHeuristic = switch (localSearch) {
                                    case TWO_OPTION_LOCAL_SEARCH -> new twoOptLocalSearch(searchParam);
                                    case GREEDY_DESCENT_LOCAL_SEARCH -> new GreedyDescentLocalSearch(searchParam);
                                    default -> throw new IllegalStateException("Unexpected value: " + heuristic.getName());
                                };
                                offspringPopulation = localSearchHeuristic.run(offspringPopulation);
                                ranHeuristics++;
                            } else {
                                break;
                            }
                        }

                        if (!heuristics.isEmpty() && isRunning && !isCancelled() && heuristics.size() > ranHeuristics) {
                            ArrayList<Individual> newPopulation = new ArrayList<>();
                            while (newPopulation.size() < populationSize && isRunning) {
                                Individual candidate;

                                String selection = heuristics.get(ranHeuristics).getName().toLowerCase(); // Get the constant value
                                Map<String,Double> selectionParam = heuristicParameters.get(ranHeuristics);
                                switch (selection) {
                                    case ELITIST_SELECTION:
                                        selectionHeuristic = new ElitistSelection();
                                        break;
                                    case ROULETTE_WHEEL_SELECTION:
                                        selectionHeuristic = new RouletteWheelSelection();
                                        break;
                                    case RANK_BASED_SELECTION:
                                        selectionHeuristic = new RankBasedSelection();
                                        break;
                                    case TOURNAMENT_SELECTION:
                                        selectionHeuristic = new TournamentSelection(selectionParam);
                                        break;
                                    case STOCHASTIC_UNIVERSAL_SAMPLING:
                                        selectionHeuristic = new StochasticUniversalSampling();
                                        break;
                                }
                                String crossover = heuristics.get(ranHeuristics + 1).getName().toLowerCase(); // Get the constant value
                                switch (crossover) {
                                    case PARTIALLY_MATCHED_CROSSOVER:
                                        crossoverHeuristic = new PartiallyMappedCrossover(selectionHeuristic, populationSize);
                                        break;
                                    case CYCLE_CROSSOVER:
                                        crossoverHeuristic = new CycleCrossover(selectionHeuristic, populationSize);
                                        break;
                                    case EDGE_RECOMBINATION_CROSSOVER:
                                        crossoverHeuristic = new EdgeRecombinationCrossover(selectionHeuristic, populationSize);
                                        break;
                                    case ORDER_CROSSOVER:
                                        crossoverHeuristic = new OrderCrossover(selectionHeuristic, populationSize);
                                        break;
                                }

                                candidate = crossoverHeuristic.run(offspringPopulation);

                                for (int i = ranHeuristics + 2; i < heuristics.size() && isRunning; i++) {
                                    HeuristicData heuristic = heuristics.get(i);
                                    Map<String,Double> param = heuristicParameters.get(i);
                                    switch (heuristic.getCategory()) {
                                        case "Selection":
                                            String sel = heuristic.getName().toLowerCase(); // Get the constant value
                                            switch (sel) {
                                                case ELITIST_SELECTION:
                                                    selectionHeuristic = new ElitistSelection();
                                                    break;
                                                case ROULETTE_WHEEL_SELECTION:
                                                    selectionHeuristic = new RouletteWheelSelection();
                                                    break;
                                                case RANK_BASED_SELECTION:
                                                    selectionHeuristic = new RankBasedSelection();
                                                    break;
                                                case TOURNAMENT_SELECTION:
                                                    selectionHeuristic = new TournamentSelection(param);
                                                    break;
                                                case STOCHASTIC_UNIVERSAL_SAMPLING:
                                                    selectionHeuristic = new StochasticUniversalSampling();
                                                    break;
                                            }
                                            break;
                                        case "Crossover":
                                            String cross = heuristic.getName().toLowerCase(); // Get the constant value
                                            switch (cross) {
                                                case PARTIALLY_MATCHED_CROSSOVER:
                                                    crossoverHeuristic = new PartiallyMappedCrossover(selectionHeuristic, populationSize);
                                                    break;
                                                case CYCLE_CROSSOVER:
                                                    crossoverHeuristic = new CycleCrossover(selectionHeuristic, populationSize);
                                                    break;
                                                case EDGE_RECOMBINATION_CROSSOVER:
                                                    crossoverHeuristic = new EdgeRecombinationCrossover(selectionHeuristic, populationSize);
                                                    break;
                                                case ORDER_CROSSOVER:
                                                    crossoverHeuristic = new OrderCrossover(selectionHeuristic, populationSize);
                                                    break;
                                            }
                                            candidate = crossoverHeuristic.run(offspringPopulation, candidate);
                                            break;
                                        case "Mutation":
                                            String mut = heuristic.getName().toLowerCase(); // Get the constant value
                                            switch (mut) {
                                                case SWAP_MUTATION:
                                                    mutationHeuristic = new SwapMutation(param);
                                                    break;
                                                case INVERSION_MUTATION:
                                                    mutationHeuristic = new InversionMutation(param);
                                                    break;
                                                case SCRAMBLE_MUTATION:
                                                    mutationHeuristic = new ScrambleMutation(param);
                                                    break;
                                                case DISPLACEMENT_MUTATION:
                                                    mutationHeuristic = new DisplacementMutation(param);
                                                    break;
                                            }
                                            candidate = mutationHeuristic.run(candidate);
                                            break;
                                        case "Local Search":
                                            String ls = heuristic.getName().toLowerCase(); // Get the constant value
                                            switch (ls) {
                                                case TWO_OPTION_LOCAL_SEARCH:
                                                    localSearchHeuristic = new twoOptLocalSearch(param);
                                                    break;
                                                case GREEDY_DESCENT_LOCAL_SEARCH:
                                                    localSearchHeuristic = new GreedyDescentLocalSearch(param);
                                                    break;
                                            }
                                            candidate = localSearchHeuristic.run(candidate);
                                            break;
                                    }
                                }
                                if (isRunning && !isCancelled()) newPopulation.add(candidate);
                            }
                            if (isRunning && !isCancelled()) offspringPopulation = newPopulation;
                        }
                        if (isRunning && !isCancelled()) population = replacementHeuristic.recombine(population, offspringPopulation);

                        // Find the best individual (shortest tour) in the population
                        if (isRunning) {
                            if (population.isEmpty()) {
                                throw new IllegalStateException("Population is empty, cannot compute best, worst, or average distance.");
                            }

                            Individual bestIndividual = population.get(0);
                            Individual worstIndividual = population.get(0);
                            double sumDistance = 0;
                            for (Individual individual : population) {
                                double distance = individual.getDistance(); // Now using getDistance() directly
                                if (Double.isNaN(distance) || Double.isInfinite(distance)) {
                                    throw new IllegalStateException("Invalid distance value for individual: " + distance);
                                }
                                sumDistance += distance;
                                if (distance < bestIndividual.getDistance()) { // Lower distance = better (shorter tour)
                                    bestIndividual = individual;
                                }
                                if (distance > worstIndividual.getDistance()) { // Higher distance = worse (longer tour)
                                    worstIndividual = individual;
                                }
                            }
                            double averageDistance = sumDistance / population.size();
                            double bestDistance = bestIndividual.getDistance();

                            double sumSquaredDiff = 0;
                            for (Individual individual : population) {
                                double diff = individual.getDistance() - averageDistance;
                                sumSquaredDiff += diff * diff;
                            }
                            double variance = sumSquaredDiff / population.size();
                            double stdDev = Math.sqrt(variance);

                            long convergedCount = population.stream().filter(ind -> Math.abs(ind.getDistance() - bestDistance) / bestDistance <= 0.05).count();
                            double convergenceRate = (convergedCount * 100.0) / population.size();

                            // Get the best tour order (array of city indices) and coordinates
                            int[] bestTour = bestIndividual.getTour(); // Assuming Individual has a getTour() method returning int[]
                            Map<Integer, double[]> cityCoords = map; // Use the parsed city coordinates from TSPLIB

                            // UI updates must be done on JavaFX thread
                            int finalGen = gen;
                            final double finalAverageDistance = averageDistance; // Effectively final for lambda
                            double finalBestDistance = bestIndividual.getDistance();
                            final double finalWorstDistance = worstIndividual.getDistance(); // Worst distance
                            Platform.runLater(() -> {
                                rightPanel.updateGraph(finalGen, finalAverageDistance, finalBestDistance, finalWorstDistance, optimalDistance);
                                rightPanel.updateBestTour(cityCoords, bestTour); // Update best tour visualization
                                rightPanel.updatePerformanceAndDeviation(finalGen, convergenceRate, stdDev);
                            });
                        }
                        try {
                            Thread.sleep(20); // Maintains UI responsiveness, with interruption handling
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // Restore interrupted status
                            break; // Exit the loop if interrupted
                        }
                        if (!isRunning || isCancelled()) break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isRunning = false; // Ensure flag is reset when task completes or errors
                }
                return null;
            }
            @Override
            protected void cancelled() {
                isRunning = false;
            }

            @Override
            protected void failed() {
                isRunning = false;
                Throwable exception = getException();
                exception.printStackTrace();
            }

            @Override
            protected void succeeded() {
                isRunning = false;
            }
        };

        // Start the task in a new thread and track it
        Thread currentThread = new Thread(currentTask);
        currentThread.setDaemon(true); // Allows the thread to terminate when the application closes
        currentThread.start();
    }

    public double getOptimalDistance() {
        return optimalDistance;
    }

    public void stop() {
        isRunning = false;
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }
        population.clear(); // Clear population to free memory
        rightPanel.resetGraph(); // Reset graphs to clear pending updates
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
        synchronized (this) {
            notifyAll(); // Notify waiting threads
        }
    }

    public boolean isPaused() {
        return isPaused;
    }
    public boolean isRunning(){
        return isRunning;
    }
}