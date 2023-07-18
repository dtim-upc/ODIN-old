package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.NextiaCore.graph.CoreGraphFactory;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.jena.GraphJenaImpl;
import edu.upc.essi.dtim.NextiaDI;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.NextiaGraphy.vocabulary.Namespaces;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

import java.util.List;
import java.util.stream.Collectors;

public class integrationModuleImpl implements integrationModuleInterface{
    @Override
    public Graph integrate(Graph graphA, Graph graphB, List<Alignment> alignments) {
        //todo change
        //Graph integratedGraph = CoreGraphFactory.createGraphInstance("normal");
        Graph integratedGraph = new GraphJenaImpl();

        NextiaDI n = new NextiaDI();

        integratedGraph.setGraph(
                n.Integrate(retrieveSourceGraph(alignments, graphA), retrieveSourceGraph(alignments, graphB), alignments)
        );

        return integratedGraph;
    }

    @Override
    public List<Alignment> getUnused() {
        NextiaDI n = new NextiaDI();
        return n.getUnused();
    }

    public Model retrieveSourceGraph(List<Alignment> alignments, Graph graph) {
        // Todo think in a better way to do this. Maybe identifiers should be declared when loading data
        List<Alignment> aligId= alignments.stream().filter(x -> x.getType().contains("datatype")  ).collect(Collectors.toList());;

        Model sourceG = graph.getGraph();

        for ( Alignment a : aligId) {
            Resource rA = sourceG.createResource(a.getIriA());
            Resource rB = sourceG.createResource(a.getIriB());

            if (sourceG.containsResource(rA) ) {
                graph.addTriple(rA.getURI(), RDFS.subClassOf.getURI(),Namespaces.SCHEMA.val()+"identifier");
            } else {
                graph.addTriple(rB.getURI(), RDFS.subClassOf.getURI(), Namespaces.SCHEMA.val()+"identifier");
            }
        }

        sourceG = graph.getGraph();
        return  sourceG;
    }
}
