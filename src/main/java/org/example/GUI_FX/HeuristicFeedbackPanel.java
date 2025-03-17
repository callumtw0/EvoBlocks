package org.example.GUI_FX;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HeuristicFeedbackPanel {
    public VBox getPanel() {
        VBox heuristicFeedback = new VBox();
        heuristicFeedback.setStyle("-fx-background-color: #9DD9F3; -fx-border-color: black;");
        heuristicFeedback.setAlignment(Pos.CENTER);
        Label heuristicFeedbackLabel = new Label("Heuristic Feedback");
        heuristicFeedbackLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        heuristicFeedback.getChildren().add(heuristicFeedbackLabel);
        return heuristicFeedback;
    }
}
