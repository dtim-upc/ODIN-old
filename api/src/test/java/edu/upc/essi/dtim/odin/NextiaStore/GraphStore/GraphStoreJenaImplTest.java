package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.LocalGraph;
import edu.upc.essi.dtim.NextiaCore.graph.Triple;
import edu.upc.essi.dtim.NextiaCore.graph.URI;
import edu.upc.essi.dtim.odin.bootstrapping.GraphModelPair;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.junit.jupiter.api.*;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.mockito.Mockito;

import java.util.Set;

import static edu.upc.essi.dtim.odin.bootstrapping.GraphModelPairTest.getHardcodedModel;

class GraphStoreJenaImplTest {

    private GraphStoreJenaImpl graphStore;
    private Dataset dataset;
    private GraphModelPair graphModelPair;
    private URI graphName;

    @BeforeEach
    void setUp() {
        // Mock AppConfig
        AppConfig appConfig = Mockito.mock(AppConfig.class);
        Mockito.when(appConfig.getJenaPath()).thenReturn("test-directory");

        // Create the GraphStoreJenaImpl instance
        graphStore = new GraphStoreJenaImpl(appConfig);

        // Create a mock Dataset
        dataset = Mockito.mock(Dataset.class);

        // Create a mock GraphModelPair and URI
        graphModelPair = new GraphModelPair(new LocalGraph(), getHardcodedModel());
        graphName = new URI("http://test.com/test");
        graphModelPair.getGraph().setName(graphName);

        // Reset the dataset before each test
        Mockito.doAnswer(invocation -> {
            dataset.abort();
            return null;
        }).when(dataset).begin(ReadWrite.WRITE);


    }

    @Test
    void testSaveGraph() {
        // Call the saveGraph method
        graphStore.saveGraph(graphModelPair);
    }


    @Test
    void testGetGraph() {
        // Call the getGraph method and handle the exception
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            graphStore.getGraph(graphName);
        });

        // Verify the exception message
        Assertions.assertEquals("Graph " + graphName.getURI() + " is empty", exception.getMessage());
    }

    
}
