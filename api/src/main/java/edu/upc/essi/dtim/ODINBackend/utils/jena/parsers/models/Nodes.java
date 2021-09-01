package edu.upc.essi.dtim.ODINBackend.utils.jena.parsers.models;

import org.apache.jena.rdf.model.Resource;

public class Nodes {
    String id;
    String type;

    public Nodes(){}

    public Nodes(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public Nodes(String id, Resource type) {
        this.id = id;
        this.type = type.getURI();
    }
}
