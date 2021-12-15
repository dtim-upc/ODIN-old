package edu.upc.essi.dtim.ODINBackend.config.vocabulary;

import edu.upc.essi.dtim.nextiadi.config.DataSourceVocabulary;

public enum DataSourceGraph {

    SCHEMA(Namespaces.DataSource.val()+"/Schema"),

    HAS_SEPARATOR(Namespaces.DataSource.val()+"/hasSeparator"),
    HAS_PATH(Namespaces.DataSource.val()+"/hasPath"),
    HAS_FORMAT(Namespaces.DataSource.val()+"/hasFormat"),
    HAS_ID(Namespaces.DataSource.val()+"/hasID"),

    INTEGRATION_OF(Namespaces.NextiaDI.val() +"integrationOf"  ),
    IS_MINIMAL_OF(Namespaces.NextiaDI.val() +"isMinimalOf"  ),
    GRAPHICAL(DataSourceVocabulary.DataSource.val() +"/graphicalGraph"),
    MINIMAL(Namespaces.NextiaDI.val() +"minimal"  );


    private String element;

    DataSourceGraph(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }


}
