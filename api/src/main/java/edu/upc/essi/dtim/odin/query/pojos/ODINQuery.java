package edu.upc.essi.dtim.odin.query.pojos;

import lombok.Getter;
import lombok.Setter;
import org.apache.jena.rdf.model.Model;

import java.util.Map;

@Getter @Setter
public class ODINQuery {

    Map<String, Model> sourceGraphs;
    Model minimal;
    Map<String, Model>  subGraphs;



}
