package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.NextiaCore.graph.CoreGraphFactory;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaDI;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.NextiaGraphy.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.integration.pojos.JoinAlignment;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

import java.util.List;
import java.util.stream.Collectors;

public class integrationModuleImpl implements integrationModuleInterface{
    @Override
    public Graph integrate(Graph graphA, Graph graphB, List<Alignment> alignments) {
        Graph integratedGraph = CoreGraphFactory.createGraphInstance("normal");

        NextiaDI n = new NextiaDI();

        integratedGraph.setGraph(
                n.Integrate(retrieveSourceGraph(alignments, graphA), retrieveSourceGraph(alignments, graphB), alignments)
        );

        return integratedGraph;
    }

    @Override
    public List<Alignment> getUnused(Graph graphA, Graph graphB, List<Alignment> alignments) {
        NextiaDI n = new NextiaDI();
        n.Integrate(retrieveSourceGraph(alignments, graphA), retrieveSourceGraph(alignments, graphB), alignments);
        return n.getUnused();
    }

    @Override
    public Graph globalGraph(Graph graphA, Graph graphB, List<Alignment> alignments) {
        Graph globalGraph = CoreGraphFactory.createGraphInstance("normal");

        NextiaDI n = new NextiaDI();

        n.Integrate(retrieveSourceGraph(alignments, graphA), retrieveSourceGraph(alignments, graphB), alignments);

        globalGraph.setGraph(
                n.getMinimalGraph2()
        );

        return globalGraph;
    }

    @Override
    public Graph joinIntegration(Graph integratedGraph, List<JoinAlignment> joinAlignments) {
        Graph joinGraph = CoreGraphFactory.createGraphInstance("normal");
        Graph schemaIntegration = CoreGraphFactory.createGraphInstance("normal");
        NextiaDI n = new NextiaDI();

        for(JoinAlignment a : joinAlignments) {

            if(a.getRightArrow())
                schemaIntegration.setGraph(n.JoinIntegration(integratedGraph.getGraph(), a.getIriA(), a.getIriB(), a.getL(), a.getRelationship(), a.getDomainA(), a.getDomainB()));
            else
                schemaIntegration.setGraph(n.JoinIntegration(integratedGraph.getGraph(), a.getIriA(), a.getIriB(), a.getL(), a.getRelationship(), a.getDomainB(), a.getDomainA()));
        }

        joinGraph.setGraph(
                schemaIntegration.getGraph()
        );

        return joinGraph;
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
