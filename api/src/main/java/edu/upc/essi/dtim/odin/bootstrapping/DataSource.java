package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.nextiadi.config.DataSourceVocabulary;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.models.mongo.Wrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@Document("DataSources")
public class DataSource {


    @Id
    private String id; //yes
    private String name; //yes
    private String iri; //yes
    private String graphicalGraph;

//    @DBRef
//    private List<Wrapper> wrappers;

    //files
    private String path;
    private String filename;

    // csv
    // private String delimiter;

    //integrated
    private String graphicalBootstrap;
    private String graphicalIntegration;
    private String graphicalMinimalIntegration;

    private List<Alignment> unusedA;

    @NotBlank
    private DataSourceTypes type;


//    public String getWrapperIRI_or_IntegratedIRI(){
//        if( type.equals(DataSourceTypes.INTEGRATED)){
//            return iri;
//        }
////        return wrappers.get(0).getIri();
//        return iri;
//    }

    public DataSource(){
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.iri = createDataSourceIri();
    }

    public DataSource(String name, DataSourceTypes type) {
        this.name = name;
        this.type = type;
        this.graphicalGraph = "";
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.iri = createDataSourceIri();
    }

    private String createDataSourceIri() {

        if(type != null) {
            if(type.equals(DataSourceTypes.INTEGRATED)){
                return DataSourceVocabulary.DataSource.val() +'/'+id;
            }
            return DataSourceVocabulary.DataSource.val() + '/' + id;
        }


        return DataSourceVocabulary.DataSource.val() + '/' + id;
    }

    public void setId(String id) {
        this.id = id;
        this.iri = createDataSourceIri();
    }

    //    public String getF() {
//
//        if(type.equals(DataSourceTypes.CSV)){
//            return DataSourceGraph.CSV.val();
//        } else if ( type.equals(DataSourceTypes.JSON)  ) {
//            return DataSourceGraph.JSON.val();
//        } else {
//            return "";
//        }
//    }

    public String getMinimalIRI(){
        // todo: use the namespaces from nextiadi library
        return Namespaces.NextiaDI.val() + id + "/minimal";
    }

    public String getSchemaIRI() {
        return DataSourceGraph.SCHEMA.val() +'/'+ id +'/'+ name ;
//        return DataSourceGraph.SCHEMA.val() +'/'+ name ;
    }

    @Override
    public String toString() {
        return String.format(
                "DataSources[name=%s, type='%s', iri='%s]",
                name, type,iri);
    }
}
