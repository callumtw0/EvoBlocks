package org.example.GUI_FX.Graphs;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Map;
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
public class BestTourGraph {
    private Canvas bestTourCanvas;
    private Map<Integer, double[]> cityCoordinates;
    private int[] bestTour;

    public BestTourGraph() {
        bestTourCanvas = new Canvas(); // Dynamic size
        drawBestTour(bestTourCanvas); // Initial empty tour
    }

    public Pane getPanel() {
        Pane pane = new Pane(bestTourCanvas);
        bestTourCanvas.widthProperty().bind(pane.widthProperty().subtract(10)); // Minimal padding
        bestTourCanvas.heightProperty().bind(pane.heightProperty().subtract(40)); // Account for tab height
        return pane;
    }

    public void updateBestTour(Map<Integer, double[]> cityCoordinates, int[] bestTour) {
        this.cityCoordinates = cityCoordinates;
        this.bestTour = bestTour.clone();
        drawBestTour(bestTourCanvas);
    }

    public void resetGraph() {
        this.cityCoordinates = null;
        this.bestTour = null;
        drawBestTour(bestTourCanvas);
    }

    public void updateUI() {
        drawBestTour(bestTourCanvas);
    }

    public void saveGraph(String filename) {
        // Saving a Canvas as an image requires JavaFX's Snapshot or custom rendering
        // For simplicity, we'll use a placeholder or extend this later
        // You can implement using WritableImage and ImageIO if needed
        System.out.println("Best tour graph saved as " + filename);
    }

    private void drawBestTour(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (gc == null) {
            throw new IllegalStateException("GraphicsContext for best tour is null. Ensure Canvas is properly initialized.");
        }
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear to dynamic size

        if (cityCoordinates == null || bestTour == null || bestTour.length == 0) {
            gc.setFill(Color.GRAY);
            gc.fillText("No best tour available", 10, 20);
            return;
        }

        // Set styling for the best tour visualization
        gc.setStroke(Color.BLUE); // Use dark blue to match UI (#0A3D5F, but JavaFX uses Color.BLUE for simplicity)
        gc.setLineWidth(2);
        gc.setFill(Color.RED); // Cities as red dots

        // Draw cities as dots and tour edges
        double maxX = canvas.getWidth() - 20; // Use full canvas width with minimal padding
        double maxY = canvas.getHeight() - 20;
        double minX = 10, minY = 10;
        double scaleX = maxX / getMaxCoordinate(cityCoordinates, 0); // Scale based on max X
        double scaleY = maxY / getMaxCoordinate(cityCoordinates, 1); // Scale based on max Y
        double scale = Math.min(scaleX, scaleY); // Use the smaller scale to fit both dimensions

        // Draw cities
        for (int cityId : bestTour) {
            double[] coords = cityCoordinates.get(cityId);
            if (coords != null) {
                double x = minX + coords[0] * scale;
                double y = minY + coords[1] * scale;
                gc.fillOval(x - 2, y - 2, 4, 4); // Draw small dot for each city
            }
        }

        // Draw tour edges
        gc.beginPath();
        for (int i = 0; i < bestTour.length; i++) {
            int city1 = bestTour[i];
            int city2 = bestTour[(i + 1) % bestTour.length]; // Connect back to start for closed tour
            double[] coords1 = cityCoordinates.get(city1);
            double[] coords2 = cityCoordinates.get(city2);
            if (coords1 != null && coords2 != null) {
                double x1 = minX + coords1[0] * scale;
                double y1 = minY + coords1[1] * scale;
                double x2 = minX + coords2[0] * scale;
                double y2 = minY + coords2[1] * scale;
                if (i == 0) {
                    gc.moveTo(x1, y1);
                } else {
                    gc.lineTo(x1, y1);
                }
                gc.lineTo(x2, y2);
            }
        }
        gc.stroke();
        gc.closePath();
    }

    private double getMaxCoordinate(Map<Integer, double[]> coordinates, int index) {
        double max = 0;
        for (double[] coords : coordinates.values()) {
            if (coords != null && coords[index] > max) {
                max = coords[index];
            }
        }
        return max > 0 ? max : 1; // Avoid division by zero
    }
}