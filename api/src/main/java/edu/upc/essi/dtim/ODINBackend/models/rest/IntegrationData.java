package edu.upc.essi.dtim.ODINBackend.models.rest;

import edu.upc.essi.dtim.ODINBackend.models.mongo.DataSource;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class IntegrationData {

    private DataSource dsA;
    private DataSource dsB;
    private String integratedName;
    private List<Alignment> alignments;

//    private String uriA;
//    private String uriB;
//    private String confidence;
//    private String integratedName;
//

}
