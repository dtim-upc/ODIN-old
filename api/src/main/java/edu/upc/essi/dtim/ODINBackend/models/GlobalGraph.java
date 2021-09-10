package edu.upc.essi.dtim.ODINBackend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class GlobalGraph {

    @Id
    private String id;
    private String name;
    private String namedGraph;
    private String graphicalGraph;
    @NotBlank
    private String namespace;

    public GlobalGraph(String name, String namespace) {
        this.name = name;
        this.graphicalGraph = "";
        this.namespace = namespace;
        this.namedGraph = createNamedGraph(name, namespace);
    }

    public String createNamedGraph(String name, String namespace) {
        String _namespace = namespace.charAt(namespace.length()-1) == '/' ? namespace : namespace + "/";
        return _namespace + UUID.randomUUID().toString().replace("-","");
    }

    @Override
    public String toString() {
        return String.format(
                "GlobalGraph[name=%s, namedGraph='%s', graphicalGraph='%s', namespace='%s']",
                name, namedGraph, graphicalGraph, namespace);
    }



}
