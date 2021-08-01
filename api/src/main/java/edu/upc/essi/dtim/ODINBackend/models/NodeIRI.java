package edu.upc.essi.dtim.ODINBackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeIRI {
    @JsonProperty("nodeIri")
    String nodeIri;
    @JsonProperty("nodeIriType")
    String nodeIriType;
}
