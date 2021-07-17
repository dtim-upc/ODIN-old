package edu.upc.essi.dtim.metadatastorage.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Deleted {
    @JsonProperty("classes")
    String[] classes;
    @JsonProperty("properties")
    String[] properties;
}
