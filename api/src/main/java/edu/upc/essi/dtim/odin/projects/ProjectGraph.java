package edu.upc.essi.dtim.odin.projects;

import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;

public enum ProjectGraph {

    HAS_ID(Namespaces.Project.val()+"/hasID"),
    CREATED_BY(Namespaces.Project.val()+"/createdBy"),
    DESCRIPTION(Namespaces.Project.val()+"/description"),
    PRIVACY(Namespaces.Project.val()+"/privacy"),
    COLOR(Namespaces.Project.val()+"/color"),
    NUMBERDATASOURCES(Namespaces.Project.val()+"/numberDataSources"),
    GLOBALSCHEMA(Namespaces.Project.val() +"/globalSchema"),
    INTEGRATEDSCHEMA(Namespaces.Project.val()+"/integratedSchema");


    private String element;

    ProjectGraph(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }


}
