package com.capitalone.weathertracker.service;

import java.time.LocalDateTime;
import com.capitalone.weathertracker.model.*;
import java.util.*;

public interface MeasurementService {
     void addMeasurement(String timestamp, Metrics metrics);
     ArrayList<Measurements> getMeasurement(String timestamp);
     Metrics deleteMeasurement(String timestamp);
     int updateMeasurement(String timestamp, Metrics metrics);
     int patchMeasurement(String timestamp, Metrics metrics);
}