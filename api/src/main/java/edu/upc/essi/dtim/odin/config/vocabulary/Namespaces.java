package edu.upc.essi.dtim.odin.config.vocabulary;

public enum Namespaces {

    DTIM("http://www.essi.upc.edu/DTIM"),
    DataSource("http://www.essi.upc.edu/DTIM/NextiaDI/DataSource"),
    Project("http://www.essi.upc.edu/DTIM/NextiaDI/Project"),
    GlobalSchema("http://www.essi.upc.edu/DTIM/NextiaDI/GlobalSchema"),
    SchemaIntegration("http://www.essi.upc.edu/DTIM/NextiaDI/GlobalSchemaComplete"),
    User("http://www.essi.upc.edu/DTIM/NextiaDI/USER"),
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
