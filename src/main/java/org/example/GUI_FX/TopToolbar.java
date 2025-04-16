package org.example.GUI_FX;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.MemeticAlgorithm.Tester;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
public class TopToolbar {
    WorkspacePanel workspacePanel;
    List<HeuristicData> heuristics;
    Tester tester;
    RightPanel rightPanel;
    private ComboBox<String> tspFileDropdown;
    private ComboBox<Integer> generationsDropdown;
    private ComboBox<Integer> populationSizeDropdown;

    private static final String EMPTY = "empty";
    private static final String INTITIALISATION = "initialisation";
    private static final String REPLACEMENT = "replacement";
    private static final String SELECTION = "selection";
    private static final String CROSSOVER = "crossover";
    private static final String PROBLEM = "problem";
    private static final String GENERATIONS = "generations";
    private static final String POPULATION = "population";

    // Fixed filenames
    private static final String HEURISTIC_FILE = "last_heuristic_list.ser";
    private static final String GRAPH_FILE = "last_graph_data.ser";

    public TopToolbar(WorkspacePanel workspacePanel, List<HeuristicData> heuristics, RightPanel rightPanel) {
        this.workspacePanel = workspacePanel;
        this.heuristics = heuristics;
        this.rightPanel = rightPanel;
        tester = new Tester(rightPanel);
    }

    public HBox getToolbar() {
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setStyle("-fx-background-color: #0A3D5F;");

        Button runButton = new Button("RUN");
        runButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-weight: bold;");
        runButton.setMinWidth(100);

        Button pausePlayButton = new Button("Pause / Resume");
        pausePlayButton.setStyle("-fx-background-color: #d93939; -fx-text-fill: white; -fx-font-weight: bold; fx-padding: 5px 10px; -fx-scale-x: 1.1");
        pausePlayButton.setMinWidth(100);

        Button instructionsButton = new Button("Instructions");
        instructionsButton.setStyle("-fx-background-color: #F4D03F; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5px 10px;");
        instructionsButton.setMinWidth(150);
        instructionsButton.setOnAction(e -> showInstructionsPopup());

        // Information Button
        Button informationButton = new Button("Information");
        informationButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-weight: bold; fx-padding: 5px 10px;");
        informationButton.setMinWidth(140);
        informationButton.setOnAction(e -> showInformationPopup());




        // TSP File Dropdown with Label, Description, and Help Icon
        HBox tspBox = new HBox(5);
        Label tspLabel = new Label("TSP File:");
        tspLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        tspFileDropdown = new ComboBox<>();
        tspFileDropdown.setPromptText("Select .tsp file");
        tspFileDropdown.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black;");
        populateTspFiles();
        Button tspHelpButton = new Button("?");
        tspHelpButton.setStyle("-fx-background-color: #666; -fx-text-fill: white; -fx-font-size: 10px; -fx-pref-width: 20px; -fx-pref-height: 20px;");
        tspHelpButton.setOnAction(e -> showInstructionsPopup());
        tspBox.getChildren().addAll(tspLabel, tspFileDropdown, tspHelpButton);

        Label tspDescription = new Label("Select the TSP problem to solve (e.g., berlin52.tsp)");
        tspDescription.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 11px;");
        tspDescription.setWrapText(true);

        // Generations Dropdown with Label, Description, and Help Icon
        HBox generationsBox = new HBox(5);
        Label generationsLabel = new Label("Generations:");
        generationsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        generationsDropdown = new ComboBox<>();
        generationsDropdown.setPromptText("Generations");
        generationsDropdown.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black;");
        generationsDropdown.getItems().addAll(2000, 5000, 10000, 20000, 50000);
        generationsDropdown.setValue(5000);
        Button generationsHelpButton = new Button("?");
        generationsHelpButton.setStyle("-fx-background-color: #666; -fx-text-fill: white; -fx-font-size: 10px; -fx-pref-width: 20px; -fx-pref-height: 20px;");
        generationsHelpButton.setOnAction(e -> showInstructionsPopup());
        generationsBox.getChildren().addAll(generationsLabel, generationsDropdown, generationsHelpButton);

        Label generationsDescription = new Label("Number of iterations (higher = better results, slower)");
        generationsDescription.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 11px;");
        generationsDescription.setWrapText(true);

        // Population Size Dropdown with Label, Description, and Help Icon
        HBox populationBox = new HBox(5);
        Label populationLabel = new Label("Population Size:");
        populationLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        populationSizeDropdown = new ComboBox<>();
        populationSizeDropdown.setPromptText("Population Size");
        populationSizeDropdown.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black;");
        populationSizeDropdown.getItems().addAll(50, 100, 200, 500, 1000);
        populationSizeDropdown.setValue(100);
        Button populationHelpButton = new Button("?");
        populationHelpButton.setStyle("-fx-background-color: #666; -fx-text-fill: white; -fx-font-size: 10px; -fx-pref-width: 20px; -fx-pref-height: 20px;");
        populationHelpButton.setOnAction(e -> showInstructionsPopup());
        populationBox.getChildren().addAll(populationLabel, populationSizeDropdown, populationHelpButton);

        Label populationDescription = new Label("Number of solutions per generation (higher = more diverse, slower)");
        populationDescription.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 11px;");
        populationDescription.setWrapText(true);

        // Group 3: File Operations (Load, Save, Save Graph, Load Graph, Compare Graphs)
        HBox fileGroup = new HBox(10);
        fileGroup.setPadding(new Insets(5));
        fileGroup.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1; -fx-border-radius: 5;");

        Button loadButton = new Button("LOAD");
        loadButton.setStyle("-fx-background-color: #777; -fx-text-fill: white;");

        Button saveButton = new Button("SAVE");
        saveButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;");

        Button saveGraphButton = new Button("Save Graph");
        saveGraphButton.setStyle("-fx-background-color: #F7C87A; -fx-text-fill: white;");

        Button loadGraphButton = new Button("Load Graph");
        loadGraphButton.setStyle("-fx-background-color: #F4A3C0; -fx-text-fill: white;");

        Button compareGraphsButton = new Button("Compare Graphs");
        compareGraphsButton.setStyle("-fx-background-color: #E07BAE; -fx-text-fill: white;");

        fileGroup.getChildren().addAll(loadButton, saveButton, saveGraphButton, loadGraphButton, compareGraphsButton);

        // Add separators between groups
        Separator separator1 = new Separator(Orientation.VERTICAL);
        separator1.setStyle("-fx-background-color: #FFFFFF;");
        Separator separator2 = new Separator(Orientation.VERTICAL);
        separator2.setStyle("-fx-background-color: #FFFFFF;");

        pausePlayButton.setOnAction(e -> {
            if (tester.isRunning()) {
                if (tester.isPaused()) {
                    tester.resume();
                    pausePlayButton.setText("Pause");
                } else {
                    tester.pause();
                    pausePlayButton.setText("Resume");
                }
            }
        });

        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tester.stop(); // Stop any running task
                rightPanel.resetGraph(); // Reset graphs
                List<Map.Entry<Integer, Map<String, Double>>> heuristicsWithParams = workspacePanel.returnHeuristics();
                List<HeuristicData> selectedHeuristics = new ArrayList<>();
                List<Map<String, Double>> indexedParams = new ArrayList<>();
                for (Map.Entry<Integer, Map<String, Double>> entry : heuristicsWithParams) {
                    if (entry.getKey() == null) continue;
                    selectedHeuristics.add(heuristics.get(entry.getKey()));
                    indexedParams.add(entry.getValue());
                }
                if (selectedHeuristics.isEmpty()) {
                    showInvalidAlgorithmPopup(EMPTY);
                    return;
                }
                if (!Objects.equals(selectedHeuristics.getFirst().getCategory(), "Initialisation")) {
                    showInvalidAlgorithmPopup(INTITIALISATION);
                    return;
                }
                else if (!Objects.equals(selectedHeuristics.get(1).getCategory(), "Replacement")) {
                    showInvalidAlgorithmPopup(REPLACEMENT);
                    return;
                }
                for (int i = 1; i < selectedHeuristics.size(); i++) {
                    if (Objects.equals(selectedHeuristics.get(i).getCategory(), "Selection")) {
                        if (i == (selectedHeuristics.size() - 1) || !Objects.equals(selectedHeuristics.get(i + 1).getCategory(), "Crossover")) {
                            showInvalidAlgorithmPopup(SELECTION);
                            return;
                        }
                    }
                    if (Objects.equals(selectedHeuristics.get(i).getCategory(), "Crossover")) {
                        if (i == 1 || !Objects.equals(selectedHeuristics.get(i - 1).getCategory(), "Selection")) {
                            showInvalidAlgorithmPopup(CROSSOVER);
                            return;
                        }
                    }
                }
                // Get selected .tsp file
                String selectedTspFile = tspFileDropdown.getValue();
                if (selectedTspFile == null || selectedTspFile.isEmpty()) {
                    showInvalidAlgorithmPopup(PROBLEM);
                    return;
                }
                // Get selected number of generations
                Integer numGenerations = generationsDropdown.getValue();
                if (numGenerations == null) {
                    showInvalidAlgorithmPopup(GENERATIONS);
                    return;
                }

                // Get selected population size
                Integer populationSize = populationSizeDropdown.getValue();
                if (populationSize == null) {
                    showInvalidAlgorithmPopup(POPULATION);
                    return;
                }

                System.out.println("Running heuristic with .tsp file: " + selectedTspFile);

                try {
                    tester.runForIDE(selectedHeuristics, selectedTspFile, numGenerations, populationSize, indexedParams);
                } catch (IOException e) {
                    showErrorPopup("Error running algorithm: " + e.getMessage());
                }
                tester.resume();
            }
        });

//        saveButton.setOnAction(e -> saveCurrentState());
        saveGraphButton.setOnAction(e -> saveGraph());
//        loadButton.setOnAction(e -> loadCurrentState());
        compareGraphsButton.setOnAction(e -> compareGraphs());

//        toolbar.getChildren().addAll(runButton, instructionsButton, loadButton, saveButton, saveGraphButton, compareGraphsButton);
        toolbar.getChildren().addAll(runButton, pausePlayButton, tspLabel, tspFileDropdown, generationsLabel, generationsDropdown, populationLabel, populationSizeDropdown, instructionsButton, informationButton);
        return toolbar;
    }
//boobies
    // Method to populate the ComboBox with .tsp files
private void populateTspFiles() {
    try {
        // Clear any existing items in the dropdown
        tspFileDropdown.getItems().clear();

        // Path to the Problems directory in resources (without leading slash for getResource)
        String resourcePath = "Problems";
        java.net.URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);

        if (resourceUrl == null) {
            tspFileDropdown.getItems().add("No .tsp files found (directory not found)");
            return;
        }

        // Determine if we're running from a JAR or the IDE
        String protocol = resourceUrl.getProtocol();
        List<String> tspFiles = new ArrayList<>();

        if ("jar".equals(protocol)) {
            // Running from a JAR
            String jarPath = resourceUrl.getPath().substring(0, resourceUrl.getPath().indexOf("!"));
            try (JarFile jar = new JarFile(new File(new URI(jarPath)))) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    // Look for files in the Problems directory that end with .tsp
                    if (name.startsWith(resourcePath + "/") && name.toLowerCase().endsWith(".tsp")) {
                        // Extract just the file name (e.g., "berlin52.tsp")
                        String fileName = name.substring(name.lastIndexOf("/") + 1);
                        tspFiles.add(fileName);
                    }
                }
            }
        } else if ("file".equals(protocol)) {
            // Running from the IDE or a filesystem
            File tspDir = new File(resourceUrl.toURI());
            if (tspDir.exists() && tspDir.isDirectory()) {
                File[] files = tspDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".tsp"));
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        tspFiles.add(file.getName());
                    }
                }
            }
        }

        // Populate the ComboBox
        if (tspFiles.isEmpty()) {
            tspFileDropdown.getItems().add("No .tsp files found in directory");
        } else {
            tspFileDropdown.getItems().addAll(tspFiles);
        }

    } catch (Exception e) {
        tspFileDropdown.getItems().add("Error loading .tsp files: " + e.getMessage());
        e.printStackTrace();
    }
}


    private void saveGraph() {
        rightPanel.saveGraph(GRAPH_FILE.replace(".ser", ""));
        showInfoPopup("Graphs saved to " + GRAPH_FILE);
    }

    private void compareGraphs() {
        // Load the last saved graph data for comparison
        rightPanel.loadGraph(GRAPH_FILE.replace(".ser", ""));
        // Compare with current data
        rightPanel.compareGraphs();
        showInfoPopup("Comparing current graphs with last saved data from " + GRAPH_FILE);
    }

    private void showInvalidAlgorithmPopup(String errorType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Algorithm");
        alert.setHeaderText(null);

        switch (errorType) {
            case EMPTY:
                alert.setContentText("Empty Algorithm");
                break;
            case INTITIALISATION:
                alert.setContentText("The designed algorithm is invalid as an Initialisation heuristic is needed at the start");
                break;
            case REPLACEMENT:
                alert.setContentText("The designed algorithm is invalid as a Replacement heuristic needed at the end");
                break;
            case SELECTION:
                alert.setContentText("The designed algorithm is invalid as a Selection heuristic must be followed by a Crossover heuristic");
                break;
            case CROSSOVER:
                alert.setContentText("The designed algorithm is invalid as a Selection heuristic must come before a Crossover heuristic");
                break;
            case PROBLEM:
                alert.setContentText("No problem has been selected");
                break;
            case GENERATIONS:
                alert.setContentText("No number of generations selected");
                break;
            case POPULATION:
                alert.setContentText("No population size selected");
                break;
        }
        alert.showAndWait();
    }

    private void showInstructionsPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("How to Use the Application");
        TextArea textArea = new TextArea(
                "1. Construct a Memetic or Genetic Algorithm by combining heuristics in the workspace panel on the left. This is done by selecting a category in the bottom left and dragging the chosen heuristic onto the workspace. A description about each can be viewed in the bottom right by clicking on a heuristic image.\n" +
                        "2. Select a .tsp file from the 'TSP File' dropdown to define the Traveling Salesman Problem instance to solve.\n" +
                        "   - Example files include berlin52.tsp (52 cities) and kroA100.tsp (100 cities).\n" +
                        "3. Choose the number of generations from the 'Generations' dropdown.\n" +
                        "   - This determines how many iterations the algorithm will run. Higher values may improve results but increase runtime.\n" +
                        "   - Options: 2000, 5000, 10000, 20000, 50000 (default: 5000).\n" +
                        "4. Choose the population size from the 'Population Size' dropdown.\n" +
                        "   - This sets the number of solutions (individuals) in each generation. Larger populations increase diversity but require more computation.\n" +
                        "   - Options: 50, 100, 200, 300, 500 (default: 100).\n" +
                        "5. Click 'Run' to start the algorithm with the selected .tsp file, generations, and population size. The algorithm can be paused and resumed with the pause / resume button.\n" +
                        "6. View the performance of the algorithm using the graphs on the right side of the screen.\n" +
                        "\n\n**Algorithm Requirements:**\n" +
                        "- The algorithm must include an Initialisation heuristic at the start and a Replacement heuristic.\n" +
                        "- Selection and Crossover heuristics must be placed in a pair: A Selection immediately followed by a Crossover.\n" +
                        "\n**Tips:**\n" +
                        "- For smaller problems (e.g., berlin52.tsp), 100 generations and a population of 100 are often sufficient.\n" +
                        "- For larger problems (e.g., a280.tsp), you may need 500+ generations and a population of 200+ for better results."
        );
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(300);

        alert.getDialogPane().setContent(textArea);
        alert.getDialogPane().setMinWidth(450);
        alert.getDialogPane().setMinHeight(250);
        alert.showAndWait();
    }

    private void showInformationPopup(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Details about Memetic Algorithms and the Travelling Salesman Problem");
        TextArea textArea = new TextArea("A Memetic Algorithm is a form of problem optimisation which simulates evolution and the principle of 'Survival of the Fittest' through a sequence of heuristics. "+
                "This involves generating a series of potential solutions to the chosen problem via a generation heuristic to form an initial generation. "+
                "The rest of the heuristic are repeated each time to form a new generation but how they affect the population depends on the position of the selection heuristics."+
                "A Mutation heuristic performs a random change to a solution to explore more of the solution space. " +
                "The chance of a mutation being performed on a solution when the heuristic is run is decided by the mutation rate. " +
                "A Local Search heuristic performs iterative changes on the solutions, keeping the changes if it is an improvement and continuing until no improvements can be found. Every time the heuristic is run, there is a 20% chance of it applying to the solution. " +
                "A Selection heuristic selects solutions to act as parents, being used to create a new solution as an offspring. " +
                "A Crossover heuristic generates a new offspring solution from the selected parents by combining the genetic material through different methods. " +
                "Finally, the Replacement heuristic creates a new generation, potentially utilising members of the old generation and the new offspring population. " +
                "\n\n Before a Selection heuristic the Mutation and Local Search heuristics are simply applied to the entire population. " +
                "The first Selection heuristic starts a loop that creates an offspring population that is the same size as the parent generation so that the replacement can be applied to both. " +
                "A Selection heuristic must always be paired with (and thus followed by) a crossover heuristic as they work together to generate offspring. " +
                "That first selection heuristic generates two parents and these are combined using the crossover heuristic to create an offspring. " +
                "This offspring is then passed through the following heuristics, so Mutation and Local search are applied only to that offspring. " +
                "If another selection heuristic, this would select a single parent from the previous generation to be combined with the candidate offspring in the subsequent Crossover heuristic." +
                "This loops through this heuristic loop until the Replacement heuristic. " +
                "\n\n\n The problems the algorithms work to solve are all Travelling Salesman Problems, meaning that they provide a set of cities placed at various coordinates. " +
                "The algorithm must then find the shortest tour to visit all of the cities exactly once and return to the start. " +
                "The performance of the generated solution is correlated directly to the distance of the tour (being one over the value). " +
                "The best solution in the current generation can be viewed with the tabs on the right side of the screen alongside the standard deviation, convergence rate and fitness/distance values over the generations. " +
                "Information about the heuristics is available in the bottom right by clicking on the desired heuristic.\n\n\n"+
                "EvoBlocks is licensed under the MIT License.\n\n" +
                "Copyright (c) 2025 Callum Welsford\n\n" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy " +
                "of this software and associated documentation files (the \"Software\"), to deal " +
                "in the Software without restriction, including without limitation the rights " +
                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell " +
                "copies of the Software, and to permit persons to whom the Software is " +
                "furnished to do so, subject to the following conditions:\n\n" +
                "The above copyright notice and this permission notice shall be included in all " +
                "copies or substantial portions of the Software.\n\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR " +
                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, " +
                "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE " +
                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER " +
                "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, " +
                "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE " +
                "SOFTWARE.\n"+
                "\n\nThird-Party Libraries:\n" +
                "- JavaFX: Licensed under the GNU General Public License v2 with the Classpath Exception (GPLv2+CE).\n" +
                "  See https://openjdk.java.net/legal/gplv2+ce.html for details.\n" +
                "- JFreeChart: Licensed under the GNU Lesser General Public License v3 (LGPLv3).\n" +
                "  See https://www.gnu.org/licenses/lgpl-3.0.html for details. Source code available at http://www.jfree.org/jfreechart/."
        );
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(300);

        alert.getDialogPane().setContent(textArea);
        alert.getDialogPane().setMinWidth(450);
        alert.getDialogPane().setMinHeight(250);
        alert.showAndWait();
    }

    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void stop(){
        tester.stop();
    }
}