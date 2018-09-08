package com.dfteam.desktop.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

public class VMLoadController {

    @FXML
    private LineChart<Integer, Double> RAMchart;

    @FXML
    private LineChart<Integer, Double> CPUchart;

    @FXML
    private Text diskLoadText;

    @FXML
    private Text diskUsedText;

    @FXML
    public void initialize(){
        XYChart.Series<Integer, Double> series = new XYChart.Series<>();
        CPUchart.getData().add(series);

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                for (int i = 0; i < 15; i++) {
                    int finalI = i;
                    Platform.runLater(() -> series.getData().add(new XYChart.Data<Integer, Double>(1 + finalI, 1 + finalI)));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
