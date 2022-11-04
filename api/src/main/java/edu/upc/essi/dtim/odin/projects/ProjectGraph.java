package edu.upc.essi.dtim.odin.projects;

import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;

public enum ProjectGraph {

    HAS_ID(Namespaces.PROJECT.val()+"/hasID"),
    CREATED_BY(Namespaces.PROJECT.val()+"/createdBy"),
    DESCRIPTION(Namespaces.PROJECT.val()+"/description"),
    PRIVACY(Namespaces.PROJECT.val()+"/privacy"),
    COLOR(Namespaces.PROJECT.val()+"/color"),
    NUMBERDATASOURCES(Namespaces.PROJECT.val()+"/numberDataSources"),
    GLOBALSCHEMA(Namespaces.PROJECT.val() +"/globalSchema"),
    INTEGRATEDSCHEMA(Namespaces.PROJECT.val()+"/integratedSchema");


    private String element;

    ProjectGraph(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }


}
