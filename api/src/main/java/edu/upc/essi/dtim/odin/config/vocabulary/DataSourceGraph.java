package edu.upc.essi.dtim.odin.config.vocabulary;

import edu.upc.essi.dtim.nextiadi.config.DataSourceVocabulary;

public enum DataSourceGraph {

    // TODO: must extend DataSourceVocabulary
    SCHEMA(Namespaces.DataSource.val()+"/Schema"),

    HAS_SEPARATOR(Namespaces.DataSource.val()+"/hasSeparator"),
    HAS_PATH(Namespaces.DataSource.val()+"/path"),
    HAS_DESCRIPTION(Namespaces.DataSource.val()+"/description"),
    HAS_FILENAME(Namespaces.DataSource.val()+"/hasFileName"),
    HAS_FILESIZE(Namespaces.DataSource.val()+"/hasFileSize"),
    HAS_FORMAT(Namespaces.DataSource.val()+"/hasFormat"),
    HAS_ID(Namespaces.DataSource.val()+"/hasID"),

    HAS_PROJECTID(Namespaces.DataSource.val()+"/hasProjectID"),

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
