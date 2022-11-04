package edu.upc.essi.dtim.odin.projects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Project {

    private String id;
    private String name;
    private String description;
    private String privacy;
    private String color;
    private String numberOfDS;
//    private String globalSchemaID;
    private String graphicalSchemaIntegration;
    private String graphicalGlobalSchema; //minimal

//    maybe this should be a user type ? .-.
    private String createdBy;

    // Not sure
    private List<String> datasourcesID;
    private List<String> datasourcesTemporalID;

    public Project(){
        this.id = UUID.randomUUID().toString().replace("-", "");
//        this.globalSchemaID = UUID.randomUUID().toString().replace("-", "");
        this.numberOfDS = "0";
        this.privacy = "private";
        this.description = "";
        this.graphicalGlobalSchema = "";
        this.graphicalSchemaIntegration = "";
    }

    // this is for minimal
    @JsonIgnore
    public String getGlobalSchemaIRI(){
        return Namespaces.GLOBALSCHEMA.val() + '/'+id;

    }

    @JsonIgnore
    public String getSchemaIntegrationIRI(){
        return Namespaces.SCHEMAINTEGRATION.val() + '/'+id;

    }

    @JsonIgnore
    public String getIri(){
        return Namespaces.PROJECT.val() + '/' + id;
    }

}
