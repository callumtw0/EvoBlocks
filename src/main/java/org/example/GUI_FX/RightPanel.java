package org.example.GUI_FX;


import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import org.example.GUI_FX.Graphs.BestTourGraph;
import org.example.GUI_FX.Graphs.PercentageDiffGraph;
import org.example.GUI_FX.Graphs.StandardDeviationGraph;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
public class RightPanel implements AutoCloseable {
    private TabPane tabPane;
    private BestTourGraph bestTourGraph;
    private StandardDeviationGraph deviationGraph;
    private PercentageDiffGraph percentageDiffGraph; // Assuming this is defined as in the previous response
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private VBox rightPanel;

    // Convergence graph components
    private JFreeChart performanceChart;
    private XYSeries distanceSeries = new XYSeries("Average Distance");
    private XYSeries bestDistanceSeries = new XYSeries("Best Distance");
    private XYSeries worstDistanceSeries = new XYSeries("Worst Distance");
    private XYSeries fitnessSeries = new XYSeries("Average Fitness (1/Average Distance)");
    private XYSeries bestFitnessSeries = new XYSeries("Best Fitness");
    private XYSeries worstFitnessSeries = new XYSeries("Worst Fitness");
    private XYSeries optimalDistanceSeries = new XYSeries("Optimal Distance"); // Unified label
    private XYSeries optimalFitnessSeries = new XYSeries("Optimal Fitness"); // Unified label

    private boolean showFitness = false;
    private Canvas convergenceCanvas;
    private final Object renderLock = new Object(); // Synchronization lock for rendering
    private double optimalDistance = 0; // Store optimal distance from Tester

    public RightPanel() {

        bestTourGraph = new BestTourGraph();
        deviationGraph = new StandardDeviationGraph();
        percentageDiffGraph = new PercentageDiffGraph(optimalDistance); // Initialize as before
        initializeChart();
        convergenceCanvas = new Canvas(700, 450);
        GraphicsContext gcConvergence = convergenceCanvas.getGraphicsContext2D();
        if (gcConvergence == null) {
            throw new IllegalStateException("GraphicsContext for convergence plot is null. Ensure Canvas is properly initialized.");
        }
        FXGraphics2D g2Convergence = new FXGraphics2D(gcConvergence);
        performanceChart.draw(g2Convergence, new java.awt.Rectangle(700, 450));
        convergenceCanvas.setWidth(700);
        convergenceCanvas.setHeight(450);

        CheckBox fitnessToggle = new CheckBox("Show Average Fitness (1/Average Distance)");
        fitnessToggle.setStyle("-fx-background-color: #0A3D5F; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5px 10px; -fx-pref-width: 300px; -fx-alignment: center;");
        fitnessToggle.setOnAction(e -> {
            showFitness = fitnessToggle.isSelected();
            updateUI();
        });

        VBox convergenceContent = new VBox(10, convergenceCanvas, fitnessToggle);
        convergenceContent.setStyle("-fx-background-color: transparent;");

        tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");
        tabPane.setTabMinWidth(150);
        tabPane.setTabMinHeight(30);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab convergenceTab = new Tab("Convergence Plot");
        convergenceTab.setStyle("-fx-background-color: #7A9EB1; -fx-text-fill: white; -fx-font-weight: bold;");
        convergenceTab.setContent(convergenceContent);

        Tab bestTourTab = new Tab("Best Tour");
        bestTourTab.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-weight: bold;");
        bestTourTab.setContent(bestTourGraph.getPanel());

        Tab deviationTab = new Tab("Standard Deviation Plot");
        deviationTab.setStyle("-fx-background-color: #FF5733; -fx-text-fill: white; -fx-font-weight: bold;");
        deviationTab.setContent(deviationGraph.getPanel());

        Tab percentageDiffTab = new Tab("Percentage Difference Plot");
        percentageDiffTab.setStyle("-fx-background-color: #6A1B9A; -fx-text-fill: white; -fx-font-weight: bold;");
        percentageDiffTab.setContent(percentageDiffGraph.getPanel());

        tabPane.getTabs().addAll(convergenceTab, bestTourTab, deviationTab, percentageDiffTab);

        rightPanel = new VBox(0, tabPane);
        rightPanel.setStyle("-fx-border-color: black; -fx-padding: 0px;");
        rightPanel.setPrefWidth(700);
        rightPanel.prefHeightProperty().bind(tabPane.heightProperty());
    }

    private void initializeChart() {
        performanceChart = ChartFactory.createXYLineChart(
                "MA Convergence (Average Distance)", "Generation", "Distance",
                new XYSeriesCollection(), PlotOrientation.VERTICAL, true, true, false
        );
        XYPlot plot = performanceChart.getXYPlot();
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        plot.setDomainGridlinePaint(java.awt.Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(java.awt.Color.LIGHT_GRAY);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, java.awt.Color.GRAY); // Average Distance
        renderer.setSeriesPaint(1, java.awt.Color.GREEN); // Best Distance
        renderer.setSeriesPaint(2, java.awt.Color.RED); // Worst Distance
        renderer.setSeriesPaint(3, java.awt.Color.BLUE); // Optimal Distance/Fitness
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesShapesVisible(2, false);
        renderer.setSeriesShapesVisible(3, false); // No shapes for optimal line
        plot.setRenderer(renderer);
    }

    public VBox getPanel() {
        return rightPanel;
    }

    public void updateGraph(int generation, double averageDistance, double bestDistance, double worstDistance, double optimalDistance) {
        if (averageDistance <= 0 || bestDistance <= 0 || worstDistance <= 0) {
            return;
        }

        this.optimalDistance = optimalDistance;

        executorService.submit(() -> {
            synchronized (distanceSeries) {
                distanceSeries.add(generation, averageDistance);
                bestDistanceSeries.add(generation, bestDistance);
                worstDistanceSeries.add(generation, worstDistance);
                fitnessSeries.add(generation, 1.0 / averageDistance);
                bestFitnessSeries.add(generation, 1.0 / bestDistance);
                worstFitnessSeries.add(generation, 1.0 / worstDistance);
                optimalFitnessSeries.add(generation, 1.0 / optimalDistance); // Optimal fitness
                optimalDistanceSeries.add(generation, optimalDistance); // Optimal distance

            }
            percentageDiffGraph.updateGraph(generation, bestDistance, optimalDistance); // Update percentage difference
            Platform.runLater(() -> {
                updateUI();
                percentageDiffGraph.updateUI();
            });
        });
    }

    public void updateBestTour(Map<Integer, double[]> cityCoordinates, int[] tour) {
        executorService.submit(() -> {
            bestTourGraph.updateBestTour(cityCoordinates, tour);
            Platform.runLater(bestTourGraph::updateUI);
        });
    }

    public void resetGraph() {
        // Shut down the executorService to prevent new tasks from being submitted
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        // Clear all series
        synchronized (distanceSeries) {
            distanceSeries.clear();
            bestDistanceSeries.clear();
            worstDistanceSeries.clear();
            fitnessSeries.clear();
            bestFitnessSeries.clear();
            worstFitnessSeries.clear();
            optimalDistanceSeries.clear();
            optimalFitnessSeries.clear();
        }

        // Reset other graphs
        bestTourGraph.resetGraph();
        deviationGraph.resetGraph();
        percentageDiffGraph.resetGraph();

        // Recreate the executorService for future updates
        executorService = Executors.newFixedThreadPool(2);

        // Force a UI update on the JavaFX thread
        Platform.runLater(() -> {
            updateUI();
            bestTourGraph.updateUI();
            deviationGraph.updateUI();
            percentageDiffGraph.updateUI();
        });
    }

    public void updateUI() {
        synchronized (renderLock) {
            GraphicsContext gc = convergenceCanvas.getGraphicsContext2D();
            if (gc == null) {
                System.err.println("GraphicsContext is null during updateUI. Skipping render.");
                return;
            }
            FXGraphics2D g2 = new FXGraphics2D(gc);
            gc.clearRect(0, 0, 700, 450);
            XYSeriesCollection dataset = new XYSeriesCollection();
            if (showFitness) {
                dataset.addSeries(fitnessSeries);
                dataset.addSeries(bestFitnessSeries);
                dataset.addSeries(worstFitnessSeries);
                dataset.addSeries(optimalFitnessSeries); // Add optimal fitness
                performanceChart.setTitle("MA Convergence (Average Fitness)");
                performanceChart.getXYPlot().getRangeAxis().setLabel("Fitness (1/Distance)");
            } else {
                dataset.addSeries(distanceSeries);
                dataset.addSeries(bestDistanceSeries);
                dataset.addSeries(worstDistanceSeries);
                dataset.addSeries(optimalDistanceSeries); // Add optimal distance
                performanceChart.setTitle("MA Convergence (Average Distance)");
                performanceChart.getXYPlot().getRangeAxis().setLabel("Distance");
            }
            performanceChart.getXYPlot().setDataset(dataset);
            try {
                performanceChart.draw(g2, new java.awt.Rectangle(700, 450));
            } catch (Exception e) {
                gc.setFill(javafx.scene.paint.Paint.valueOf("RED"));
                gc.fillText("Error rendering chart: " + e.getMessage(), 10, 20);
                System.err.println("Rendering error in updateUI: " + e.getMessage());
            }
        }
    }

    public void saveGraph(String filename) {
        Platform.runLater(() -> {
            try {
                BufferedImage image = performanceChart.createBufferedImage(700, 450);
                ImageIO.write(image, "PNG", new File(filename + "_convergence.png"));
                bestTourGraph.saveGraph(filename + "_besttour.png");
                deviationGraph.saveGraph(filename + "_deviation.png");
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename + ".ser"))) {
                    oos.writeObject(distanceSeries);
                    oos.writeObject(fitnessSeries);
                    oos.writeObject(bestDistanceSeries);
                    oos.writeObject(worstDistanceSeries);
                    oos.writeObject(bestFitnessSeries);
                    oos.writeObject(worstFitnessSeries);
                    oos.writeObject(optimalDistanceSeries); // Save optimal series
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadGraph(String filename) {
        executorService.submit(() -> {
            try {
                BufferedImage image = ImageIO.read(new File(filename + "_convergence.png"));
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename + ".ser"))) {
                    XYSeries loadedDistance = (XYSeries) ois.readObject();
                    XYSeries loadedFitness = (XYSeries) ois.readObject();
                    XYSeries loadedBestDistance = (XYSeries) ois.readObject();
                    XYSeries loadedWorstDistance = (XYSeries) ois.readObject();
                    XYSeries loadedBestFitness = (XYSeries) ois.readObject();
                    XYSeries loadedWorstFitness = (XYSeries) ois.readObject();
                    XYSeries loadedOptimalDistance = (XYSeries) ois.readObject(); // Load optimal series

                    synchronized (distanceSeries) {
                        distanceSeries.clear();
                        for (int i = 0; i < loadedDistance.getItemCount(); i++) {
                            XYDataItem item = loadedDistance.getDataItem(i);
                            distanceSeries.add(item.getX(), item.getY());
                        }
                    }
                    synchronized (fitnessSeries) {
                        fitnessSeries.clear();
                        for (int i = 0; i < loadedFitness.getItemCount(); i++) {
                            XYDataItem item = loadedFitness.getDataItem(i);
                            fitnessSeries.add(item.getX(), item.getY());
                        }
                    }
                    synchronized (bestDistanceSeries) {
                        bestDistanceSeries.clear();
                        for (int i = 0; i < loadedBestDistance.getItemCount(); i++) {
                            XYDataItem item = loadedBestDistance.getDataItem(i);
                            bestDistanceSeries.add(item.getX(), item.getY());
                        }
                    }
                    synchronized (worstDistanceSeries) {
                        worstDistanceSeries.clear();
                        for (int i = 0; i < loadedWorstDistance.getItemCount(); i++) {
                            XYDataItem item = loadedWorstDistance.getDataItem(i);
                            worstDistanceSeries.add(item.getX(), item.getY());
                        }
                    }
                    synchronized (bestFitnessSeries) {
                        bestFitnessSeries.clear();
                        for (int i = 0; i < loadedBestFitness.getItemCount(); i++) {
                            XYDataItem item = loadedBestFitness.getDataItem(i);
                            bestFitnessSeries.add(item.getX(), item.getY());
                        }
                    }
                    synchronized (worstFitnessSeries) {
                        worstFitnessSeries.clear();
                        for (int i = 0; i < loadedWorstFitness.getItemCount(); i++) {
                            XYDataItem item = loadedWorstFitness.getDataItem(i);
                            worstFitnessSeries.add(item.getX(), item.getY());
                        }
                    }
                    synchronized (optimalDistanceSeries) {
                        optimalDistanceSeries.clear();
                        for (int i = 0; i < loadedOptimalDistance.getItemCount(); i++) {
                            XYDataItem item = loadedOptimalDistance.getDataItem(i);
                            optimalDistanceSeries.add(item.getX(), item.getY());
                        }
                    }
                    Platform.runLater(() -> {
                        updateUI();
                        bestTourGraph.updateUI();
                        deviationGraph.updateUI();
                        percentageDiffGraph.updateUI();
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void compareGraphs() {
        Platform.runLater(() -> {
            XYSeriesCollection dataset = new XYSeriesCollection();
            if (showFitness) {
                dataset.addSeries(fitnessSeries);
                dataset.addSeries(bestFitnessSeries);
                dataset.addSeries(worstFitnessSeries);
                dataset.addSeries(optimalDistanceSeries); // Add optimal fitness
            } else {
                dataset.addSeries(distanceSeries);
                dataset.addSeries(bestDistanceSeries);
                dataset.addSeries(worstDistanceSeries);
                dataset.addSeries(optimalDistanceSeries); // Add optimal distance
            }
            performanceChart.getXYPlot().setDataset(dataset);
            updateUI();
        });
    }

    @Override
    public void close() throws Exception {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    public void updatePerformanceAndDeviation(int generation, double convergenceRate, double stdDev) {
        Platform.runLater(() -> {
            try {
                deviationGraph.updateGraph(generation, stdDev);
            } catch (Exception e) {
                System.err.println("Error updating performance/deviation graphs: " + e.getMessage());
            }
        });
    }
}