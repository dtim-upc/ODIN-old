package edu.upc.essi.dtim.ODINBackend.config.db;
import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.logging.Logger;

@Component
public class JenaConnection {

    @Value("${db.jena.dir}")
    private String dir;
    @Value("${db.jena.name}")
    private String name;


    private static JenaConnection instance = new JenaConnection();
    private static final Logger LOGGER = Logger.getLogger(JenaConnection.class.getName());
    private Dataset dataset;

    public JenaConnection() {}

    public Dataset getTDBDataset() {
        LOGGER.info("getTDBDataset()");
        LOGGER.info("Jena Dir: " + dir);
        LOGGER.info("Jena Name: " + name);
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