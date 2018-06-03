package com.capitalone.weathertracker.service;

import java.time.ZonedDateTime;
import com.capitalone.weathertracker.model.*;
import java.util.*;

public interface MeasurementService {
     void addMeasurement(String timestamp, Metrics metrics);
     ArrayList<Measurements> getMeasurement(String timestamp);
     Measurements updateMeasurement(String timestamp, Metrics metrics, boolean isValidRequest, boolean entryExists);
}