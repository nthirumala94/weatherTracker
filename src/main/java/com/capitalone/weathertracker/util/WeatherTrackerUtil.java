package com.capitalone.weathertracker.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherTrackerUtil {
    
    public static LocalDateTime convertStringToLocalDate(String dateTimeStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
		return dateTime;
    }
}