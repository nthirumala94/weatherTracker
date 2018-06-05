package com.capitalone.weathertracker.model;

import java.time.*;
import java.util.ArrayList;

public class StatsRequest {

    private ArrayList<String> metric;
    private ArrayList<String> stats;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    
    public StatsRequest(ArrayList<String> metric, ArrayList<String> stats) {
    	this.metric = metric;
    	this.stats = stats;
    }

    public StatsRequest(ArrayList<String> metric, ArrayList<String> stats, LocalDateTime fromDateTime,
    		LocalDateTime toDateTime) {
		this.metric = metric;
		this.stats = stats;
		this.fromDateTime = fromDateTime;
		this.toDateTime = toDateTime;
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

    public LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(LocalDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public LocalDateTime getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(LocalDateTime toDateTime) {
        this.toDateTime = toDateTime;
    }


}