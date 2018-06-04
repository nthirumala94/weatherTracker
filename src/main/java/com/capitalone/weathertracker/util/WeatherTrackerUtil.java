package com.capitalone.weathertracker.util;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.capitalone.weathertracker.model.Measurements;

public class WeatherTrackerUtil {
    
    public static LocalDateTime convertStringToLocalDate(String dateTimeStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
		return dateTime;
    }
    
    //2015-09-01T16:30:00.000Z
    public static String convertLocalDateToString(LocalDateTime timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String formatDateTime = timestamp.format(formatter) + "Z";
        return formatDateTime;
    }
    
    public static void sortMeasurementList(ArrayList<Measurements> measurementList) {
    	Collections.sort(measurementList, new Comparator<Measurements>() {
    		@Override
    	    public int compare(Measurements m1, Measurements m2) {
    	        return m1.getTimestamp().compareTo(m2.getTimestamp());
    	    }
    	});
    }
    
    public static LocalDate convertStringToLocalDateTimeToLocalDate(String timestamp) {
    	LocalDateTime fromDateLdt = WeatherTrackerUtil.convertStringToLocalDate(timestamp);
    	LocalDate fromDateLt = LocalDate.of(fromDateLdt.getYear(), fromDateLdt.getMonthValue(), fromDateLdt.getDayOfMonth());
    	return fromDateLt;
    }
}