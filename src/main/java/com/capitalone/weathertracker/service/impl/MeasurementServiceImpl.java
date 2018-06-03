package com.capitalone.weathertracker.service.impl;

import com.capitalone.weathertracker.service.MeasurementService;
import java.time.*;
import com.capitalone.weathertracker.model.*;
import java.util.*;
import com.capitalone.weathertracker.util.WeatherTrackerUtil;

public class MeasurementServiceImpl implements MeasurementService {
    
    private static MeasurementServiceImpl measurementServiceImpl = new MeasurementServiceImpl();
    
    private MeasurementServiceImpl() {}
    
    private Map<ZonedDateTime, Metrics> weatherData = new LinkedHashMap<>();
    
    public static MeasurementServiceImpl getInstance() {
      return measurementServiceImpl;
   }
    
    @Override
    public void addMeasurement(ZonedDateTime timestamp, Metrics metrics) {
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
		    Iterator<Map.Entry<ZonedDateTime, Metrics>> iterator = weatherData.entrySet().iterator();
		    LocalDate localTimestamp = LocalDate.parse(timestamp);
		    while(iterator.hasNext()) {
		        Map.Entry<ZonedDateTime, Metrics> entry = iterator.next();
		        System.out.println("Entry Key in loop: " + entry.getKey());
		        if(entry.getKey().getYear() == localTimestamp.getYear() &&
		        entry.getKey().getMonth() == localTimestamp.getMonth() &&
		        entry.getKey().getDayOfMonth() == localTimestamp.getDayOfMonth()
		        ) {
		            System.out.println("Match found");
		            Measurements m = new Measurements(
			        WeatherTrackerUtil.convertLocalDateToString(entry.getKey()),
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
    public Measurements updateMeasurement(ZonedDateTime timestamp, Metrics metrics, boolean isValidRequest, boolean entryExists) {
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
    		result = new Measurements(
    				timestamp.toString(),
    				existingMetric.getTemperature(),
    				existingMetric.getDewPoint(),
    				existingMetric.getPrecipation()
    				);
    	}
    	return result;
    }
}