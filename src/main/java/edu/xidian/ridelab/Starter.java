package edu.xidian.ridelab;

import edu.xidian.ridelab.counters.JavaCounter;

/**
 * Author: cwz
 * Time: 2018/4/29
 * Description:
 */
public class Starter {
    public static void main(String[] args) {
        FileDownloadCounter fileDownloadCounter = new FileDownloadCounter("https://github.com/Claymoreterasa/SimpleJavaApp.git");
        fileDownloadCounter.addCounter(new JavaCounter());
        fileDownloadCounter.statistics();
    }
}
