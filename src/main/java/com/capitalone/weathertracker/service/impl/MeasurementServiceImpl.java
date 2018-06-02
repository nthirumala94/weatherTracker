package com.capitalone.weathertracker.service.impl;

import com.capitalone.weathertracker.service.MeasurementService;
import java.time.LocalDateTime;
import com.capitalone.weathertracker.model.*;
import java.util.*;

public class MeasurementServiceImpl implements MeasurementService {
    private Map<LocalDateTime, Metrics> weatherData = new HashMap<>();
    @Override
    public void addMeasurement(LocalDateTime timestamp, Metrics metrics) {
        weatherData.put(timestamp, metrics);
        for(Map.Entry<LocalDateTime, Metrics> entry : weatherData.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " value: " + entry.getValue().convertToString());
        }
    }
    
    @Override
    private ArrayList<Measurements> getMeasurement(LocalDateTime timestamp) {
        List<Measurements> result = new ArrayList<>();
        if(timestamp.toString().length() > 10) {
			Metrics metricData = weatherData.get(timestamp);
			
			if(metricData != null) {
			    Measurements m = new Measurements(
			        timestamp,
			        metricData.getTemperature(),
			        metricData.getDewPoint(),
			        metricData.getPrecipation()
			    );
			    result.add(m);
			}
		} else {
		    Iterator<Map.Entry<LocalDateTime, Metrics>> iterator = weatherData.entrySet().iterator();
		    while(iterator.hasNext()) {
		        Map.Entry<LocalDateTime, Metrics> entry = iterator.next();
		        if(entry.getKey().getYear() == timestamp.getYear() &&
		        entry.getKey().getMonth() == timestamp.getMonth() &&
		        entry.getKey().getDayOfMonth() == timestamp.getDayOfMonth()
		        ) {
		            
		            Measurements m = new Measurements(
			        timestamp,
			        entry.getValue().getTemperature(),
			        entry.getValue().getDewPoint(),
			        entry.getValue().getPrecipation()
			    );
		            result.add(m);
		        }
		    }
		}
        return result;
    }
}