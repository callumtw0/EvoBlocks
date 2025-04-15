package org.example.GUI_FX;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;

import java.util.*;
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
public class WorkspacePanel {
    private GridPane workspaceGrid;
    private GridPane initialisationSpace;
    private GridPane replacementSpace;
    private int rowIndex = 0;
    private long instanceCounter = 0; // Counter for generating unique instance IDs
    private HeuristicDescriptionPanel heuristicDescriptionPanel;
    private List<HeuristicData> heuristics;
    private VBox layout;
    private Pane arrowOverlay;
    private Map<Long, Map<String, Double>> heuristicParameters; // Map to store heuristic parameters with instance ID
    private Map<Long, Integer> instanceToHeuristicMap; // Map to store instance ID to heuristic ID mapping

    public WorkspacePanel(HeuristicDescriptionPanel heuristicDescriptionPanel, List<HeuristicData> heuristics) {
        this.heuristics = heuristics;
        this.heuristicDescriptionPanel = heuristicDescriptionPanel;
        this.heuristicParameters = new HashMap<>();
        this.instanceToHeuristicMap = new HashMap<>();

        // Initialize workspaceGrid
        workspaceGrid = new GridPane();
        workspaceGrid.setStyle("-fx-background-color: #F4F4F4; -fx-border-color: black;");
        workspaceGrid.setPadding(new Insets(10));
        workspaceGrid.setHgap(5);
        workspaceGrid.setVgap(6);
        workspaceGrid.setPadding(new Insets(2, 5, 1, 5));
        workspaceGrid.setPrefWidth(6000);
        workspaceGrid.setAlignment(Pos.TOP_CENTER);

        // Initialize initialisationSpace
        initialisationSpace = new GridPane();
        initialisationSpace.setStyle("-fx-background-color: #F4F4F4; -fx-border-color: black;");
        initialisationSpace.setPadding(new Insets(10));
        initialisationSpace.setHgap(5);
        initialisationSpace.setVgap(5);
        initialisationSpace.setPadding(new Insets(3, 5, 3, 5));
        initialisationSpace.setPrefWidth(6000);
        initialisationSpace.setAlignment(Pos.TOP_CENTER);

        // Initialize replacementSpace
        replacementSpace = new GridPane();
        replacementSpace.setStyle("-fx-background-color: #F4F4F4; -fx-border-color: black;");
        replacementSpace.setPadding(new Insets(10));
        replacementSpace.setHgap(5);
        replacementSpace.setVgap(0);
        replacementSpace.setPadding(new Insets(2, 5, 2, 5));
        replacementSpace.setPrefWidth(6000);
        replacementSpace.setAlignment(Pos.TOP_CENTER);

        // Add placeholders
        Label initialisationPlaceholder = new Label("Drop Initialisation Here (Required to run algorithm)");
        initialisationPlaceholder.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: center;");
        initialisationPlaceholder.setMaxWidth(Double.MAX_VALUE);
        initialisationPlaceholder.setMinHeight(48);
        initialisationPlaceholder.setAlignment(Pos.CENTER);
        initialisationSpace.add(initialisationPlaceholder, 0, 0);

        Label replacementPlaceholder = new Label("Drop Replacement Here (Required to run algorithm)");
        replacementPlaceholder.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: center;");
        replacementPlaceholder.setMaxWidth(Double.MAX_VALUE);
        replacementPlaceholder.setMinHeight(48);
        replacementPlaceholder.setAlignment(Pos.CENTER);
        replacementSpace.add(replacementPlaceholder, 0, 0);

        // Predefine rows with placeholders in workspaceGrid
        for (int i = 0; i < 8; i++) {
            RowConstraints row = new RowConstraints(50); // Original height
            workspaceGrid.getRowConstraints().add(row);

            Label placeholder = new Label("Drop Heuristic Here");
            placeholder.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: center;");
            placeholder.setMaxWidth(Double.MAX_VALUE);
            placeholder.setMinHeight(48);
            placeholder.setAlignment(Pos.CENTER);
            workspaceGrid.add(placeholder, 0, i);
        }

        // Predefine rows for initialisationSpace and replacementSpace
        RowConstraints initRow = new RowConstraints(50);
        initialisationSpace.getRowConstraints().add(initRow);
        RowConstraints replaceRow = new RowConstraints(50);
        replacementSpace.getRowConstraints().add(replaceRow);

        // Column constraints
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        workspaceGrid.getColumnConstraints().add(col);
        initialisationSpace.getColumnConstraints().add(col);
        replacementSpace.getColumnConstraints().add(col);

        // Arrow overlay for workspaceGrid
        arrowOverlay = new Pane();
        arrowOverlay.setStyle("-fx-background-color: transparent;");
        arrowOverlay.setMouseTransparent(true);
        arrowOverlay.prefWidthProperty().bind(workspaceGrid.widthProperty());
        arrowOverlay.prefHeightProperty().bind(workspaceGrid.heightProperty());

        StackPane workspaceStack = new StackPane(workspaceGrid, arrowOverlay);
        workspaceStack.setAlignment(Pos.TOP_CENTER);

        layout = new VBox(0, initialisationSpace, workspaceStack, replacementSpace);
        layout.setAlignment(Pos.CENTER);

        // Drag and drop handling
        layout.setOnDragOver(event -> {
            if (event.getGestureSource() != layout && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        layout.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                int heuristicId = Integer.parseInt(db.getString());
                HeuristicData heuristicData = heuristics.get(heuristicId);
                long instanceId = instanceCounter++; // Generate a unique instance ID
                ImageView heuristicImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/" + heuristicData.getCategory() + "/" + heuristicData.getName() + ".png"))));
                heuristicImage.setFitWidth(104);
                heuristicImage.setFitHeight(48);
                heuristicImage.setUserData(instanceId); // Store instance ID in the image

                heuristicImage.setOnMouseClicked(e -> heuristicDescriptionPanel.updateDescription(heuristicId));

                Button removeButton = new Button("❌");
                removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 1px 4px;");
                removeButton.setPadding(new Insets(0, 0, 0, 10)); // Add 10px left padding to shift it left

                // Create a StackPane for the image (centered)
                StackPane imageWrapper = new StackPane(heuristicImage);
                imageWrapper.setAlignment(Pos.CENTER);

                // Create a GridPane to hold parameters on the left, image in the middle, and remove button on the right
                GridPane heuristicWrapper = new GridPane();
                heuristicWrapper.setAlignment(Pos.CENTER);
                heuristicWrapper.setUserData(instanceId); // Store instance ID in the wrapper
                heuristicWrapper.setHgap(20); // Increased spacing between items to 20px

                // Define columns: left (params), middle (image), right (remove button)
                ColumnConstraints colLeft = new ColumnConstraints();
                colLeft.setPercentWidth(35); // For parameters
                ColumnConstraints colMiddle = new ColumnConstraints();
                colMiddle.setPercentWidth(30); // For image (centered)
                colMiddle.setHgrow(Priority.ALWAYS);
                ColumnConstraints colRight = new ColumnConstraints();
                colRight.setPercentWidth(35); // For remove button
                heuristicWrapper.getColumnConstraints().addAll(colLeft, colMiddle, colRight);

                // Add parameters on the left
                VBox paramsBox = createParametersBox(instanceId, heuristicId, heuristicData);
                paramsBox.setPrefWidth(150); // Increased width for more space
                heuristicWrapper.add(paramsBox, 0, 0);
                GridPane.setHalignment(paramsBox, HPos.LEFT);

                // Add image in the middle
                heuristicWrapper.add(imageWrapper, 1, 0);
                GridPane.setHalignment(imageWrapper, HPos.CENTER);

                // Add remove button on the right, shifted left with padding
                heuristicWrapper.add(removeButton, 2, 0);
                GridPane.setHalignment(removeButton, HPos.RIGHT);

                // Map instance ID to heuristic ID
                instanceToHeuristicMap.put(instanceId, heuristicId);

                removeButton.setOnAction(e -> {
                    if (Objects.equals(heuristicData.getCategory(), "Initialisation")) {
                        initialisationSpace.getChildren().remove(heuristicWrapper);
                        initialisationSpace.getChildren().add(initialisationPlaceholder);
                    } else if (Objects.equals(heuristicData.getCategory(), "Replacement")) {
                        replacementSpace.getChildren().remove(heuristicWrapper);
                        replacementSpace.getChildren().add(replacementPlaceholder);
                    } else {
                        workspaceGrid.getChildren().remove(heuristicWrapper);
                        ObservableList<Node> children = FXCollections.observableArrayList(workspaceGrid.getChildren());
                        rowIndex--;
                        int newIndex = 0;
                        for (Node node : children) {
                            if (node.getUserData() != null) {
                                GridPane.setRowIndex(node, newIndex++);
                            } else {
                                workspaceGrid.getChildren().remove(node);
                            }
                        }
                        for (int i = rowIndex; i < 8; i++) {
                            Label placeholder = new Label("Drop Heuristic Here");
                            placeholder.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: center;");
                            placeholder.setMaxWidth(Double.MAX_VALUE);
                            placeholder.setMinHeight(48);
                            placeholder.setAlignment(Pos.CENTER);
                            workspaceGrid.add(placeholder, 0, i);
                        }
                        updateArrows();
                    }
                    // Remove parameters for this instance
                    heuristicParameters.remove(instanceId);
                    instanceToHeuristicMap.remove(instanceId);
                });

                // Add heuristic to appropriate grid
                if (Objects.equals(heuristicData.getCategory(), "Initialisation")) {
                    int initialisationRow = 0;
                    initialisationSpace.getChildren().removeFirst();
                    initialisationSpace.add(heuristicWrapper, 0, initialisationRow);
                } else if (Objects.equals(heuristicData.getCategory(), "Replacement")) {
                    int replacementRow = 0;
                    replacementSpace.getChildren().removeFirst();
                    replacementSpace.add(heuristicWrapper, 0, replacementRow);
                } else if (rowIndex < 8) {
                    ObservableList<Node> children = FXCollections.observableArrayList(workspaceGrid.getChildren());
                    workspaceGrid.add(heuristicWrapper, 0, rowIndex++);
                    for (Node node : children) {
                        if (node.getClass() == Label.class) {
                            workspaceGrid.getChildren().remove(node);
                        }
                    }
                    for (int i = rowIndex; i < 8; i++) {
                        Label placeholder = new Label("Drop Heuristic Here");
                        placeholder.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: center;");
                        placeholder.setMaxWidth(Double.MAX_VALUE);
                        placeholder.setMinHeight(48);
                        placeholder.setAlignment(Pos.CENTER);
                        workspaceGrid.add(placeholder, 0, i);
                    }
                    updateArrows();
                }
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private VBox createParametersBox(long instanceId, int heuristicId, HeuristicData heuristicData) {
        VBox paramsBox = new VBox(10); // Increased spacing between parameters to 10px
        paramsBox.setAlignment(Pos.CENTER_LEFT);

        Map<String, Double> params = new HashMap<>();
        String category = heuristicData.getCategory();
        String name = heuristicData.getName().toLowerCase();

        // Define parameters based on heuristic type
        if (category.equals("Mutation") || name.contains("mutation")) {
            params.put("mutationRate", 0.05); // Default value
        } else if (category.equals("Selection") && name.contains("tournament")) {
            params.put("tournamentSize", 3.0); // Default value
        } else if ((category.equals("Replacement") && name.contains("elitist"))) {
            params.put("elitismRate", 0.1); // Default value
        } else if (name.contains("local") || name.contains("search")) {
            params.put("localSearchProbability", 0.2); // Default value
        } else if (category.equals("Replacement") && name.contains("steady")) {
            params.put("replacementRate", 0.1); // Default value (fine-tuned to 0.1)
        } else if (category.equals("Initialisation") && name.contains("hybrid")) {
            params.put("hybridRatio", 0.5); // Default value
        }

        if (params.isEmpty()) {
            return paramsBox; // Return empty VBox if no parameters
        }

        heuristicParameters.put(instanceId, params);

        // Add dropdowns for each parameter
        for (Map.Entry<String, Double> entry : params.entrySet()) {
            String paramName = entry.getKey();
            Double defaultValue = entry.getValue();

            VBox paramWrapper = new VBox(2); // Stack label and dropdown vertically
            Label paramLabel = new Label(capitalize(paramName));
            paramLabel.setStyle("-fx-text-fill: #333; -fx-font-size: 11px;");
            ComboBox<Double> paramDropdown = new ComboBox<>();
            paramDropdown.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black; -fx-font-size: 11px;");
            paramDropdown.setPrefWidth(80); // Increased width for readability

            // Set options based on parameter type
            if (paramName.equals("mutationRate")) {
                paramDropdown.getItems().addAll(0.01, 0.05, 0.1, 0.2);
            } else if (paramName.equals("tournamentSize")) {
                paramDropdown.getItems().addAll(2.0, 3.0, 5.0, 7.0);
            } else if (paramName.equals("elitismRate")) {
                paramDropdown.getItems().addAll(0.02, 0.05, 0.1, 0.2);
            } else if (paramName.equals("localSearchProbability")) {
                paramDropdown.getItems().addAll(0.1, 0.2, 0.5, 1.0);
            } else if (paramName.equals("replacementRate")) {
                paramDropdown.getItems().addAll(0.05, 0.1, 0.15, 0.2, 0.25); // Fine-tuned values
            } else if (paramName.equals("hybridRatio")) {
                paramDropdown.getItems().addAll(0.0, 0.25, 0.5, 0.75, 1.0);
            }

            paramDropdown.setValue(defaultValue);
            paramDropdown.setOnAction(e -> {
                params.put(paramName, paramDropdown.getValue());
                heuristicParameters.put(instanceId, params);
            });

            Button helpButton = new Button("?");
            helpButton.setStyle("-fx-background-color: #666; -fx-text-fill: white; -fx-font-size: 10px; -fx-pref-width: 18px; -fx-pref-height: 18px;");
            helpButton.setOnAction(e -> showParameterHelp(paramName));

            HBox dropdownBox = new HBox(5, paramDropdown, helpButton); // Spacing between dropdown and help button
            dropdownBox.setAlignment(Pos.CENTER_LEFT);
            paramWrapper.getChildren().addAll(paramLabel, dropdownBox);
            paramsBox.getChildren().add(paramWrapper);
        }

        return paramsBox;
    }

    private String capitalize(String str) {
        if (str.equals("tournamentSize")){return "Tournament Size (percentage)";}
        return str.substring(0, 1).toUpperCase() + str.substring(1).replaceAll("([A-Z])", " $1");
    }

    private void showParameterHelp(String paramName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Parameter Help");
        alert.setHeaderText(capitalize(paramName));

        String description = switch (paramName) {
            case "mutationRate" -> "The probability of applying a mutation to an individual in the population.\n- Range: 0.01 to 0.2\n- Lower values mean less frequent mutations, higher values increase diversity but may disrupt good solutions.";
            case "tournamentSize" -> "The percentage of individuals competing in a tournament selection.\n- Range: 2% to 7%\n- Smaller sizes increase randomness, larger sizes favor fitter individuals.";
            case "elitismRate" -> "The fraction of the best individuals to carry over to the next generation unchanged.\n- Range: 0.0 to 0.3\n- Higher values preserve good solutions but may reduce diversity.";
            case "localSearchProbability" -> "The probability of applying local search to an individual.\n- Range: 0.1 to 1.0\n- Higher values mean more frequent local optimisation, improving solutions but increasing computation.";
            case "replacementRate" -> "The fraction of the population to replace in each generation for Steady-State Replacement.\n- Range: 0.05 to 0.25\n- Lower values maintain more continuity, higher values introduce more new individuals.";
            case "hybridRatio" -> "The balance between Random Initialisation and Nearest Neighbor Initialisation in Hybrid Initialisation.\n- Range: 0.0 to 1.0\n- 0.0 uses only Random Initialisation, 1.0 uses only Nearest Neighbor, intermediate values blend the two.";
            default -> "No description available.";
        };

        TextArea textArea = new TextArea(description);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(200);

        alert.getDialogPane().setContent(textArea);
        alert.getDialogPane().setMinWidth(450);
        alert.getDialogPane().setMinHeight(250);
        alert.showAndWait();
    }

    private Map<String, Double> extractParametersFromWrapper(GridPane heuristicWrapper) {
        Map<String, Double> params = new HashMap<>();

        // The paramsBox is the first child (column 0) of the heuristicWrapper GridPane
        for (Node child : heuristicWrapper.getChildren()) {
            if (GridPane.getColumnIndex(child) == 0 && child instanceof VBox) {
                VBox paramsBox = (VBox) child;
                // Iterate through each parameter wrapper in paramsBox
                for (Node paramNode : paramsBox.getChildren()) {
                    if (paramNode instanceof VBox) {
                        VBox paramWrapper = (VBox) paramNode;
                        // The paramWrapper contains a Label and an HBox (with ComboBox and help button)
                        String paramName = null;
                        Double paramValue = null;
                        for (Node paramChild : paramWrapper.getChildren()) {
                            if (paramChild instanceof Label) {
                                paramName = ((Label) paramChild).getText().replace(" ", "");
                                paramName = paramName.replace("(percentage)","");
                            } else if (paramChild instanceof HBox) {
                                HBox dropdownBox = (HBox) paramChild;
                                for (Node dropdownChild : dropdownBox.getChildren()) {
                                    if (dropdownChild instanceof ComboBox) {
                                        @SuppressWarnings("unchecked")
                                        ComboBox<Double> paramDropdown = (ComboBox<Double>) dropdownChild;
                                        paramValue = paramDropdown.getValue();
                                    }
                                }
                            }
                        }
                        if (paramName != null && paramValue != null) {
                            params.put(paramName, paramValue);
                        }
                    }
                }
            }
        }

        return params;
    }

    public List<Map.Entry<Integer, Map<String, Double>>> returnHeuristics() {
        List<Map.Entry<Integer, Map<String, Double>>> heuristicsWithParams = new ArrayList<>();

        // Initialisation heuristic
        Node initNode = initialisationSpace.getChildren().getFirst();
        if (initNode.getUserData() != null && initNode instanceof GridPane) {
            GridPane heuristicWrapper = (GridPane) initNode;
            long instanceId = (long) heuristicWrapper.getUserData();
            int heuristicId = instanceToHeuristicMap.get(instanceId);
            Map<String, Double> params = extractParametersFromWrapper(heuristicWrapper);
            heuristicsWithParams.add(new AbstractMap.SimpleEntry<>(heuristicId, params));
        }
        // Replacement heuristic
        Node replaceNode = replacementSpace.getChildren().getFirst();
        if (replaceNode.getUserData() != null && replaceNode instanceof GridPane) {
            GridPane heuristicWrapper = (GridPane) replaceNode;
            long instanceId = (long) heuristicWrapper.getUserData();
            int heuristicId = instanceToHeuristicMap.get(instanceId);
            Map<String, Double> params = extractParametersFromWrapper(heuristicWrapper);
            heuristicsWithParams.add(new AbstractMap.SimpleEntry<>(heuristicId, params));
        }

        // Workspace heuristics (in order)
        List<Node> workspaceNodes = new ArrayList<>();
        for (Node node : workspaceGrid.getChildren()) {
            if (node.getUserData() != null) {
                workspaceNodes.add(node);
            }
        }
        workspaceNodes.sort(Comparator.comparingInt(GridPane::getRowIndex));
        for (Node node : workspaceNodes) {
            GridPane heuristicWrapper = (GridPane) node;
            long instanceId = (long) heuristicWrapper.getUserData();
            int heuristicId = instanceToHeuristicMap.get(instanceId);
            Map<String, Double> params = extractParametersFromWrapper(heuristicWrapper);
            heuristicsWithParams.add(new AbstractMap.SimpleEntry<>(heuristicId, params));
        }



        return heuristicsWithParams;
    }

    private void updateArrows() {
        arrowOverlay.getChildren().clear();

        ObservableList<Node> children = workspaceGrid.getChildren();
        List<Node> heuristicNodes = new ArrayList<>();
        for (Node node : children) {
            if (node.getUserData() != null) {
                heuristicNodes.add(node);
            }
        }

        for (int i = 0; i < heuristicNodes.size() - 1; i++) {
            Node startNode = heuristicNodes.get(i);
            Node endNode = heuristicNodes.get(i + 1);

            int startRow = GridPane.getRowIndex(startNode);
            int endRow = GridPane.getRowIndex(endNode);

            double startX = workspaceGrid.getWidth() / 2;
            double endX = startX;
            double startY = (startRow * 57) + 51; // Original spacing
            double endY = (endRow * 57) + 3;

            Line line = new Line(startX, startY, endX, endY);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);

            Polygon arrowHead = new Polygon();
            arrowHead.getPoints().addAll(
                    endX, endY,
                    endX - 5, endY - 5,
                    endX + 5, endY - 5
            );
            arrowHead.setFill(Color.BLACK);

            arrowOverlay.getChildren().addAll(line, arrowHead);
        }

        if (heuristicNodes.size() > 1) {
            Node lastNode = heuristicNodes.get(heuristicNodes.size() - 1);
            Node firstNode = heuristicNodes.get(0);
            int lastRow = GridPane.getRowIndex(lastNode);
            int firstRow = GridPane.getRowIndex(firstNode);

            double startX = workspaceGrid.getWidth() / 2 + 52;
            double startY = (lastRow * 57) + 3 + (48 / 2);
            double endX = workspaceGrid.getWidth() / 2 + 52;
            double endY = (firstRow * 57) + 3 + (48 / 2);
            double controlX = workspaceGrid.getWidth() * 0.9;
            double controlY = ((startY - endY) / 2) + endY;

            QuadCurve nextGenArrow = new QuadCurve(startX, startY, controlX, controlY, endX, endY);
            nextGenArrow.setStroke(Color.BLACK);
            nextGenArrow.setStrokeWidth(2);
            nextGenArrow.setFill(null);

            double dx = endX - controlX;
            double dy = endY - controlY;
            double angle = Math.atan2(dy, dx) * 180 / Math.PI;
            double arrowHeadAngle = angle + 90;

            Polygon nextGenArrowHead = new Polygon();
            nextGenArrowHead.getPoints().addAll(
                    endX, endY,
                    endX + 7 * Math.cos(Math.toRadians(arrowHeadAngle + 40)), endY + 7 * Math.sin(Math.toRadians(arrowHeadAngle + 40)),
                    endX - 7 * Math.cos(Math.toRadians(arrowHeadAngle - 40)), endY - 7 * Math.sin(Math.toRadians(arrowHeadAngle - 40))
            );
            nextGenArrowHead.setFill(Color.BLACK);

            arrowOverlay.getChildren().addAll(nextGenArrow, nextGenArrowHead);
        }
    }

    public VBox getPanel() {
        return layout;
    }

    public Map<Long, Map<String, Double>> getHeuristicParameters() {
        return heuristicParameters;
    }

    public Map<Long, Integer> getInstanceToHeuristicMap() {
        return instanceToHeuristicMap;
    }

    public void setHeuristics(List<Integer> heuristicIds) {
        System.out.println("Updating heuristics");
        // TODO: Implement loading of heuristics and their parameters
    }
}