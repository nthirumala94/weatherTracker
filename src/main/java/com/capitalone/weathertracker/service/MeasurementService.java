package com.capitalone.weathertracker.service;

import java.time.LocalDateTime;
import com.capitalone.weathertracker.model.*;

public interface MeasurementService {
    void addMeasurement(LocalDateTime timestamp, Metrics metrics);
    void getMeasurement(String timestamp);
}