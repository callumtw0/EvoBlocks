package org.example.GUI_FX;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
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

public class MainApp extends Application {

    private RightPanel rightPanel;
    private TopToolbar topToolbar;
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("EvoBlocks - Evolutionary Algorithm Tool");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Icon/cubes.png")));
        primaryStage.getIcons().add(icon);
        HeuristicParser heuristicParser = new HeuristicParser();
        List<HeuristicData> heuristics = heuristicParser.parse();
        System.out.println(heuristics.getFirst().toString());

        // Main Layout: GridPane
        GridPane mainLayout = new GridPane();
        mainLayout.setPadding(new Insets(0));
        mainLayout.setHgap(0);
        mainLayout.setVgap(5);

        GridPane topLayout = new GridPane();
        topLayout.setPadding(new Insets(5));
        topLayout.setHgap(4);
        topLayout.setVgap(5);

        GridPane bottomLayout = new GridPane();
        bottomLayout.setPadding(new Insets(5));
        bottomLayout.setHgap(4);
        bottomLayout.setVgap(5);

        // Column Constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(60);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
        col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(40);
        col3.setHgrow(Priority.ALWAYS);

        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(60);
        col4.setHgrow(Priority.ALWAYS);


        // Row Constraints
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(80);
        row1.setVgrow(Priority.ALWAYS);

        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(20);
        row2.setVgrow(Priority.ALWAYS);

        mainLayout.getRowConstraints().addAll(row1, row2);

        bottomLayout.getColumnConstraints().addAll(col1, col2);

        topLayout.getColumnConstraints().addAll(col3, col4);

        // Attach UI Components
        rightPanel = new RightPanel();
        HeuristicDescriptionPanel heuristicDescriptionPanel = new HeuristicDescriptionPanel(heuristics);
        WorkspacePanel workspacePanel = new WorkspacePanel(heuristicDescriptionPanel, heuristics);
        BottomPanel bottomPanel = new BottomPanel(workspacePanel, heuristicDescriptionPanel, heuristics); // Drag support added


        // Place Elements in Grid
        topLayout.add(workspacePanel.getPanel(), 0, 0);
        topLayout.add(rightPanel.getPanel(), 1, 0);
        bottomLayout.add(bottomPanel.getPanel(), 0, 0);
        bottomLayout.add(heuristicDescriptionPanel.getPanel(), 1, 0);
        mainLayout.add(topLayout,0,0);
        mainLayout.add(bottomLayout,0,1);


        // Root BorderPane
        BorderPane root = new BorderPane();
        topToolbar = new TopToolbar(workspacePanel, heuristics, rightPanel);
        root.setTop(topToolbar.getToolbar());
        root.setCenter(mainLayout);

        // Scene & Resizing
        Scene scene = new Scene(root, 1200, 760);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        if (rightPanel != null) {
            rightPanel.close();
        }
        topToolbar.stop();
        super.stop();
    }
}

