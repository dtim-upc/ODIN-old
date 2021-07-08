package edu.upc.essi.dtim.metadatastorage.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "dataSources")
@Getter
@Setter
@NoArgsConstructor
public class DataSources {
    @Id
    private String id;
    private String name;
    @NotBlank
    private String type;

    public DataSources(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format(
                "DataSources[name=%s, type='%s']",
                name, type);
    }
}
