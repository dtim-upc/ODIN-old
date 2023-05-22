package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.LocalGraph;
import edu.upc.essi.dtim.NextiaCore.graph.Triple;
import edu.upc.essi.dtim.NextiaCore.graph.URI;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class GraphStoreInterfaceTest {
    private static GraphStoreJenaImpl graphStore;
    private static Graph testGraph;

    @BeforeAll
    static void setUp() {
        AppConfig appConfig = new AppConfig();
        graphStore = new GraphStoreJenaImpl(appConfig);

        // create a test graph to save
        testGraph = createTestGraph();
    }

    @Test
    void testSaveGraph() {
        // save the test graph
        graphStore.saveGraph(testGraph);

        // retrieve the graph from the database and verify that it matches the original graph
        //Graph retrievedGraph = graphStore.getGraph(new URI(testGraph.getName().getURI());
        //Assertions.assertEquals(testGraph, retrievedGraph);
    }

    @AfterAll
    static void tearDown() {
        // delete the test graph from the database
        //graphStore.deleteGraph(new URI("http://example.com/test"));
    }

    private static Graph createTestGraph() {
        Set<Triple> triples = new HashSet<>();
        Graph testGraph = new LocalGraph(new URI("testGraph"), triples);

        Triple testTriple1 = new Triple(new URI("http://example.com/subject1"),
                new URI("http://example.com/predicate1"),
                new URI("http://example.com/object1"));
        Triple testTriple2 = new Triple(new URI("http://example.com/subject2"),
                new URI("http://example.com/predicate2"),
                new URI("http://example.com/object1"));
        Triple testTriple3 = new Triple(new URI("http://example.com/subject1"),
                new URI("http://example.com/predicate1"),
                new URI("http://example.com/object2"));

        testGraph.addTriple(testTriple1);
        testGraph.addTriple(testTriple2);
        testGraph.addTriple(testTriple3);

        return testGraph;
    }
}