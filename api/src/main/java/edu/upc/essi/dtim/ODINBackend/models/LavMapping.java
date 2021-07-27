package edu.upc.essi.dtim.ODINBackend.models;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Arrays;

@Getter @Setter
@NoArgsConstructor
public class LavMapping {

    @Id
    private String id;
    private String globalGraphId;
    private String wrapperId;
    private SameAs[] sameAs;
    private String graphicalSubgraph;

    public LavMapping(String globalGraphId, String wrapperId, SameAs[] sameAs, String graphicalSubgraph) {
        this.globalGraphId = globalGraphId;
        this.wrapperId = wrapperId;
        this.sameAs = sameAs;
        this.graphicalSubgraph = graphicalSubgraph;
    }

    @Override
    public String toString() {
        return "LavMapping{" +
                "id=" + id +
                ", globalGraphId='" + globalGraphId + '\'' +
                ", wrapperId='" + wrapperId + '\'' +
                ", sameAs=" + Arrays.toString(sameAs) +
                ", graphicalSubgraph='" + graphicalSubgraph + '\'' +
                '}';
    }
}
