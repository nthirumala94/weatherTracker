package com.capitalone.weathertracker.model;

public class Metrics {
    public float temperature;
    public float dewPoint;
    public String precipation;
    
    public Metrics(float temperature, float dewPoint, String precipation) {
        this.temperature = temperature;
        this.dewPoint = dewPoint;
        this.precipation = precipation;
    }
    
    public Metrics() {
        
    }

	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getDewPoint() {
		return dewPoint;
	}
	public void setDewPoint(float dewPoint) {
		this.dewPoint = dewPoint;
	}
	public String getPrecipation() {
		return precipation;
	}
	public void setPrecipation(String precipation) {
		this.precipation = precipation;
	}
	
	public String convertToString() {
	    String string = "Metrics { " +
	        "temperature = " + this.temperature +
	        "dewPoint = " + this.dewPoint +
	        "precipation = " + this.precipation +
	        "}";
	    return string;
	}
}