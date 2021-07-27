package edu.upc.essi.dtim.ODINBackend.config;

public enum Namespaces {

    S("http://www.essi.upc.edu/~snadal/BDIOntology/Source/"),
    G("http://www.essi.upc.edu/~snadal/BDIOntology/Global/"),
    M("http://www.essi.upc.edu/~snadal/BDIOntology/Mappings/"),

    owl("http://www.w3.org/2002/07/owl#"),
    rdf("http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
    rdfs("http://www.w3.org/2000/01/rdf-schema#");

    private String element;

    Namespaces(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }
}
