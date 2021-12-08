package edu.upc.essi.dtim.ODINBackend.models.rest;

import lombok.Data;

import java.util.List;

@Data
public class QueryDataSelection {

    String graphIRI;
    List<Classes> classes; // It contains IRIs
    List<Property> properties;

}
