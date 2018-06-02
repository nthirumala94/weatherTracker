package com.capitalone.weathertracker.model;

public class Metrics {
    public float temperature;
    public float dewPoint;
    public float precipation;
    
    public Metrics(float temperature, float dewPoint, float precipation) {
        this.temperature = temperature;
        this.dewPoint = dewPoint;
        this.precipation = precipation;
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
	public float getPrecipation() {
		return precipation;
	}
	public void setPrecipation(float precipation) {
		this.precipation = precipation;
	}
	
	@Override
	public String toString() {
	    return "Metrics { " +
	        "temperature = " + this.temperature + " \" +
	        "dewPoint = " + this.dewPoint + " \" +
	        "precipation = " + this.precipation + " \" +
	        "}";
	}
}