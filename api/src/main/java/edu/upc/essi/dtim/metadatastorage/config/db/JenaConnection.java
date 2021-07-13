package edu.upc.essi.dtim.metadatastorage.config.db;
import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.logging.Logger;

@Component
public class JenaConnection {

    @Value("${db.jena.dir}")
    private String dir = "/sample";
    @Value("${db.jena.name}")
    private String name = "JenaODIN";


    private static JenaConnection instance = new JenaConnection();
    private static final Logger LOGGER = Logger.getLogger(JenaConnection.class.getName());
    private Dataset dataset;

    public JenaConnection() {}

    public Dataset getTDBDataset() {
        System.out.println("getTDBDataset()");
        System.out.println("Jena Dir: " + dir);
        System.out.println("Jena Name: " + name);
        if (dataset == null) {
            try {
                System.out.println("here");
                dataset = TDBFactory.createDataset(dir+"/"+name);

                if(dataset== null){
                    System.out.println("DATASET NULO");
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An error has occurred obtaining TDB dataset");
            }


        }
        return dataset;
    }

    public void init() {
        getTDBDataset();

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
        System.out.println(
                "Callback triggered Jena Connection - @PreDestroy.");
        close();
    }

    public static JenaConnection getInstance() {
        return instance;
    }
}