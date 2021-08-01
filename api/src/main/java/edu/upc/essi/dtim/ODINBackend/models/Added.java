package edu.upc.essi.dtim.ODINBackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Added {
    @JsonProperty("classes")
    NodeIRI[] classes;
    @JsonProperty("properties")
    JenaPropertyTriplet[] properties;
}