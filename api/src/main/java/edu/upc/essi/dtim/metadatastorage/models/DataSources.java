package edu.upc.essi.dtim.metadatastorage.models;

import edu.upc.essi.dtim.metadatastorage.config.SourceGraph;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class DataSources {
    @Id
    private String id;
    private String name;
    private String iri;
    @NotBlank
    private String type;

    public DataSources(String name, String type) {
        this.name = name;
        this.type = type;
        this.iri = createDataSourceIri(name);
    }

    private String createDataSourceIri(String name) {
        return SourceGraph.DATA_SOURCE.val() + '/' + name;
    }

    @Override
    public String toString() {
        return String.format(
                "DataSources[name=%s, type='%s']",
                name, type);
    }
}
