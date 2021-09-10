package edu.upc.essi.dtim.ODINBackend.config.vocabulary;

public enum Namespaces {

    S("http://www.essi.upc.edu/~snadal/BDIOntology/Source/"),
    G("http://www.essi.upc.edu/~snadal/BDIOntology/Global/"),
    M("http://www.essi.upc.edu/~snadal/BDIOntology/Mappings/"),
    A("http://www.essi.upc.edu/~snadal/BDIOntology/Alignments/"),

    SCHEMA("http://schema.org/");

    private String element;

    Namespaces(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }
}
