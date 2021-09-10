package edu.upc.essi.dtim.ODINBackend.config.db;
import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class JenaConnection {

    @Value("${db.jena.dir}")
    private String dir;
    @Value("${db.jena.name}")
    private String name;


    private static JenaConnection instance = new JenaConnection();
    private static final Logger LOGGER = LoggerFactory.getLogger(JenaConnection.class);
    private Dataset dataset;

    public Dataset getTDBDataset() {
        LOGGER.info("getTDBDataset()");
        LOGGER.info("Jena Dir: {}" , dir);
        LOGGER.info("Jena Name: {}" , name);
        if (dataset == null) {
            try {
                dataset = TDBFactory.createDataset(dir+"/"+name);

                if(dataset== null){
                    LOGGER.info("DATASET NULO");
                }

            } catch (Exception e) {
                LOGGER.info("An error has occurred obtaining TDB dataset");
            }


        }
        return dataset;
    }

    public void close() {
        if(dataset != null){
            if(dataset.isInTransaction())
                dataset.commit();
            dataset.end();
            dataset.close();
            dataset = null;
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