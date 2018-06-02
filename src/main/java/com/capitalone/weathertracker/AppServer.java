/***************************
 * DO NOT CHANGE THIS FILE *
 ***************************/

package com.capitalone.weathertracker;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class AppServer {
    public static final int PORT = 8000; // Required port for HackerRank

    public static void main(String[] args) {
        ResourceConfig config = new ResourceConfig()
            .packages("com.capitalone.weathertracker")
            .register(JacksonFeature.class);

        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(PORT);
        String pathSpec = "/*";
        ServletContextHandler context = new ServletContextHandler(server, pathSpec);
        context.addServlet(servlet, pathSpec);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
