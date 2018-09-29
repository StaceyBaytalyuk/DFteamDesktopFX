package com.dfteam.desktop.controller;

import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.VMLoad;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;

public class VMLoadController {

    private static String ip;

    @FXML
    private NumberAxis yCPU;

    @FXML
    private NumberAxis xCPU;

    @FXML
    private NumberAxis yRAM;

    @FXML
    private NumberAxis xRAM;

    @FXML
    private LineChart<Integer, Double> RAMchart;

    @FXML
    private LineChart<Integer, Double> CPUchart;

    @FXML
    private Text diskLoadText;

    @FXML
    private Text diskUsedText;

    private Thread updateChar = null;

    private VMLoad vmLoad;

    public static void setIp(String ip) {
        VMLoadController.ip = ip;
    }

    @FXML
    public void initialize(){
        XYChart.Series<Integer, Double> series1 = new XYChart.Series<>();
        XYChart.Series<Integer, Double> series2 = new XYChart.Series<>();
        vmLoad = new VMLoad(ip, "root");
        ////////////////////////////
        xCPU.setAutoRanging(false);
        xCPU.setTickUnit(1);
        //set 4 dot on X
        xCPU.setLowerBound(1);
        xCPU.setUpperBound(4);
        ////////////////////////////
        yCPU.setAutoRanging(false);
        //set Char from 0 to 100 on Y
        yCPU.setLowerBound(0);
        yCPU.setUpperBound(100);
        ////////////////////////////

        ////////////////////////////
        xRAM.setAutoRanging(false);
        xRAM.setTickUnit(1);
        //set 4 dot on X
        xRAM.setLowerBound(1);
        xRAM.setUpperBound(4);
        ////////////////////////////
        yRAM.setAutoRanging(false);
        //set Char from 0 to RAM on Y
        yRAM.setLowerBound(0);
        yRAM.setUpperBound(vmLoad.MemTotalStat());
        ////////////////////////////

        if(vmLoad.isConnected())
            diskLoadText.setText("Disk load: \n"+vmLoad.DiskUsedPersent());
        if(vmLoad.isConnected())
            diskUsedText.setText("Disk usage: \n"+vmLoad.DiskUsedSize()+"GB/"+vmLoad.DiskTotalSize()+"GB");

        CPUchart.getData().add(series1);
        RAMchart.getData().add(series2);

        updateChar = new Thread(() ->{
            int i = 1;

            while(!Thread.interrupted()){
                int finalI = i;
                double cpu = vmLoad.ProcLoadStat();
                double ram = vmLoad.MemFreeStat();
                Platform.runLater(() -> {
                    if (finalI > 5) {
                        xCPU.setLowerBound(xCPU.getLowerBound() + 1d);
                        xCPU.setUpperBound(xCPU.getUpperBound() + 1d);
                        xRAM.setLowerBound(xRAM.getLowerBound() + 1d);
                        xRAM.setUpperBound(xRAM.getUpperBound() + 1d);
                    }
                    series1.getData().add(new XYChart.Data<Integer, Double>(finalI, cpu));
                    series2.getData().add(new XYChart.Data<Integer, Double>(finalI, ram));
                });
                i++;
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateChar.start();
    }

    public void stop(){
        if(updateChar!=null){
            updateChar.interrupt();
            updateChar=null;
        }
        if(vmLoad.isConnected())
            vmLoad.disconnect();
    }
}
