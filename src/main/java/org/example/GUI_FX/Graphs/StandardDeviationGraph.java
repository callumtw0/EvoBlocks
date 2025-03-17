package org.example.GUI_FX.Graphs;

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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StandardDeviationGraph {
    private JFreeChart chart;
    private XYSeries stdDevSeries = new XYSeries("Standard Deviation");
    private Canvas canvas;
    private final Object renderLock = new Object(); // Synchronization lock for rendering
    private VBox panel;

    public StandardDeviationGraph() {
        initializeChart();

        canvas = new Canvas(700, 450);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (gc == null) {
            throw new IllegalStateException("GraphicsContext for deviation graph is null. Ensure Canvas is properly initialized.");
        }
        FXGraphics2D g2 = new FXGraphics2D(gc);
        chart.draw(g2, new Rectangle(700, 450));

        canvas.setWidth(700);
        canvas.setHeight(450);

        panel = new VBox(10, canvas);
        panel.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10px;");
        panel.setPrefWidth(700);
        panel.setPrefHeight(450);
    }

    private void initializeChart() {
        chart = ChartFactory.createXYLineChart(
                "Standard Deviation", "Generation", "Standard Deviation",
                new XYSeriesCollection(), PlotOrientation.VERTICAL, true, true, false
        );
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.ORANGE);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);
    }

    public void updateGraph(int generation, double stdDev) {
        synchronized (stdDevSeries) {
            stdDevSeries.add(generation, stdDev);
        }
        updateUI();
    }

    public void resetGraph() {
        // Not used since we want to persist data across runs
        synchronized (stdDevSeries) {
            stdDevSeries.clear();
        }
    }

    public void saveGraph(String filename) {
        try {
            BufferedImage image = chart.createBufferedImage(700, 450);
            ImageIO.write(image, "PNG", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUI() {
        synchronized (renderLock) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            if (gc == null) {
                System.err.println("GraphicsContext is null during deviation graph updateUI. Skipping render.");
                return;
            }
            FXGraphics2D g2 = new FXGraphics2D(gc);
            gc.clearRect(0, 0, 700, 450);
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(stdDevSeries);
            chart.getXYPlot().setDataset(dataset);
            try {
                chart.draw(g2, new Rectangle(700, 450));
            } catch (Exception e) {
                gc.setFill(javafx.scene.paint.Paint.valueOf("RED"));
                gc.fillText("Error rendering deviation graph: " + e.getMessage(), 10, 20);
                System.err.println("Rendering error in deviation graph: " + e.getMessage());
            }
        }
    }

    public VBox getPanel() {
        return panel;
    }
}