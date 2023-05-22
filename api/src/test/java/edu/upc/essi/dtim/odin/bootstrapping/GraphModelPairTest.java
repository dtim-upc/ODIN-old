package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GraphModelPairTest {

    @Test
    void testGetGraph() {
        // Create a mock Graph object
        Graph mockGraph = Mockito.mock(Graph.class);

        // Create a mock Model object
        Model mockModel = Mockito.mock(Model.class);

        // Create a GraphModelPair instance with the mock objects
        GraphModelPair pair = new GraphModelPair(mockGraph, mockModel);

        // Verify that the returned graph is the same as the mockGraph
        Assertions.assertEquals(mockGraph, pair.getGraph());
    }

    @Test
    void testGetModel() {
        // Create a mock Graph object
        Graph mockGraph = Mockito.mock(Graph.class);

        // Create a mock Model object
        Model mockModel = Mockito.mock(Model.class);

        // Create a GraphModelPair instance with the mock objects
        GraphModelPair pair = new GraphModelPair(mockGraph, mockModel);

        // Verify that the returned model is the same as the mockModel
        Assertions.assertEquals(mockModel, pair.getModel());
    }
}
