package edu.upc.essi.dtim.ODINBackend.config.vocabulary;

public enum GlobalGraph {

    FEATURE(Namespaces.G.val()+"Feature"),
    HAS_FEATURE(Namespaces.G.val()+"hasFeature");

    private String element;

    GlobalGraph(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }

}
