package com.capitalone.weathertracker.service.impl;

import com.capitalone.weathertracker.service.MeasurementService;
import java.time.LocalDateTime;
import com.capitalone.weathertracker.model.Metrics;
import java.util.*;

public class MeasurementServiceImpl implements MeasurementService {
    private Map<LocalDateTime, Metrics> weatherData = new HashMap<>();
    @Override
    public void addMeasurement(LocalDateTime timestamp, Metrics metrics) {
        weatherData.put(timestamp, metrics);
        for(Map.Entry<LocalDateTime, Metrics> entry : weatherDate.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " value: " + entry.getValue().convertToString());
        }
    }
}