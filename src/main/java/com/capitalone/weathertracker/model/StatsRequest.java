package com.capitalone.weathertracker.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class StatsRequest {

    private ArrayList<String> metric;
    private ArrayList<String> stats;
//    private LocalDate fromDateTime;
//    private LocalDate toDateTime;
    
    public StatsRequest(ArrayList<String> metric, ArrayList<String> stats) {
    	this.metric = metric;
    	this.stats = stats;
    }

    public ArrayList<String> getMetric() {
        return metric;
    }

	public void setMetric(ArrayList<String> metric) {
        this.metric = metric;
    }

    public ArrayList<String> getStats() {
        return stats;
    }

    public void setStats(ArrayList<String> stats) {
        this.stats = stats;
    }

//    public LocalDate getFromDateTime() {
//        return fromDateTime;
//    }
//
//    public void setFromDateTime(LocalDate fromDateTime) {
//        this.fromDateTime = fromDateTime;
//    }
//
//    public LocalDate getToDateTime() {
//        return toDateTime;
//    }
//
//    public void setToDateTime(LocalDate toDateTime) {
//        this.toDateTime = toDateTime;
//    }


}