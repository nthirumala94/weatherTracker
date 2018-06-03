package com.capitalone.weathertracker.service;

import java.time.LocalDateTime;
import com.capitalone.weathertracker.model.*;
import java.util.*;

public interface MeasurementService {
     void addMeasurement(LocalDateTime timestamp, Metrics metrics);
     ArrayList<Measurements> getMeasurement(String timestamp);
     Measurements updateMeasurement(LocalDateTime timestamp, Metrics metrics, boolean isValidRequest, boolean entryExists);
}