package edu.upc.essi.dtim.odin.storage;
import edu.upc.essi.dtim.odin.storage.filestorage.FileSystemStorageService;
import edu.upc.essi.dtim.odin.storage.filestorage.StorageProperties;
import edu.upc.essi.dtim.odin.storage.graph.GraphStore;
import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.xml.crypto.Data;

@Component
@DependsOn("fileSystemStorageService")
public class JenaConnection {

//    @Value("${dataStorage.landingZone.dir}")
//    private String dir;
//    @Value("${dataStorage.landingZone.jena.name}")
//    private String name;

//    private static JenaConnection instance = new JenaConnection();
    private static final Logger LOGGER = LoggerFactory.getLogger(JenaConnection.class);

    private GraphStore persistentDataset;
    private GraphStore temporalDataset;

    Dataset tmp;

    public Dataset getTmp(){return tmp;}

    @Autowired
    public JenaConnection(StorageProperties properties, FileSystemStorageService io) {
        io.init();
        System.out.println("jena const");
        open(properties.getPersistentDir()+"/"+properties.getJena(),properties.getTemporalDir()+"/"+properties.getJena());


        if (tmp == null) {
            try {
                tmp = TDBFactory.createDataset(properties.getTemporalDir()+"/tmp");

                if(tmp== null){
                    LOGGER.info("DATASET NULO");
                }

            } catch (Exception e) {
                LOGGER.error("An error has occurred obtaining TDB persistentDataset");
            }


        }


        System.out.println("end jena const");
    }

//    public void init(){
//        System.out.println("jena init");
//    }

    public void open(String persistent, String temporal){
        LOGGER.info("getTDBDataset()");
        LOGGER.info("Jena Dir: {}" , persistent);
        LOGGER.info("Jena temporal: {}" , temporal);

        try {
            persistentDataset = GraphStore.createGraphStore(persistent);
//            persistentDataset = TDBFactory.createDataset(persistent);
            if(persistentDataset== null){
                LOGGER.info("DATASET NULO");
            }
        } catch (Exception e) {
            LOGGER.error("An error has occurred obtaining TDB persistentDataset", e);
        }

        try {
            temporalDataset = GraphStore.createGraphStore(temporal);
//            temporalDataset = TDBFactory.createDataset(temporal);
            if(temporalDataset== null){
                LOGGER.info("DATASET NULO");
            }
        } catch (Exception e) {
            LOGGER.error("An error has occurred obtaining TDB temporalDataset", e);
        }

    }

    public GraphStore persistent(){
        return persistentDataset;
    }

    public GraphStore temporal(){
        return temporalDataset;
    }


//    public Dataset getTDBDataset() {
//
//        if (persistentDataset == null) {
//            try {
//                persistentDataset = TDBFactory.createDataset(dir+"/"+name);
//
//                if(persistentDataset== null){
//                    LOGGER.info("DATASET NULO");
//                }
//            } catch (Exception e) {
//                LOGGER.error("An error has occurred obtaining TDB persistentDataset");
//            }
//        }
//        return persistentDataset;
//    }

    public void close() {
        if(persistentDataset != null){
            if(persistentDataset.isInTransaction())
                persistentDataset.commit();
            persistentDataset.end();
            persistentDataset.close();
            persistentDataset = null;
        }
        if(temporalDataset != null){
            if(temporalDataset.isInTransaction())
                temporalDataset.commit();
            temporalDataset.end();
            temporalDataset.close();
            temporalDataset = null;
        }
    }

    @PreDestroy
    public void destroy() {
        LOGGER.info("Callback triggered Jena Connection - @PreDestroy.");
        close();
    }

//    public static JenaConnection getInstance() {
//        return instance;
//    }
}