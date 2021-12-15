package edu.upc.essi.dtim.ODINBackend.models.query;

import lombok.Getter;
import lombok.Setter;
import org.apache.jena.rdf.model.Model;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class ODINQuery {

    Map<String, Model> sourceGraphs;
    Model minimal;
    Map<String, Model>  subGraphs;



}
