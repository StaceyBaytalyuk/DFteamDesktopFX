package com.dfteam.desktop.controller;

import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.VMLoad;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

public class VMLoadController {

    private static String ip;

    @FXML
    private NumberAxis yCPU;

    @FXML
    private NumberAxis xCPU;

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
        XYChart.Series<Integer, Double> series2 = new XYChart.Series<>();
        VMLoad vmLoad = new VMLoad(ip, "root");
        /*yCPU.setAutoRanging(false);
        xCPU.setAutoRanging(true);
        xCPU.setTickUnit(7);
        yCPU.setLowerBound(0);
        yCPU.setUpperBound(100);*/

        if(vmLoad.isConnected())
            diskLoadText.setText("Disk load: \n"+vmLoad.DiskUsedPersent());
        if(vmLoad.isConnected())
            diskUsedText.setText("Disk usage: \n"+vmLoad.DiskUsedSize()+"GB/"+vmLoad.DiskTotalSize()+"GB");

        new Thread(() -> {
            try {
                int i = 1;
                while (StageManager.isOpenLoad()) {
                    series1.getData().add(new XYChart.Data<Integer, Double>(i, vmLoad.ProcLoadStat()));
                    series2.getData().add(new XYChart.Data<Integer, Double>(i, vmLoad.MemFreeStat()));
                    CPUchart.getData().add(series1);
                    RAMchart.getData().add(series2);
                    i++;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
