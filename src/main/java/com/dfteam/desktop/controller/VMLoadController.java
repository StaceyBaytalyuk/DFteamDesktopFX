package com.dfteam.desktop.controller;

import com.dfteam.desktop.VMLoad;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

public class VMLoadController {

    private static String ip;

    @FXML
    private LineChart<Integer, Double> RAMchart;

    @FXML
    private LineChart<Integer, Double> CPUchart;

    @FXML
    private Text diskLoadText;

    @FXML
    private Text diskUsedText;

    public static void setIp(String ip) {
        VMLoadController.ip = ip;
    }

    @FXML
    public void initialize(){
        XYChart.Series<Integer, Double> series1 = new XYChart.Series<>();

        VMLoad test = new VMLoad(ip, "root");

        new Thread(() -> {
            try {
                int i = 1;
                while (true) {
                    System.out.println(i);
                    double d = test.ProcLoadStat();
                    System.out.println(d);
                    series1.getData().add(new XYChart.Data<Integer, Double>(i, d));
                    CPUchart.getData().add(series1);
                    i++;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
