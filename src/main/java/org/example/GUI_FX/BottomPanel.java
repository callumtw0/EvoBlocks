package org.example.GUI_FX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Objects;

public class BottomPanel {
    private WorkspacePanel workspace;
    private HeuristicDescriptionPanel heuristicDescriptionPanel;
    private Text floatingHeuristic = new Text(); // Floating heuristic label
    List<HeuristicData> heuristics;

    public BottomPanel(WorkspacePanel workspace, HeuristicDescriptionPanel heuristicDescriptionPanel, List<HeuristicData> heuristics) {
        this.workspace = workspace;
        this.heuristicDescriptionPanel = heuristicDescriptionPanel;
        this.heuristics = heuristics;
    }

    public VBox getPanel() {
        VBox bottomPanel = new VBox(10);
        VBox.setVgrow(bottomPanel, Priority.ALWAYS);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.setStyle("-fx-background-color: #E0E0E0; -fx-border-color: black;");

        TabPane heuristicCategories = getTabPane();

        Tab initializationTab = createDraggableTab("Initialisation");
        Tab selectionTab = createDraggableTab("Selection");
        Tab crossoverTab = createDraggableTab("Crossover");
        Tab mutationTab = createDraggableTab("Mutation");
        Tab localSearchTab = createDraggableTab("Local Search");
        Tab replacementTab = createDraggableTab("Replacement");

        initializationTab.setStyle("-fx-background-color: #B0D9FF;"); // Pastel Blue
        selectionTab.setStyle("-fx-background-color: #FFD699;"); // Pastel Orange
        crossoverTab.setStyle("-fx-background-color: #B5E3B5;"); // Pastel Green
        mutationTab.setStyle("-fx-background-color: #D4B5FF;"); // Pastel Purple
        localSearchTab.setStyle("-fx-background-color: #FFB5B5;"); // Pastel Red
        replacementTab.setStyle("-fx-background-color: #B5C99A;"); // Pastel Grey

        heuristicCategories.getTabs().addAll(
                initializationTab, selectionTab, crossoverTab, mutationTab, localSearchTab, replacementTab
        );

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        HBox tabContainer = new HBox(leftSpacer, heuristicCategories, rightSpacer);
        HBox.setHgrow(tabContainer, Priority.ALWAYS);
        tabContainer.setPadding(new Insets(10));
        tabContainer.setAlignment(Pos.CENTER);

        bottomPanel.getChildren().add(heuristicCategories);
        return bottomPanel;
    }

    private static TabPane getTabPane() {
        TabPane heuristicCategories = new TabPane();
        heuristicCategories.setStyle(
                "-fx-tab-min-width: 70px; " + // Wider tabs
                        "-fx-tab-max-width: 200px; " +
                        "-fx-tab-min-height: 20px; " + // Taller tabs for better visibility
                        "-fx-font-size: 14px; " + // Larger text inside tabs
                        "-fx-font-weight: bold;"
        );
        heuristicCategories.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return heuristicCategories;
    }

    private Tab createDraggableTab(String categoryName) {
        HBox box = new HBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #D0F0FF; -fx-border-color: black;");

        for (HeuristicData heuristicData : heuristics) {
            if (Objects.equals(heuristicData.getCategory(), categoryName)) {
                ImageView heuristicImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + categoryName + "/" + heuristicData.getName() + ".png"))));
                heuristicImage.setFitWidth(123);
                heuristicImage.setFitHeight(57);

                heuristicImage.setOnMouseClicked(mouseEvent -> {
                    heuristicDescriptionPanel.updateDescription(heuristicData.getId());
                });
                heuristicImage.setOnDragDetected(event -> {
                    Dragboard db = heuristicImage.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(Integer.toString(heuristicData.getId()));

                    db.setContent(content);
                    // Create a scaled snapshot of the image
                    ImageView dragView = new ImageView(heuristicImage.getImage());
                    dragView.setFitWidth(123);  // Set new size
                    dragView.setFitHeight(57);
                    dragView.setSmooth(true);
                    dragView.setPreserveRatio(true);

                    // Ensure smooth rendering
                    SnapshotParameters params = new SnapshotParameters();
                    params.setFill(Color.TRANSPARENT);
                    Image snapshot = dragView.snapshot(params, null);

                    // Set the resized image as the drag view
                    db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);
                    event.consume();
                });

                box.getChildren().add(heuristicImage);
            }
        }


        return new Tab(categoryName, box);
    }
}
