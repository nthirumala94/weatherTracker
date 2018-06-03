package com.capitalone.weathertracker.service.impl;

import com.capitalone.weathertracker.service.MeasurementService;
import java.time.*;
import com.capitalone.weathertracker.model.*;
import java.util.*;
import com.capitalone.weathertracker.util.WeatherTrackerUtil;

public class MeasurementServiceImpl implements MeasurementService {
    
    private static MeasurementServiceImpl measurementServiceImpl = new MeasurementServiceImpl();
    
    private MeasurementServiceImpl() {}
    
    private Map<String, Metrics> weatherData = new LinkedHashMap<>();
    
    public static MeasurementServiceImpl getInstance() {
      return measurementServiceImpl;
   }
    
    @Override
    public void addMeasurement(String timestamp, Metrics metrics) {
        weatherData.put(timestamp, metrics);
        System.out.println("Size of weatherDate in addMeasurement" + weatherData.size());
    }
    
    @Override
    public ArrayList<Measurements> getMeasurement(String timestamp) {
        System.out.println("Entering getMeasurement: " + timestamp);
        System.out.println("Entering getMeasurement size of weatherDate: " + weatherData.size());
        ArrayList<Measurements> result = new ArrayList<>();
        if(timestamp.toString().length() > 10) {
            ZonedDateTime dateTimestamp = WeatherTrackerUtil.convertStringToLocalDate(timestamp);
            
			Metrics metricData = weatherData.get(dateTimestamp);
			
			if(metricData != null) {
			    Measurements m = new Measurements(
			        WeatherTrackerUtil.convertLocalDateToString(dateTimestamp),
			        metricData.getTemperature(),
			        metricData.getDewPoint(),
			        metricData.getPrecipation()
			    );
			    result.add(m);
			}
		} else {
		    System.out.println("Entering Else, weatherData size" + weatherData.size());
		    Iterator<Map.Entry<String, Metrics>> iterator = weatherData.entrySet().iterator();
		    LocalDate localTimestamp = LocalDate.parse(timestamp);
		    while(iterator.hasNext()) {
		        Map.Entry<String, Metrics> entry = iterator.next();
		        System.out.println("Entry Key in loop: " + entry.getKey());
		        String[] entryKeySplit = entry.getKey().split("(?:-|T)");
		        int year = Integer.parseInt(entryKeySplit[0]);
		        int month = Integer.parseInt(entryKeySplit[1]);
		        int day = Integer.parseInt(entryKeySplit[2]);
		        System.out.println("Year : " + year + " month " + month + " day " + day);
		        if(year == localTimestamp.getYear() &&
		        month == localTimestamp.getMonth().getValue() &&
		        day == localTimestamp.getDayOfMonth()
		        ) {
		            System.out.println("Match found");
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
		System.out.println(result.size());
        return result;
    }
    
    @Override
    public Measurements updateMeasurement(String timestamp, Metrics metrics, boolean isValidRequest, boolean entryExists) {
    	Measurements result = null;
    	if(weatherData.keySet().contains(timestamp)) {
    		entryExists = false;
    	}
    	if(isValidRequest) {
    		weatherData.put(timestamp, metrics);
    		result = new Measurements(
    				timestamp.toString(),
    				metrics.getTemperature(),
    				metrics.getDewPoint(),
    				metrics.getPrecipation()
    				);
    	} else {
    		Metrics existingMetric = weatherData.get(timestamp);
    		System.out.println("The error with Metric: " + existingMetric);
    		result = new Measurements(
    				timestamp.toString(),
    				existingMetric.getTemperature(),
    				existingMetric.getDewPoint(),
    				existingMetric.getPrecipation()
    				);
    	}
    	return result;
    }
    
    @Override
    public void deleteMeasurement(String timestamp) {
    	weatherData.remove(timestamp);
    }
}