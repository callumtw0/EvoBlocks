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
        executorService.submit(() -> {
            synchronized (percentageDiffSeries) {
                percentageDiffSeries.clear();
            }
            Platform.runLater(this::updateUI);
        });
    }

    public VBox getPanel() {
        return new VBox(10, percentageCanvas);
    }

}