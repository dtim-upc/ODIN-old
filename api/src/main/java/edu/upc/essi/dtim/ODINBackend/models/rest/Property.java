package edu.upc.essi.dtim.ODINBackend.models.rest;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Property {

    String domain;
    String range;
    String iri;


    public boolean isDataTypeProperty(){

        if(range.contains("http://www.w3.org/2001/XMLSchema#")){
            return true;
        }
        return false;

    }


}
