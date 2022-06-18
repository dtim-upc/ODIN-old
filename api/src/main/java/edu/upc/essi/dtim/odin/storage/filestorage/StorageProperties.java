package edu.upc.essi.dtim.odin.services.filestorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    @Value("${dataStorage.landingZone.dir}")
    private String location;
    @Value("${dataStorage.landingZone.persistent.name}")
    private String persistent_name;
    @Value("${dataStorage.landingZone.temporal.name}")
    private String temporal_name;

//    I don't think we need this:
//    public String getLocation() {
//        return location;
//    }

    public String getPersistentDir(){
        return location + "/" + persistent_name;
    }

    public String getTemporalDir(){
        return location + "/" + temporal_name;
    }


//    public void setLocation(String location) {
//        this.location = location;
//    }

}