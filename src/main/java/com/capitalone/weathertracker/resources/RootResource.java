package com.capitalone.weathertracker.resources;

import com.capitalone.weathertracker.annotations.PATCH;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import java.net.URI;
import com.capitalone.weathertracker.service.MeasurementService;
import com.capitalone.weathertracker.service.impl.MeasurementServiceImpl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.capitalone.weathertracker.model.*;
import java.lang.Exception;
import org.apache.commons.lang3.StringUtils;
import com.capitalone.weathertracker.util.WeatherTrackerUtil;

/*
  TODO: Implement the endpoints in the ATs.
  The below stubs are provided as a starting point.
  You may refactor them however you like, so long as the resources are defined
  in the `com.capitalone.weathertracker.resources` package.
*/

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RootResource {
    private static final Response NOT_IMPLEMENTED = Response.status(501).build();
    private MeasurementService measurementService = new MeasurementServiceImpl();
    private final String SERVER_URL = "http://localhost:8000";
    
    @Context
    private UriInfo uriInfo;
    // dummy handler so you can tell if the server is running
    // e.g. `curl localhost:8000`
    @GET
    public Response get() {
        return Response
            .ok("Weather tracker is up and running!\n")
            .build();
    }

    // features/01-measurements/01-add-measurement.feature
    @POST @Path("/measurements")
    public Response createMeasurement(JsonNode measurement) {
        UriBuilder builder = null;
        try {
        System.out.println("\n Here is the timeStamp: " + measurement.get("timestamp").asText() + "\n\n\n");
        LocalDateTime timeStamp = WeatherTrackerUtil.convertStringToLocalDate(measurement.get("timestamp").asText());
        
        if(!isRequestValid(measurement)) {
            return Response.status(400).build();
        }
        
        Metrics metric = new Metrics(
            measurement.get("temperature").floatValue(),
            measurement.get("dewPoint").floatValue(),
            measurement.get("precipitation").floatValue()
            );
        
        measurementService.addMeasurement(timeStamp, metric);
        /* Example:
        measurement := {
            "timestamp": "2015-09-01T16:00:00.000Z",
            "temperature": 27.1,
            "dewPoint": 16.7,
            "precipitation": 0
        }
        */
        // UriBuilder builder = UriBuilder.path("/measurements/" + timeStamp);
        
        builder = uriInfo
        .getAbsolutePathBuilder()
        .fromPath("http://localhost:8000/measurements")
        .path(timeStamp.toString());
        // UriBuilder builder = null;
        // try {
        // builder = UriBuilder
        // .fromUri(new URI(SERVER_URL))
        // .path("measurements")
        // .path(timeStamp.toString());
        // }
        // catch (Exception e) {
        //     e.printStackTrace();
        // }
        System.out.println("Here is my path: " + Response.created(builder.build()).build());
        }
        catch (Exception e) {
            return Response.status(400).build();
        }
        return Response.created(builder.build()).build();
    }

    // features/01-measurements/02-get-measurement.feature
    @GET @Path("/measurements/{timestamp}")
    public Response getMeasurement(@PathParam("timestamp") String timestamp) {
        
        ArrayList<Measurements> measurementList = measurementService.getMeasurement(timestamp);
        System.out.println(measurementList);

        /* Example 1:
        timestamp := "2015-09-01T16:20:00.000Z"

        return {
            "timestamp": "2015-09-01T16:00:00.000Z",
            "temperature": 27.1,
            "dewPoint": 16.7,
            "precipitation": 0
        }
        */

        /* Example 2:
        timestamp := "2015-09-01"

        return [
            {
                "timestamp": "2015-09-01T16:00:00.000Z",
                "temperature": 27.1,
                "dewPoint": 16.7,
                "precipitation": 0
            },
            {
                "timestamp": "2015-09-01T16:01:00.000Z",
                "temperature": 27.3,
                "dewPoint": 16.9,
                "precipitation": 0
            }
        ]
        */

        return Response.status(200).build();
    }

    // features/01-measurements/03-update-measurement.feature
    @PUT @Path("/measurements/{timestamp}")
    public Response replaceMeasurement(@PathParam("timestamp") String timestamp, JsonNode measurement) {
        /* Example:
        timestamp := "2015-09-01T16:20:00.000Z"

        measurement := {
            "timestamp": "2015-09-01T16:00:00.000Z",
            "temperature": 27.1,
            "dewPoint": 16.7,
            "precipitation": 0
        }
        */

        return NOT_IMPLEMENTED;
    }

    // features/01-measurements/03-update-measurement.feature
    @PATCH @Path("/measurements/{timestamp}")
    public Response updateMeasurement(@PathParam("timestamp") String timestamp, JsonNode measurement) {
        /* Example:
        timestamp := "2015-09-01T16:20:00.000Z"

        measurement := {
            "timestamp": "2015-09-01T16:00:00.000Z",
            "precipitation": 15.2
        }
        */

        return NOT_IMPLEMENTED;
    }

    // features/01-measurements/04-delete-measurement.feature
    @DELETE @Path("/measurements/{timestamp}")
    public Response deleteMeasurement(@PathParam("timestamp") String timestamp) {
        /* Example:
        timestamp := "2015-09-01T16:20:00.000Z"
        */

        return NOT_IMPLEMENTED;
    }

    @GET @Path("/stats")
    public Response getStats(@QueryParam("metric") List<String> metrics, @QueryParam("stat") List<String> stats) {
        /* Example:
        metrics := [
            "temperature",
            "dewPoint"
        ]

        stats := [
            "min",
            "max"
        ]

        return [
            {
                "metric": "temperature",
                "stat": "min"
                "value": 27.1
            },
            {
                "metric": "temperature",
                "stat": "max"
                "value": 27.5
            },
            {
                "metric": "dewPoint",
                "stat": "min"
                "value": 16.9
            },
            {
                "metric": "dewPoint",
                "stat": "max"
                "value": 17.3
            }
        ]
        */

        return NOT_IMPLEMENTED;
    }
    
    
    private boolean isFloatCheck(String str) {
        try {
            Float.parseFloat(str);
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }
    
    private boolean isRequestValid(JsonNode measurement) {
        return isFloatCheck(measurement.get("temperature").asText()) &&
            isFloatCheck(measurement.get("dewPoint").asText()) &&
            isFloatCheck(measurement.get("precipitation").asText());
    }
}
