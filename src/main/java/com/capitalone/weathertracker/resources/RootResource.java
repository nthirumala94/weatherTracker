package com.capitalone.weathertracker.resources;

import com.capitalone.weathertracker.annotations.PATCH;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        /* Example:
        measurement := {
            "timestamp": "2015-09-01T16:00:00.000Z",
            "temperature": 27.1,
            "dewPoint": 16.7,
            "precipitation": 0
        }
        */

        return NOT_IMPLEMENTED;
    }

    // features/01-measurements/02-get-measurement.feature
    @GET @Path("/measurements/{timestamp}")
    public Response getMeasurement(@PathParam("timestamp") String timestamp) {
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

        return NOT_IMPLEMENTED;
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
}
