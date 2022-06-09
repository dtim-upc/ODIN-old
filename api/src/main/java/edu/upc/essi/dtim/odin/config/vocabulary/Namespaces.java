package edu.upc.essi.dtim.odin.config.vocabulary;

public enum Namespaces {

    DTIM("http://www.essi.upc.edu/DTIM"),
    DataSource("http://www.essi.upc.edu/DTIM/DataSource"),
    Integration("http://www.essi.upc.edu/DTIM/Integration"),
    NextiaDI("http://www.essi.upc.edu/DTIM/NextiaDI/"),

    S("http://www.essi.upc.edu/dtim/BDIOntology/Source/"),
    G("http://www.essi.upc.edu/dtim/BDIOntology/Global/"),
    I("http://www.essi.upc.edu/dtim/BDIOntology/Integrated/"),
    M("http://www.essi.upc.edu/dtim/BDIOntology/Mappings/"),
    A("http://www.essi.upc.edu/dtim/BDIOntology/Alignments/"),

    SCHEMA("http://schema.org/");

    private String element;

    Namespaces(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }
}
