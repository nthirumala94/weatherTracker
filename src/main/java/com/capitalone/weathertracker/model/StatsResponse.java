package com.capitalone.weathertracker.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatsResponse {

    private String metric;
    private String stat;
    private float value;

    public StatsResponse(String metric, String stat, float value){
        this.metric = metric;
        this.stat= stat;
        this.value = value;
    }

    public StatsResponse(){
    }

    public String getMetric() {
        return metric;
    }

    public float getValue() {
        return value;
    }
    
    public String getStat() {
    	return stat;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public void setValue(float value) {
        this.value = value;
    }


}
