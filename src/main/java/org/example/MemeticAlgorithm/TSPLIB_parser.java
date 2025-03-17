package org.example.MemeticAlgorithm;

import java.io.*;
import java.util.*;

public class TSPLIB_parser {
    static final String problemPath = "Problems/";
    static final String solutionPath = "Solutions/";
    public double optimalDistance;

    public Map<Integer, double[]> parse(String filepath) throws IOException {
        // Load the file
        InputStream inputStream = TSPLIB_parser.class.getClassLoader().getResourceAsStream(problemPath + filepath);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource file not found: " + filepath);
        }


        Map<Integer, double[]> nodes = new LinkedHashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        boolean pastInfo = false;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("NODE_COORD_SECTION")) {
                pastInfo = true;
                continue;
            }
            if (line.equals("EOF")) {
                break;
            }
            if (pastInfo) {
                String[] parts = line.split("\\s+");
                int id = Integer.parseInt(parts[0]);
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                nodes.put(id, new double[]{x, y});
            }
        }
        reader.close();
        return nodes;
    }

    public double parseOptimalTour(String selectedTspFile, double[][] distanceMatrix) throws IOException {
        List<Integer> tour = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(TSPLIB_parser.class.getClassLoader().getResourceAsStream(solutionPath + selectedTspFile.replace(".tsp", ".opt.tour")))))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("TOUR_SECTION")) {
                    // Read tour until -1
                    while ((line = br.readLine()) != null && !line.trim().equals("-1")) {
                        try {
                            int city = Integer.parseInt(line.trim());
                            if (city > 0) { // Skip invalid or negative indices
                                tour.add(city);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid tour index: " + line);
                        }
                    }
                    break; // No need to read further once we have the tour
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (tour.isEmpty()) {
            throw new IOException("Failed to parse valid tour from " + selectedTspFile);
        }

        double distance = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int city1 = tour.get(i);
            int city2 = tour.get(i + 1);
            distance += distanceMatrix[city1 - 1][city2 - 1]; // Adjust for 1-based indexing
        }
        distance += distanceMatrix[tour.get(tour.size() - 1) - 1][tour.get(0) - 1]; // Close the loop
        return distance;


    }
}
