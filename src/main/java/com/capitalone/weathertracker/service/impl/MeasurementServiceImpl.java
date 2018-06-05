package com.capitalone.weathertracker.service.impl;

import com.capitalone.weathertracker.service.MeasurementService;
import java.time.*;
import com.capitalone.weathertracker.model.*;
import java.util.*;
import com.capitalone.weathertracker.util.WeatherTrackerUtil;

/**
 * This is implementation of main Service class, it contains the implementation of all the required end points
 */
public class MeasurementServiceImpl implements MeasurementService {
    
    private static MeasurementServiceImpl measurementServiceImpl = new MeasurementServiceImpl();
    
    private MeasurementServiceImpl() {} // Private constructor to make the class a Singleton
    
    private Map<String, Metrics> weatherData = new LinkedHashMap<>(); // The Map which will contain all the data entered during execution
    
    /**
     * This will return a static MeasurementServiceImpl
     * Note - There could have been a better way to create a singleton using Jersey or Jax-Rs. 
     * However due to lack to time had to create a singleton using the traditional method.
     * @return MeasurementServiceImpl which is a singleton
     */
    public static MeasurementServiceImpl getInstance() {
      return measurementServiceImpl;
   }
    /**
     * This method contains the main implementation of add measurement feature
     * it will add a timestamp and associated metrics in Internal database
     */
    @Override
    public void addMeasurement(String timestamp, Metrics metrics) {
        weatherData.put(timestamp, metrics);
    }
    
    /**
     * This method contains the main implementation of get measurement feature
     * it will get information based on a particular timestamp or a date
     */
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
    
    /**
     * This method contains the main implementation of delete measurement feature
     * it will delete information based on a timestamp
     */
    @Override
    public Metrics deleteMeasurement(String timestamp) {
    	return weatherData.remove(timestamp);
    }
    
    /**
     * This method contains the main implementation of update measurement feature
     * it will update information related to metrics based on a particular timestamp
     */
    @Override
    public int updateMeasurement(String timestamp, Metrics metrics) {
    	if(weatherData.containsKey(timestamp)) {
    		weatherData.put(timestamp, metrics);
        	return 204;
    	} else {
    		return 404;
    	}
    }
    
    /**
     * This method contains the main implementation of update measurement feature
     * it will patch information related to metrics based on a particular timestamp
     */
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
    
    /**
     * This method contains the main implementation of stats measurement feature
     * it will retrieve stats information related to metrics between particular timestamps
     */
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