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
        ArrayList<Measurements> result = new ArrayList<>();
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
		    Iterator<Map.Entry<String, Metrics>> iterator = weatherData.entrySet().iterator();
		    LocalDate localTimestamp = LocalDate.parse(timestamp);
		    while(iterator.hasNext()) {
		        Map.Entry<String, Metrics> entry = iterator.next();
		        String[] entryKeySplit = entry.getKey().split("(?:-|T)");
		        
		        int year = Integer.parseInt(entryKeySplit[0]);
		        int month = Integer.parseInt(entryKeySplit[1]);
		        int day = Integer.parseInt(entryKeySplit[2]);
		        
		        if(year == localTimestamp.getYear() &&
		        month == localTimestamp.getMonth().getValue() &&
		        day == localTimestamp.getDayOfMonth()
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
		Map<String, Metrics> weatherDataForStats = new LinkedHashMap<>();
 		Iterator<Map.Entry<String, Metrics>> iterator = weatherData.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, Metrics> entry = iterator.next();
			LocalDateTime localTimestamp = WeatherTrackerUtil.convertStringToLocalDate(entry.getKey());
			
			if((localTimestamp.isEqual(statsRequest.getFromDateTime())
					|| localTimestamp.isAfter(statsRequest.getFromDateTime()))
					&& (localTimestamp.isBefore(statsRequest.getToDateTime())
					)) {
				weatherDataForStats.put(entry.getKey(),entry.getValue());
			}
		}
		
    	for(String metric : statsRequest.getMetric()){
			for(String stat : statsRequest.getStats()) {
				StatsResponse statsResp = new StatsResponse();
				statsResp.setMetric(metric);
				statsResp.setStat(stat);
				
				
				Iterator<Map.Entry<String, Metrics>> statsIterator = weatherDataForStats.entrySet().iterator();
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

								if(statsResp.getValue() == 0.0f) {
									statsResp.setValue(statsEntry.getValue().getDewPoint());
								}
								if (statsEntry.getValue().getDewPoint() != 0.0f &&
										statsResp.getValue() > statsEntry.getValue().getDewPoint()) {
									statsResp.setValue(statsEntry.getValue().getDewPoint());
								}

								break;
				 			case "precipitation":
				 				if(statsResp.getValue() == 0.0f) {
				 					statsResp.setValue(statsEntry.getValue().getPrecipation());
				 				}
				 				if (statsEntry.getValue().getPrecipation() != 0.0f &&
				 						statsResp.getValue() > statsEntry.getValue().getPrecipation()) {
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
								if(statsResp.getValue() == 0.0f) {
									statsResp.setValue(statsEntry.getValue().getTemperature());
								}
								if (statsEntry.getValue().getTemperature() != 0.0f &&
										statsResp.getValue() < statsEntry.getValue().getTemperature()) {
									statsResp.setValue(statsEntry.getValue().getTemperature());
								}
								break;
							case "dewPoint":
								if(statsResp.getValue() == 0.0f) {
									statsResp.setValue(statsEntry.getValue().getDewPoint());
								}
								if (statsEntry.getValue().getDewPoint() != 0.0f &&
										statsResp.getValue() < statsEntry.getValue().getDewPoint()) {
									statsResp.setValue(statsEntry.getValue().getDewPoint());
								}
								break;
				 			case "precipitation":
				 				if(statsResp.getValue() == 0.0f) {
				 					statsResp.setValue(statsEntry.getValue().getPrecipation());
				 				}
				 				if (statsEntry.getValue().getPrecipation() != 0.0f &&
				 						statsResp.getValue() < statsEntry.getValue().getPrecipation()) {
				 					statsResp.setValue(statsEntry.getValue().getPrecipation());
				 				}
				 				break;
							default:
								break;
						}
					}
				}
				if (stat.equalsIgnoreCase("average")) {
					float temperatureAvg = 0.0f;
					int temperatureCount=0;
					float dewpointAvg = 0.0f;
					int dewpointCount=0;
					float precipAvg = 0.0f;
					int precipCount=0;

					while (statsIterator.hasNext()) {
						Map.Entry<String, Metrics> statsEntry = statsIterator.next();
						switch (metric) {
							case "temperature":
							    if(statsEntry.getValue().getTemperature() != 0.0f){
									temperatureAvg=	temperatureAvg + statsEntry.getValue().getTemperature();
									temperatureCount ++;
								}
								break;
							case "dewPoint":
								if(statsEntry.getValue().getDewPoint() != 0.0f){
									dewpointAvg=	dewpointAvg + statsEntry.getValue().getDewPoint();
									dewpointCount ++;
								}
								break;
				 			case "precipitation":
				 				if(statsEntry.getValue().getPrecipation() != 0.0f){
				 					precipAvg=	precipAvg + statsEntry.getValue().getPrecipation();
				 					precipCount ++;
				 				}
				 				break;
							default:
								break;
						}
					}
					switch (metric) {
						case "temperature":
							if(temperatureCount!=0) temperatureAvg= temperatureAvg/temperatureCount;
							temperatureAvg = (float) (Math.round(temperatureAvg * 100.0)/100.0);
							statsResp.setValue(temperatureAvg);
							break;
						case "dewPoint":
							if(dewpointCount!=0) dewpointAvg= dewpointAvg/dewpointCount;
							dewpointAvg = (float) (Math.round(dewpointAvg * 100.0)/100.0);
							statsResp.setValue(dewpointAvg);
							break;
				 		case "precipitation":
				 			if(precipCount!=0) precipAvg= precipAvg/precipCount;
				 			statsResp.setValue(dewpointAvg);
				 			break;
					}
				}
				statsResponseList.add(statsResp);
			}
		}
		WeatherTrackerUtil.removeItemIfNull(statsResponseList);
    	return statsResponseList;
	}
}