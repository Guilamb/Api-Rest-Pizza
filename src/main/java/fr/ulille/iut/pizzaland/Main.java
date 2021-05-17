package fr.ulille.iut.pizzaland;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/api/v1/";
    private static final LogManager logManager = LogManager.getLogManager();

    static {
        try {
            logManager.readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.properties"));
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
     * application.
     *
     * @return Grizzly HTTP server. 
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in fr.ulille.iut.todo package

        /*        ResourceConfig rc = new ResourceConfig().packages("fr.ulille.iut.pizzaland");
        // Activation des log des requêtes et réponses
        String logging = System.getenv("LOG_MESSAGES");
        if ( logging != null && logging.equalsIgnoreCase("true") ) {
            rc.register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.INFO,
                                           LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));
        }
        String debug = System.getenv("DEBUG_INTERNAL");
        if ( debug != null && debug.equalsIgnoreCase("true") ) {
            rc.register(DebugMapper.class);
            }*/
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new ApiV1());
    }

    /**
     * Main method.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format(
                "Jersey app started with WADL available at " + "%sapplication.wadl\nexport LOG_MESSAGES=true pour voir les requêtes et réponses\nexport DEBUG_INTERNAL=true pour voir les erreurs 500\nHit enter to stop it...",
                BASE_URI));
        System.in.read();
        server.stop();
    }
}
