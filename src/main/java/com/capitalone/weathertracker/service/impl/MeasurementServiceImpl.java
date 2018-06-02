package com.capitalone.weathertracker.service.impl;

import com.capitalone.weathertracker.service.MeasurementService;
import java.time.*;
import com.capitalone.weathertracker.model.*;
import java.util.*;
import com.capitalone.weathertracker.util.WeatherTrackerUtil;

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
    public ArrayList<Measurements> getMeasurement(String timestamp) {
        System.out.println("Entering getMeasurement: " + timestamp);
        ArrayList<Measurements> result = new ArrayList<>();
        if(timestamp.toString().length() > 10) {
            LocalDateTime dateTimestamp = WeatherTrackerUtil.convertStringToLocalDate(timestamp);
            
			Metrics metricData = weatherData.get(dateTimestamp);
			
			if(metricData != null) {
			    Measurements m = new Measurements(
			        dateTimestamp,
			        metricData.getTemperature(),
			        metricData.getDewPoint(),
			        metricData.getPrecipation()
			    );
			    result.add(m);
			}
		} else {
		    System.out.println("Entering Else, weatherData size" + weatherData.size);
		    Iterator<Map.Entry<LocalDateTime, Metrics>> iterator = weatherData.entrySet().iterator();
		    LocalDate localTimestamp = LocalDate.parse(timestamp);
		    while(iterator.hasNext()) {
		        Map.Entry<LocalDateTime, Metrics> entry = iterator.next();
		        if(entry.getKey().getYear() == localTimestamp.getYear() &&
		        entry.getKey().getMonth() == localTimestamp.getMonth() &&
		        entry.getKey().getDayOfMonth() == localTimestamp.getDayOfMonth()
		        ) {
		            
		            Measurements m = new Measurements(
			        entry.getKey(),
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