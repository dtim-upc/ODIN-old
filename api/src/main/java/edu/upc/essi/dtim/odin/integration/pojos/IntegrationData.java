package edu.upc.essi.dtim.odin.integration.pojos;

//import edu.upc.essi.dtim.odin.bootstrapping.DataSource;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.bootstrapping.pojos.DataSource;
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
