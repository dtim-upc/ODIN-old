package edu.upc.essi.dtim.ODINBackend.models.mongo;

import edu.upc.essi.dtim.ODINBackend.config.DataSourceTypes;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.SourceGraph;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Document("DataSources")
public class DataSource {
    @Id
    private String id;
    private String name;
    private String iri;
    private String graphicalGraph;

    @DBRef
    private List<Wrapper> wrappers;

    //files
    private String path;


    // csv
    // private String delimiter;

    //integrated
    private String graphicalBootstrap;
    private String graphicalIntegration;
    private String graphicalMinimalIntegration;

    private List<Alignment> unusedA;

    @NotBlank
    private DataSourceTypes type;


    public String getWrapperIRI_or_IntegratedIRI(){
        if( type.equals(DataSourceTypes.INTEGRATED)){
            return iri;
        }
        return wrappers.get(0).getIri();
    }

    public DataSource(String name, DataSourceTypes type) {
        this.name = name;
        this.type = type;
        this.graphicalGraph = "";
        this.iri = createDataSourceIri(name);
    }

    private String createDataSourceIri(String name) {
        return SourceGraph.DATA_SOURCE.val() + '/' + name;
    }

    @Override
    public String toString() {
        return String.format(
                "DataSources[name=%s, type='%s', iri='%s]",
                name, type,iri);
    }
}