package edu.upc.essi.dtim.odin.bootstrapping.pojos;

import com.google.gson.Gson;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.utils.jena.parsers.graphy.Graphy;
import lombok.Data;

import java.util.List;

@Data
public class Schema {

    // represents local or global schemas

    private Graphy graphicalSchema; //local or minimal schema
    private Graphy graphicalIntegration;
    private String type;


    private List<Alignment> unusedA;


    public Schema(){
//        this.graphicalSchema = "";
//        this.graphicalIntegration = "";
    }


    public void setGraphicalSchemaStr(String s){
        graphicalSchema = new Gson().fromJson(s, Graphy.class);
    }

    public String getGraphicalSchemaStr() {
        return new Gson().toJson(graphicalSchema);
    }

    public void setGraphicalIntegrationStr(String s){
        graphicalIntegration = new Gson().fromJson(s, Graphy.class);
    }

    public String getGraphicalIntegrationStr() {
        return new Gson().toJson(graphicalIntegration);
    }

}
