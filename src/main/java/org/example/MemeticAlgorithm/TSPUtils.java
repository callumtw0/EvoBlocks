package org.example.MemeticAlgorithm;

import java.util.Map;
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
public class TSPUtils {
    public static final String RANDOM_INITIALISATION   = "random_initialisation";
    public static final String NN_INITIALISATION   = "nearest_neighbour_initialisation";
    public static final String HYBRID_INITIALISATION = "hybrid_initialisation";

    public static final String ELITIST_SELECTION = "elitist_selection";
    public static final String RANK_BASED_SELECTION = "rank_based_selection";
    public static final String ROULETTE_WHEEL_SELECTION = "roulette_wheel_selection";
    public static final String TOURNAMENT_SELECTION = "tournament_selection";
    public static final String STOCHASTIC_UNIVERSAL_SAMPLING = "stochastic_universal_sampling";

    public static final String PARTIALLY_MATCHED_CROSSOVER = "partially_mapped_crossover";
    public static final String ORDER_CROSSOVER = "order_crossover";
    public static final String CYCLE_CROSSOVER = "cycle_crossover";
    public static final String EDGE_RECOMBINATION_CROSSOVER = "edge_recombination_crossover";

    public static final String SWAP_MUTATION = "swap_mutation";
    public static final String INVERSION_MUTATION = "inversion_mutation";
    public static final String SCRAMBLE_MUTATION = "scramble_mutation";
    public static final String DISPLACEMENT_MUTATION = "displacement_mutation";

    public static final String ELITIST_REPLACEMENT = "elitist_replacement";
    public static final String FITNESS_BASED_REPLACEMENT = "fitness_based_replacement";
    public static final String GENERATIONAL_REPLACEMENT = "generational_replacement";
    public static final String STEADY_STATE_REPLACEMENT = "steady_state_replacement";

    public static final String TWO_OPTION_LOCAL_SEARCH = "two_option_local_search";
    public static final String THREE_OPTION_LOCAL_SEARCH = "three_option_local_search";
    public static final String GREEDY_DESCENT_LOCAL_SEARCH = "greedy_descent_local_search";
    public static final String GUIDED_LOCAL_SEARCH = "guided_local_search";


    public static double[][] computeDistanceMatrix(Map<Integer, double[]> nodes) {
        int numCities = nodes.size();
        double[][] distanceMatrix = new double[numCities][numCities];

        for (int i = 0; i < numCities; i++) {
            double[] city1 = nodes.get(i + 1); // Assuming nodes are 1-indexed
            for (int j = 0; j < numCities; j++) {
                double[] city2 = nodes.get(j + 1);
                if (i == j) {
                    distanceMatrix[i][j] = 0; // Distance to itself is zero
                } else {
                    distanceMatrix[i][j] = euclideanDistance(city1, city2);
                }
            }
        }

        return distanceMatrix;
    }


    private static double euclideanDistance(double[] point1, double[] point2) {
        double dx = point1[0] - point2[0];
        double dy = point1[1] - point2[1];
        return Math.sqrt(dx * dx + dy * dy);

    }
}