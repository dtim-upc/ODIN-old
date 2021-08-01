package edu.upc.essi.dtim.ODINBackend.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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
    @JsonProperty("new")
    Added added;
}

