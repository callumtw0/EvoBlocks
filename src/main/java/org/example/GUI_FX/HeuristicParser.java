package org.example.GUI_FX;

import org.example.MemeticAlgorithm.TSPLIB_parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HeuristicParser {
    public List<HeuristicData> parse() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(TSPLIB_parser.class.getClassLoader().getResourceAsStream("Data/heuristics"))));
        if (reader == null) {
            throw new FileNotFoundException("Resource file not found: ");
        }
        String line;
        String name = "", displayName = "", category = "", description = "";
        List<HeuristicData> heuristics = new ArrayList<>();
        int counter = 0;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.startsWith("name:")) {
                name = line.split(":")[1].trim();
            } else if (line.startsWith("displayName:")) {
                displayName = line.split(":")[1].trim();
            } else if (line.startsWith("category:")) {
                category = line.split(":")[1].trim();
            } else if (line.startsWith("description:")) {
                description = line.split(":")[1].trim();
            }

            // When we have all fields, create and store the heuristic object
            if (!name.isEmpty() && !displayName.isEmpty() && !category.isEmpty() && !description.isEmpty()) {
                heuristics.add(new HeuristicData(counter++, name, displayName, category, description));
                name = displayName = category = description = ""; // Reset for the next heuristic
            }
        }
        return heuristics;

    }
}
