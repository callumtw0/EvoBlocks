package org.example.GUI_FX;

import org.example.MemeticAlgorithm.TSPLIB_parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
                heuristics.add(new HeuristicData(counter++, name.toUpperCase(), displayName, category, description));
                name = displayName = category = description = ""; // Reset for the next heuristic
            }
        }
        return heuristics;

    }
}
