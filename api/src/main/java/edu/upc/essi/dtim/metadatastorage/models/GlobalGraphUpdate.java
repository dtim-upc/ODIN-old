package edu.upc.essi.dtim.metadatastorage.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Deleted {
    @JsonProperty("classes")
    String[] classes;
    @JsonProperty("properties")
    String[] properties;
}

@Getter
@Setter
public class GlobalGraphUpdate {
    @JsonProperty("globalGraph")
    GlobalGraph globalGraph;
    @JsonProperty("isModified")
    String isModified;
    @JsonProperty("ttl")
    String ttl;
    @JsonProperty("deleted")
    Deleted deleted;
}

