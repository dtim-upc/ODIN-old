package edu.upc.essi.dtim.odin.newBootstrapping;

import edu.upc.essi.dtim.nextiadi.config.DataSourceVocabulary;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.utils.jena.parsers.graphy.Graphy;
import lombok.Data;
import org.apache.hadoop.shaded.org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class newDataSource {

    private String id;
    private String name;
    private String description;
    @JsonIgnore
    private String path;
    private String filename;
    private String filesize;
    private String projectID;
    private String graphicalSchema;

//    private List<String> tags; maybe if there's time

    @NotBlank
    private DataSourceTypes type;

    // in rdf is link by property has_schema ?id
//    private Schema schema;

    public newDataSource(){

        this.id = UUID.randomUUID().toString().replace("-", "");
//        schema = new Schema();
//        this.iri = DataSourceVocabulary.DataSource.val() + '/' + id;
    }

    public newDataSource(String name, DataSourceTypes type) {

        this.name = name;
        this.type = type;

//        schema = new Schema();
        this.id = UUID.randomUUID().toString().replace("-", "");
//        this.iri = DataSourceVocabulary.DataSource.val() + '/' + id;
    }

    public String getIri(){
        return DataSourceVocabulary.DataSource.val() + '/' + id;
    }

    private String createDataSourceIri() {

        if(type.equals(DataSourceTypes.INTEGRATED)){
            return DataSourceVocabulary.DataSource.val() +'/'+id;
        }

        return DataSourceVocabulary.DataSource.val() + '/' + id;
    }

    public String getSchemaIRI() {
        return DataSourceGraph.SCHEMA.val() +'/'+ id +'/'+ name ;
//        return DataSourceGraph.SCHEMA.val() +'/'+ name ;
    }

    // used to prepare data for query algorithm
    public String getMinimalIRI(){
        // todo: use the namespaces from nextiadi library
        return Namespaces.NextiaDI.val() + id + "/minimal";
    }



}
