package edu.upc.essi.dtim.ODINBackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LavMappingSubgraph {
    public String selection;
    @JsonProperty("LAVMappingID")
    public String lAVMappingID;
    public List<String> graphicalSubGraph;

    @Override
    public String toString() {
        return "LavMappingSubgraph{" +
                "selection='" + selection + '\'' +
                ", lAVMappingID='" + lAVMappingID + '\'' +
                ", graphicalSubGraph=" + graphicalSubGraph +
                '}';
    }
}
