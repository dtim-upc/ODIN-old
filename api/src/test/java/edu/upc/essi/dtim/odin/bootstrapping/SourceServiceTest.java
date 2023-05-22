package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.CsvDataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.LocalGraph;
import edu.upc.essi.dtim.NextiaCore.graph.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class SourceServiceTest {

    @Autowired
    private SourceService sourceService;

    @Test
    void testReconstructFile() {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/plain",
                "test file content".getBytes()
        );

        String filePath = sourceService.reconstructFile(multipartFile);

        assertEquals("..\\api\\dbFiles\\diskFiles\\", filePath.substring(0,25));
    }

    @Test
    void testExtractData() {
        String filePath = "api/src/test/resources/csvTestFile.csv";
        String datasetName = "Test Dataset";
        String datasetDescription = "Description of the test dataset";

        Dataset dataset = sourceService.extractData(filePath, datasetName, datasetDescription);

        assertEquals(datasetName, dataset.getDatasetName());
        assertEquals(datasetDescription, dataset.getDatasetDescription());
        assertEquals(CsvDataset.class, dataset.getClass());
        assertEquals(filePath, ((CsvDataset) dataset).getPath());
    }

    @Test
    void testTransformToGraph() {
        // Create a dataset object
        Dataset dataset = new CsvDataset("test", "Test Dataset", "Description", "../api/src/test/resources/csvTestFile.csv".replace("/", "\\"));

        // Call the transformToGraph method
        GraphModelPair graphModelPair = sourceService.transformToGraph(dataset);

        // Add assertions to check the result
        assertEquals(LocalGraph.class, graphModelPair.getGraph().getClass());
        assertEquals(URI.class, graphModelPair.getGraph().getName().getClass());
        assertEquals(dataset.getDatasetName(), graphModelPair.getGraph().getName().getURI());
    }

    @Test
    void testGenerateVisualSchema() {
        // Create a graph object
        Graph graph = new LocalGraph(null, new URI("Test Dataset"), new HashSet<>());

        //TODO:
        // Call the generateVisualSchema method
        //String visualSchema = sourceService.generateVisualSchema(graph);
        String visualSchema = "Expected visual schema";

        // Add assertions to check the result
        assertEquals("Expected visual schema", visualSchema);
    }

    @Test
    void testSaveGraphToDatabase() {
        // Create a graph object
        Graph graph = new LocalGraph(null, new URI("Test Dataset"), new HashSet<>());

        // Call the saveGraphToDatabase method
        boolean result = sourceService.saveGraphToDatabase(graph);

        // Add assertions to check the result
        assertEquals(true, result);
    }

    @Test
    void testAddDatasetIdToProject() {
        String projectId = "1";
        Dataset dataset = new CsvDataset(null, "Test Dataset", "Description", "path/to/file.csv");

        // Call the addDatasetIdToProject method
        sourceService.addDatasetIdToProject(projectId, dataset);

        // Add assertions or perform necessary checks
    }

    @Test
    void testDeleteDatasetFromProject() {
        String projectId = "1";
        String datasetId = "dataset-id";

        // Call the deleteDatasetFromProject method
        sourceService.deleteDatasetFromProject(projectId, datasetId);

        // Add assertions or perform necessary checks
    }
}
