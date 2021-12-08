package edu.upc.essi.dtim.ODINBackend.config.vocabulary;

public enum DataSourceGraph {

    SCHEMA(Namespaces.DataSource.val()+"/Schema"),

    HAS_SEPARATOR(Namespaces.DataSource.val()+"/hasSeparator"),
    HAS_PATH(Namespaces.DataSource.val()+"/hasPath"),
    HAS_FORMAT(Namespaces.DataSource.val()+"/hasFormat"),
    HAS_ID(Namespaces.DataSource.val()+"/hasID"),

    INTEGRATION_OF(Namespaces.NextiaDI.val() +"integrationOf"  );


    private String element;

    DataSourceGraph(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }


}
