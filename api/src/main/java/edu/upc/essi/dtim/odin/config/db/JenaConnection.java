package edu.upc.essi.dtim.odin.config.db;
import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class JenaConnection {

    @Value("${dataStorage.landingZone.dir}")
    private String dir;
    @Value("${dataStorage.landingZone.jena.name}")
    private String name;

    private static JenaConnection instance = new JenaConnection();
    private static final Logger LOGGER = LoggerFactory.getLogger(JenaConnection.class);

    private Dataset persistentDataset;
    private Dataset temporalDataset;

    public Dataset getTDBDataset() {
        LOGGER.info("getTDBDataset()");
        LOGGER.info("Jena Dir: {}" , dir);
        LOGGER.info("Jena Name: {}" , name);
        if (persistentDataset == null) {
            try {
                persistentDataset = TDBFactory.createDataset(dir+"/"+name);

                if(persistentDataset== null){
                    LOGGER.info("DATASET NULO");
                }

            } catch (Exception e) {
                LOGGER.error("An error has occurred obtaining TDB persistentDataset");
            }


        }
        return persistentDataset;
    }

    public void close() {
        if(persistentDataset != null){
            if(persistentDataset.isInTransaction())
                persistentDataset.commit();
            persistentDataset.end();
            persistentDataset.close();
            persistentDataset = null;
        }
    }

    @PreDestroy
    public void destroy() {
        LOGGER.info("Callback triggered Jena Connection - @PreDestroy.");
        close();
    }

    public static JenaConnection getInstance() {
        return instance;
    }
}