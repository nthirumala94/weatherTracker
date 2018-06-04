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
        System.out.println("Here is the timestamp: " + timestamp + ", metrics" + metrics);
        weatherData.put(timestamp, metrics);
        System.out.println("Size of weatherDate in addMeasurement" + weatherData.size());
    }
    
    @Override
    public ArrayList<Measurements> getMeasurement(String timestamp) {
        System.out.println("Entering getMeasurement: " + timestamp);
        System.out.println("Entering getMeasurement size of weatherDate: " + weatherData.size());
        ArrayList<Measurements> result = new ArrayList<>();
        if(timestamp.toString().length() > 10) {
            LocalDateTime dateTimestamp = WeatherTrackerUtil.convertStringToLocalDate(timestamp);
            
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
		System.out.println("Total results size: " + result.size());
		WeatherTrackerUtil.sortMeasurementList(result);
		for (Measurements object: result) {
            System.out.println(object);
        }
        return result;
    }
    
    @Override
    public Metrics deleteMeasurement(String timestamp) {
    	return weatherData.remove(timestamp);
    }
    
    @Override
    public int updateMeasurement(String timestamp, Metrics metrics) {
    	if(weatherData.containsKey(timestamp)) {
    		weatherData.put(timestamp, metrics);
        	return 204;
    	} else {
    		return 404;
    	}
    }
    
    @Override
    public int patchMeasurement(String timestamp, Metrics metrics) {
    	if(weatherData.containsKey(timestamp)) {
    		Metrics oldMetric = weatherData.get(timestamp);
    		if(metrics.getPrecipation() != Float.MIN_VALUE) {
    			oldMetric.setPrecipation(metrics.getPrecipation());
    		}
    		
    		if(metrics.getDewPoint() != Float.MIN_VALUE) {
    			oldMetric.setDewPoint(metrics.getDewPoint());
    		}
    		
    		if(metrics.getTemperature() != Float.MIN_VALUE) {
    			oldMetric.setTemperature(metrics.getTemperature());
    		}
    		weatherData.put(timestamp, oldMetric);
    		return 204;
    	} else {
    		return 404;
    	}
    }
    
    @Override
	public ArrayList<StatsResponse> getMeasurementStatistics(StatsRequest statsRequest) {

		ArrayList<StatsResponse> statsResponseList = new ArrayList<StatsResponse>();
//		Map<String, Metrics> weatherDataForStats = new LinkedHashMap<>();
// 		Iterator<Map.Entry<String, Metrics>> iterator = weatherData.entrySet().iterator();
//		while(iterator.hasNext()) {
//			Map.Entry<String, Metrics> entry = iterator.next();
//			LocalDate localTimestamp = LocalDate.parse(entry.getKey());
//			if((localTimestamp.isEqual(statsRequest.getFromDateTime())
//					|| localTimestamp.isAfter(statsRequest.getFromDateTime()))
//					&& (localTimestamp.isEqual(statsRequest.getToDateTime())
//					|| localTimestamp.isAfter(statsRequest.getToDateTime()))) {
//				weatherDataForStats.put(entry.getKey(),entry.getValue());
//			}
//		}
        System.out.println("WeatherData in getMeasurementStats: " + weatherData);
        Iterator it = weatherData.entrySet().iterator();
        while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        System.out.println("In weatherData" + pair.getKey() + " = " + pair.getValue());
        it.remove(); // avoids a ConcurrentModificationException
        }
    	for(String metric : statsRequest.getMetric()){
			for(String stat : statsRequest.getStats()) {
				StatsResponse statsResp = new StatsResponse();
				statsResp.setMetric(metric);
				statsResp.setStat(stat);
				
				
				Iterator<Map.Entry<String, Metrics>> statsIterator = weatherData.entrySet().iterator();
				if (stat.equalsIgnoreCase("min")) {
					while (statsIterator.hasNext()) {
						Map.Entry<String, Metrics> statsEntry = statsIterator.next();
						switch (metric) {
							case "temperature":
								if(statsResp.getValue() == 0.0f) {
									statsResp.setValue(statsEntry.getValue().getTemperature());
								}
								if (statsResp.getValue() > statsEntry.getValue().getTemperature()) {
									statsResp.setValue(statsEntry.getValue().getTemperature());
								}
								break;
							case "dewPoint":
								System.out.println("StatsEntry1: " + statsEntry.getValue().getDewPoint());
								System.out.println("StatusResponse1: " + statsResp.getValue());
								if(statsResp.getValue() == 0.0f) {
									statsResp.setValue(statsEntry.getValue().getDewPoint());
								}
								System.out.println("StatsEntry2: " + statsEntry.getValue().getDewPoint());
								System.out.println("StatusResponse2: " + statsResp.getValue());
								if (statsEntry.getValue().getDewPoint() != Float.MIN_VALUE && statsResp.getValue() > statsEntry.getValue().getDewPoint()) {
									statsResp.setValue(statsEntry.getValue().getDewPoint());
								}
								System.out.println("StatsEntry3: " + statsEntry.getValue().getDewPoint());
								System.out.println("StatusResponse3: " + statsResp.getValue());
							case "precipitation":
								if(statsResp.getValue() == 0.0f) {
									statsResp.setValue(statsEntry.getValue().getPrecipation());
								}
								if (statsResp.getValue() > statsEntry.getValue().getPrecipation()) {
									statsResp.setValue(statsEntry.getValue().getPrecipation());
								}
								break;
							default:
								break;
						}
					}
				}
				if (stat.equalsIgnoreCase("max")) {
					while (statsIterator.hasNext()) {
						Map.Entry<String, Metrics> statsEntry = statsIterator.next();
						switch (metric) {
							case "temperature":
								if (statsResp.getValue() < statsEntry.getValue().getTemperature()) {
									statsResp.setValue(statsEntry.getValue().getTemperature());
								}
								break;
							case "dewPoint":
								if (statsResp.getValue() < statsEntry.getValue().getDewPoint()) {
									statsResp.setValue(statsEntry.getValue().getDewPoint());
								}
								break;
							case "precipitation":
								if (statsResp.getValue() < statsEntry.getValue().getPrecipation()) {
									statsResp.setValue(statsEntry.getValue().getPrecipation());
								}
								break;
							default:
								break;
						}
					}
				}
				if (stat.equalsIgnoreCase("average")) {
					while (statsIterator.hasNext()) {
						Map.Entry<String, Metrics> statsEntry = statsIterator.next();
						switch (metric) {
							case "temperature":

								break;
							case "dewPoint":
								break;
							case "precipitation":
								break;
							default:
								break;
						}
					}
				}
				statsResponseList.add(statsResp);
			}
		}
    	return statsResponseList;
	}
}