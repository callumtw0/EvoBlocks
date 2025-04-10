package org.example.GUI_FX;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
public class HeuristicDescriptionPanel {
    private VBox descriptionPanel;
    private Label descriptionLabel;
    private Label warningLabel;
    List<HeuristicData> heuristics;

    public HeuristicDescriptionPanel(List<HeuristicData> heuristics) {
        this.heuristics = heuristics;
        descriptionPanel = new VBox(10);
        descriptionPanel.setStyle("-fx-background-color: #A8D5BA; -fx-border-color: black;");
        descriptionLabel = new Label("Heuristic Description: Select a heuristic to see details.");
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-padding: 1px;");
        descriptionLabel.setWrapText(true);
        warningLabel = new Label();
        warningLabel.setStyle("-fx-font-size: 14px; -fx-padding: 1px;");
        warningLabel.setTextFill(Color.RED);
        descriptionPanel.getChildren().add(descriptionLabel);
    }

    public VBox getPanel() {
        return descriptionPanel;
    }

    public void updateDescription(int heuristicID) {
        HeuristicData heuristicData = heuristics.get(heuristicID);
        if (Objects.equals(heuristicData.getCategory(), "Initialisation")) {
            warningLabel.setText("An Initialisation Heuristic is required at the start of every algorithm");
            if (!descriptionPanel.getChildren().contains(warningLabel)) {
                descriptionPanel.getChildren().addFirst(warningLabel);
            }
        } else {
            descriptionPanel.getChildren().remove(warningLabel);
        }
        descriptionLabel.setText("Selected Heuristic: " + heuristicData.getDisplayName() + "\nDescription: " + heuristicData.getDescription());
    }
}

