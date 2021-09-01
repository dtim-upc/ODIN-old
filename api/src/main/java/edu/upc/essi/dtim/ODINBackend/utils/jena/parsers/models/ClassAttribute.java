package edu.upc.essi.dtim.ODINBackend.utils.jena.parsers.models;

import org.apache.jena.rdf.model.Resource;

public class ClassAttribute {

    String id;
    String iri;
    String baseIri;
    String label;
//    List<Integer> pos; //for now null

    public ClassAttribute(){}

    public ClassAttribute(String id, String iri, String baseIri, String label) {
        this.id = id;
        this.iri = iri;
        this.baseIri = baseIri;
        this.label = label;
    }

    public ClassAttribute(String id, Resource iri, String baseIri, String label) {
        this.id = id;
        this.iri = iri.toString();
        this.baseIri = baseIri;
        this.label = label;
    }

}
