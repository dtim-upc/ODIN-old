package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.CsvDataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.graph.LocalGraph;
import org.apache.jena.rdf.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@SpringBootTest
class SourceServiceTest {

    private SourceService sourceService;

    @BeforeEach
    void setUp(@Autowired SourceService sourceService) {
        // Initialize SourceService instance
        this.sourceService = sourceService;
    }

    @Test
    void testReconstructFile() throws IOException {
        // Create a mock MultipartFile
        MultipartFile multipartFile = new MockMultipartFile("file", "test.csv", "text/csv", "file content".getBytes());

        // Call the method to reconstruct the file
        String filePath = sourceService.reconstructFile(multipartFile);

        // Assert that the returned file path is not null or empty
        Assertions.assertNotNull(filePath);
        Assertions.assertFalse(filePath.isEmpty());

        // Assert that the file exists at the specified path
        java.io.File file = new java.io.File(filePath);
        Assertions.assertTrue(file.exists());

        // Assert that the file content matches the original content
        String fileContent = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        Assertions.assertEquals("file content", fileContent);
    }

    @Test
    void testExtractData() {
        // Prepare test data
        String filePath = "path/to/dataset.csv";
        String datasetName = "Test Dataset";
        String datasetDescription = "This is a test dataset";

        // Call the method to extract data
        Dataset dataset = sourceService.extractData(filePath, datasetName, datasetDescription);

        // Assert that the dataset is not null
        Assertions.assertNotNull(dataset);

        // Assert that the dataset properties are set correctly
        Assertions.assertEquals(datasetName, dataset.getDatasetName());
        Assertions.assertEquals(datasetDescription, dataset.getDatasetDescription());
    }

    @Test
    void testTransformToGraph() {
        // Prepare test data
        Dataset dataset = new CsvDataset("datasetId", "Test Dataset", "This is a test dataset", "../api/src/test/resources/csvTestFile.csv");

        // Call the method to transform the dataset to a graph
        GraphModelPair graphModelPair = sourceService.transformToGraph(dataset);

        // Assert that the returned GraphModelPair is not null
        Assertions.assertNotNull(graphModelPair);

        // Assert that the graph and model in the GraphModelPair are not null
        Assertions.assertNotNull(graphModelPair.getGraph());
        Assertions.assertNotNull(graphModelPair.getModel());
    }

    @Test
    void testGenerateVisualSchema() {
        // Prepare test data
        GraphModelPair graphModelPair = new GraphModelPair(new LocalGraph(), getHardcodedModel());

        // Call the method to generate the visual schema
        String visualSchema = sourceService.generateVisualSchema(graphModelPair);

        // Assert that the returned visual schema is not null or empty
        Assertions.assertNotNull(visualSchema);
        Assertions.assertFalse(visualSchema.isEmpty());
    }

    private Model getHardcodedModel() {
        // Create a new Jena model
        Model model = ModelFactory.createDefaultModel();

        // Create resources and statements
        Resource subject = model.createResource("http://example.org/subject");
        Resource object = model.createResource("http://example.org/object");
        Property predicate = model.createProperty("http://example.org/predicate");
        Statement statement = model.createStatement(subject, predicate, object);

        // Add the statement to the model
        model.add(statement);

        return model;
    }
}
