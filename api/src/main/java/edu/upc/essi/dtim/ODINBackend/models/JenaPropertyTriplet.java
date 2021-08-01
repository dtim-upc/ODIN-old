package edu.upc.essi.dtim.ODINBackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JenaPropertyTriplet {
    @JsonProperty("object")
    String object;
    @JsonProperty("subject")
    String subject;
    @JsonProperty("property")
    String property;
    @JsonProperty("iriType")
    String iriType;
}
