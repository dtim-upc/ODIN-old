package edu.upc.essi.dtim.ODINBackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SameAs {
    @JsonProperty("attribute")
    private String attribute;
    @JsonProperty("feature")
    private String feature;
}
