package edu.upc.essi.dtim.ODINBackend.models;

import edu.upc.essi.dtim.ODINBackend.config.DataSourceTypes;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.SourceGraph;
import edu.upc.essi.dtim.nuupdi.utils.Alignment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document("DataSources")
public class DataSource {
    @Id
    private String id;
    private String name;
    private String iri;
    private String graphicalGraph;


    //files
    private String path;


    // csv
//    private delimiter;

    //integrated
    private String graphicalBootstrap;
    private String graphicalIntegration;
    private String graphicalMinimalIntegration;

    private List<Alignment> unusedA;



    @NotBlank
    private DataSourceTypes type;

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
