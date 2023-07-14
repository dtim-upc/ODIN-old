package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.NextiaCore.graph.CoreGraphFactory;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaDI;
import edu.upc.essi.dtim.nextiadi.models.Alignment;

import java.util.List;

public class integrationModuleImpl implements integrationModuleInterface{
    @Override
    public Graph integrate(Graph graphA, Graph graphB, List<Alignment> alignments) {
        Graph integratedGraph = CoreGraphFactory.createGraphInstance("normal");
        NextiaDI n = new NextiaDI();

        integratedGraph.setGraph(
                n.Integrate(graphA.getGraph(), graphB.getGraph(), alignments)
        );

        return integratedGraph;
    }
}
