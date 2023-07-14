package edu.upc.essi.dtim.odin.NextiaGraphy;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;

public class nextiaGraphyModuleImpl implements nextiaGraphyModuleInterface{
    @Override
    public String generateVisualGraph(Graph graph) {
        NextiaGraphy visualLib = new NextiaGraphy();
        return visualLib.generateVisualGraphNew(graph);
    }
}
