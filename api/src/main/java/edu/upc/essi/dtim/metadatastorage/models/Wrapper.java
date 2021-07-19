package edu.upc.essi.dtim.metadatastorage.models;

import edu.upc.essi.dtim.metadatastorage.config.SourceGraph;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "wrapper")
@Getter
@Setter
@NoArgsConstructor
public class Wrapper {
    @Id
    private String id;
    private String name;
    private Attribute[] attributes;
    private String dataSourcesId;
    private String dataSourcesLabel;

    public Wrapper(String name, Attribute[] attributes, String dataSourcesId) {
        this.name = name;
        this.attributes = attributes;
        this.dataSourcesId = dataSourcesId;
    }

    @Override
    public String toString() {
        return String.format(
                "Wrapper[name=%s, attribute[0]='%s', dataSourceId='%s']",
                name, attributes[0], dataSourcesId);
    }
}