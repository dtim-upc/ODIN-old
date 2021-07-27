package edu.upc.essi.dtim.ODINBackend.models;

import edu.upc.essi.dtim.ODINBackend.config.SourceGraph;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class DataSource {
    @Id
    private String id;
    private String name;
    private String iri;
    private String Json_path;
    @NotBlank
    private String type;

    public DataSource(String name, String type) {
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
