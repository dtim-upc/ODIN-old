package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;

import edu.upc.essi.dtim.odin.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GraphStoreFactory {

    private static final Logger logger = LoggerFactory.getLogger(GraphStoreFactory.class);

    private GraphStoreFactory() {
        // Private constructor prevents instantiation from outside the class
    }

    public static GraphStoreInterface getInstance(AppConfig appConfig) throws Exception {
        if (appConfig != null) {
            String dbType = appConfig.getDBTypeProperty();
            logger.info("Creating new instance of GraphStoreFactory with DB type: {}", dbType);

            switch (dbType) {
                case "JENA":
                    return new GraphStoreJenaImpl(appConfig);
                case "DUMMY":
                    //OTHER IMPLEMENTATIONS
                    logger.warn("DUMMY IS NOT A REAL DB TYPE");
                    break;
                default:
                    throw new Exception("Error with DB type");
            }
        } else {
            logger.error("The AppConfig is null, so instance of GraphStoreFactory is not working");
            throw new IllegalArgumentException("AppConfig is null");
        }
        return null;
    }
}
