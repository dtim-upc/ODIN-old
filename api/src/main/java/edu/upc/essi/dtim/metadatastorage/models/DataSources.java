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
    private String file;
    private Integer wrappers = 0;

    public DataSources(String name, String type) {
        System.out.println("DATA SOURCES CREATOR");
        this.name = name;
        this.type = type;
        this.wrappers = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "DataSources[name=%s, type='%s', file='%s', wrappers='%s']",
                name, type, file, wrappers);
    }
}
