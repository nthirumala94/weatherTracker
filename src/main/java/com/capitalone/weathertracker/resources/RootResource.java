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
    private MeasurementService measurementService = MeasurementServiceImpl.getInstance();
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
        LocalDateTime timeStamp = WeatherTrackerUtil.convertStringToLocalDate(measurement.get("timestamp").asText());
        
        if(!isRequestValid(measurement)) {
            return Response.status(400).build();
        }
        
        Metrics metric = new Metrics(
            measurement.get("temperature").floatValue(),
            measurement.get("dewPoint").floatValue(),
            measurement.get("precipitation").floatValue()
            );
        
        measurementService.addMeasurement(measurement.get("timestamp").asText(), metric);
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
        .path(measurement.get("timestamp").asText());
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
        System.out.println("Here is the list" + measurementList);

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
        
        Response resp = null;
        if(measurementList.size() != 0) {
        	if(measurementList.size() > 1) {
        		resp = Response.status(200).entity(measurementList).build();
        	} else {
        		resp = Response.status(200).entity(measurementList.get(0)).build();
        	}
        } else {
            resp = Response.status(404).build();
        }
        return resp;
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
    	
    	int status;
    	
    	if(isRequestValid(measurement)) {
    		String newtimestamp = measurement.get("timestamp").asText();
    		if(newtimestamp.equals(timestamp)) {
    			Metrics metric = new Metrics(
    					measurement.get("temperature").floatValue(),
    					measurement.get("dewPoint").floatValue(),
    					measurement.get("precipitation").floatValue()
    					);

    			status = measurementService.updateMeasurement(timestamp, metric);
    		} else {
    			status = 409;
    		}
    	} else {
    		status = 400;
    	}
    	
        return Response.status(status).build();
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

        int status;
    	boolean isValidRequest = true;
    	if(measurement.get("temperature") != null) {
    		isValidRequest = isValidRequest && isFloatCheck(measurement.get("temperature").asText());
    	}
    	if(measurement.get("dewPoint") != null) {
    		isValidRequest = isValidRequest && isFloatCheck(measurement.get("dewPoint").asText());
    	}
    	if(measurement.get("precipitation") != null) {
    		isValidRequest = isValidRequest && isFloatCheck(measurement.get("precipitation").asText());
    	}
    	
    	if(isValidRequest) {
    		String newtimestamp = measurement.get("timestamp").asText();
    		if(newtimestamp.equals(timestamp)) {
    			Metrics metric = new Metrics(
    					measurement.get("temperature").floatValue(),
    					measurement.get("dewPoint").floatValue(),
    					measurement.get("precipitation").floatValue()
    					);

    			status = measurementService.patchMeasurement(timestamp, metric);
    		} else {
    			status = 409;
    		}
    	} else {
    		status = 400;
    	}
    	
        return Response.status(status).build();
    }

    // features/01-measurements/04-delete-measurement.feature
    @DELETE @Path("/measurements/{timestamp}")
    public Response deleteMeasurement(@PathParam("timestamp") String timestamp) {
        /* Example:
        timestamp := "2015-09-01T16:20:00.000Z"
        */

        Metrics result = measurementService.deleteMeasurement(timestamp);
        int status = 0;
        if(result != null) {
            status = 204;
        } else {
            status = 404;
        }
        return Response.status(status).build();
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
    
    private boolean isRequestValidPatch(JsonNode measurement) {
    	try {
    		String temp = measurement.get("temperature").asText();
    		String dew = measurement.get("dewPoint").asText();
    		String prec = measurement.get("precipitation").asText();
    		
    		Float.parseFloat(temp);
    		Float.parseFloat(dew);
    		Float.parseFloat(prec);
    	} catch (Exception e) {
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