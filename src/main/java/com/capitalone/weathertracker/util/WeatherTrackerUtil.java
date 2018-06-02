package com.capitalone.weathertracker.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherTrackerUtil {
    
    public static LocalDateTime convertStringToLocalDate(String dateTimeStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
		return dateTime;
    }
    
    //2015-09-01T16:30:00.000Z
    public static String convertLocalDateToString(LocalDateTime timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String formatDateTime = timestamp.format(formatter);
        return formatDateTime;
    }
}