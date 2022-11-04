package edu.upc.essi.dtim.odin.models.rest;

import lombok.Data;

import java.util.List;

@Data
public class QueryDataSelection {

//    String graphIRI;
    String graphID;
    String graphType;
    List<Classes> classes; // It contains IRIs
    List<Property> properties;

}
