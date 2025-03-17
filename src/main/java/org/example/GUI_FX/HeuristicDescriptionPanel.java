package org.example.GUI_FX;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;

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

