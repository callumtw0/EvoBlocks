package org.example.GUI_FX.Graphs;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.fx.FXGraphics2D;

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
public class PercentageDiffGraph {
    private JFreeChart percentageChart;
    private XYSeries percentageDiffSeries = new XYSeries("Percentage Difference from Optimum");
    private Canvas percentageCanvas;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private double optimalDistance; // Store optimal distance

    public PercentageDiffGraph(double optimalDistance) {
        this.optimalDistance = optimalDistance;
        initializeChart();
        percentageCanvas = new Canvas(700, 450);
        GraphicsContext gc = percentageCanvas.getGraphicsContext2D();
        if (gc == null) {
            throw new IllegalStateException("GraphicsContext for percentage plot is null.");
        }
        FXGraphics2D g2 = new FXGraphics2D(gc);
        percentageChart.draw(g2, new java.awt.Rectangle(700, 450));
        percentageCanvas.setWidth(700);
        percentageCanvas.setHeight(450);
    }

    private void initializeChart() {
        percentageChart = ChartFactory.createXYLineChart(
                "Percentage Difference from Optimum", "Generation", "% Difference",
                new XYSeriesCollection(), PlotOrientation.VERTICAL, true, true, false
        );
        XYPlot plot = percentageChart.getXYPlot();
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        plot.setDomainGridlinePaint(java.awt.Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(java.awt.Color.LIGHT_GRAY);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, java.awt.Color.BLUE);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);
    }

    public void updateGraph(int generation, double bestDistance, double optimalDistance) {
        this.optimalDistance = optimalDistance;
        if (bestDistance <= 0 || optimalDistance <= 0) {
            return;
        }
        double percentageDiff = ((bestDistance - optimalDistance) / optimalDistance) * 100;
        executorService.submit(() -> {
            synchronized (percentageDiffSeries) {
                percentageDiffSeries.add(generation, percentageDiff);
            }
            Platform.runLater(this::updateUI);
        });
    }

    public void updateUI() {
        GraphicsContext gc = percentageCanvas.getGraphicsContext2D();
        if (gc == null) {
            System.err.println("GraphicsContext is null during updateUI. Skipping render.");
            return;
        }
        FXGraphics2D g2 = new FXGraphics2D(gc);
        gc.clearRect(0, 0, 700, 450);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(percentageDiffSeries);
        percentageChart.getXYPlot().setDataset(dataset);
        try {
            percentageChart.draw(g2, new java.awt.Rectangle(700, 450));
        } catch (Exception e) {
            gc.setFill(javafx.scene.paint.Paint.valueOf("RED"));
            gc.fillText("Error rendering chart: " + e.getMessage(), 10, 20);
            System.err.println("Rendering error in updateUI: " + e.getMessage());
        }
    }

    public void resetGraph() {
        // Shut down the executorService
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

        // Clear the series
        synchronized (percentageDiffSeries) {
            percentageDiffSeries.clear();
        }

        // Recreate the executorService
        executorService = Executors.newFixedThreadPool(1);

        // Update UI
        Platform.runLater(this::updateUI);
    }

    public VBox getPanel() {
        return new VBox(10, percentageCanvas);
    }

}