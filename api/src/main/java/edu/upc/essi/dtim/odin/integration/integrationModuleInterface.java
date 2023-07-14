package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.nextiadi.models.Alignment;

import java.util.List;

public interface integrationModuleInterface {
    Graph integrate(Graph graphA, Graph graphB, List<Alignment> alignments);

    List<Alignment> getUnused();
}
