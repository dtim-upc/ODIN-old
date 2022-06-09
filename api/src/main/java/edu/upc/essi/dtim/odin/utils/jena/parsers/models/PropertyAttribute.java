package edu.upc.essi.dtim.odin.utils.jena.parsers.models;

import java.util.ArrayList;
import java.util.List;

public class PropertyAttribute {

    String id;
    String iri;
    String baseIri;
    String label;
    List<String> attributes;
    String domain;
    String range;

    public PropertyAttribute(){
        this.attributes = new ArrayList<>();
        this.attributes.add("object");
    }

    public PropertyAttribute(String id, String iri, String baseIri, String label, String domain, String range) {
        this.id = id;
        this.iri = iri;
        this.baseIri = baseIri;
        this.label = label;
        this.domain = domain;
        this.range = range;
        this.attributes = new ArrayList<>();
        this.attributes.add("object");
    }

    public PropertyAttribute(String id, String iri, String domain, String range) {
        this.id = id;
        this.iri = iri;
        this.baseIri = getBaseIri(iri);
        this.label = getLastElem(iri);
        this.domain = domain;
        this.range = range;
        this.attributes = new ArrayList<>();
        this.attributes.add("object");
    }

    public String getLastElem(String iri) {
        String regex = "/";
        if(iri.contains("#")){
            regex = "#";
        }
        String[] bits = iri.split(regex);
        String label = bits[bits.length - 1]; // it could throw an exception when split empty....CHECK!

        if(label.contains(".")){
            String[] bits2 = label.split("\\.");
            label = bits2[bits2.length - 1];
        }

        return label;
    }

    public String getBaseIri(String iri) {
        String regex = "/";
        if(iri.contains("#")){
            regex = "#";
        }
        String[] elem = iri.split(regex);
        String baseIri = "";
        for (int i = 0; i < elem.length - 1; i++) {
            baseIri += elem[i] + "/";
        }
        return baseIri;
    }



}
