package edu.upc.essi.dtim.ODINBackend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class LavMapping {

    @Id
    private String id;
    private String globalGraphId;
    private String wrapperId;
    private SameAs[] sameAs;
    private List<String> globalQuery;

    public LavMapping(String globalGraphId, String wrapperId, SameAs[] sameAs, List<String> globalQuery) {
        this.globalGraphId = globalGraphId;
        this.wrapperId = wrapperId;
        this.sameAs = sameAs;
        this.globalQuery = globalQuery;
    }

    @Override
    public String toString() {
        return "LavMapping{" +
                "id=" + id +
                ", globalGraphId='" + globalGraphId + '\'' +
                ", wrapperId='" + wrapperId + '\'' +
                ", sameAs=" + Arrays.toString(sameAs) +
                ", graphicalSubgraph='" + globalQuery + '\'' +
                '}';
    }
}
