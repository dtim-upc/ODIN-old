package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.CsvDataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.JsonDataset;
import edu.upc.essi.dtim.NextiaCore.graph.LocalGraph;
import org.apache.jena.rdf.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static edu.upc.essi.dtim.odin.bootstrapping.GraphModelPairTest.getHardcodedModel;

@SpringBootTest
class SourceServiceTest {

    private SourceService sourceService;

    @BeforeEach
    void setUp(@Autowired SourceService sourceService) {
        // Initialize SourceService instance
        this.sourceService = sourceService;
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
    void testReconstructFile() {
        // Create a mock MultipartFile
        String originalFilename = "test.txt";
        byte[] content = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", originalFilename, null, content);

        // Call the reconstructFile method
        String reconstructedFilePath = sourceService.reconstructFile(multipartFile);

        // Verify the reconstructed file
        Path reconstructedPath = Path.of(reconstructedFilePath);
        Assertions.assertTrue(Files.exists(reconstructedPath));
        Assertions.assertEquals(originalFilename, reconstructedPath.getFileName().toString().substring(17));
    }

    @Test
    void testReconstructFileEmptyFile() {
        // Create an empty mock MultipartFile
        MultipartFile multipartFile = new MockMultipartFile("file", new byte[0]);

        // Call the reconstructFile method and expect a RuntimeException
        Assertions.assertThrows(RuntimeException.class, () -> sourceService.reconstructFile(multipartFile));
    }

    @Test
    void testReconstructFileOutsideDirectory() {
        // Create a mock MultipartFile
        String originalFilename = "../file.txt";
        byte[] content = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", originalFilename, null, content);

        // Call the reconstructFile method and expect a RuntimeException
        Assertions.assertThrows(RuntimeException.class, () -> sourceService.reconstructFile(multipartFile));
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
    void testConvertDatasetToModelWithCsvDataset() {
        // Create a CsvDataset object for testing
        CsvDataset csvDataset = new CsvDataset();
        csvDataset.setDatasetId("123");
        csvDataset.setDatasetName("Test Dataset");
        csvDataset.setPath("../api/src/test/resources/csvTestFile.csv");

        // Call the convertDatasetToModel method
        Model model = sourceService.convertDatasetToModel(csvDataset);

        // Perform assertions
        Assertions.assertNotNull(model);
        // Add more assertions based on your expected behavior
    }

    @Test
    void testConvertDatasetToModelWithJsonDataset() {
        // Create a JsonDataset object for testing
        JsonDataset jsonDataset = new JsonDataset();
        jsonDataset.setDatasetId("456");
        jsonDataset.setDatasetName("Test Dataset");
        jsonDataset.setPath("../api/src/test/resources/jsonTestFile.json");

        // Call the convertDatasetToModel method
        Model model = sourceService.convertDatasetToModel(jsonDataset);

        // Perform assertions
        Assertions.assertNotNull(model);
    }

    @Test
    void testConvertDatasetToModelWithIOException() {
        // Create a CsvDataset object for testing
        CsvDataset csvDataset = new CsvDataset();
        csvDataset.setDatasetId("123");
        csvDataset.setDatasetName("Test Dataset");
        csvDataset.setPath("path/to/csv");

        // Call the convertDatasetToModel method and expect an exception
        Assertions.assertThrows(RuntimeException.class, () -> sourceService.convertDatasetToModel(csvDataset));
    }

    @Test
    void testConvertDatasetToModelWithFileNotFoundException() {
        // Create a JsonDataset object for testing
        JsonDataset jsonDataset = new JsonDataset();
        jsonDataset.setDatasetId("456");
        jsonDataset.setDatasetName("Test Dataset");
        jsonDataset.setPath("path/to/json");

        // Call the convertDatasetToModel method and expect an exception
        Assertions.assertThrows(RuntimeException.class, () -> sourceService.convertDatasetToModel(jsonDataset));
    }

    @Test
    void testHandleUnsupportedDatasetFormat() {
        // Create a Dataset object for testing
        Dataset dataset = new Dataset();
        dataset.setDatasetId("789");
        dataset.setDatasetName("Test Dataset");


        // Call the handleUnsupportedDatasetFormat method
        GraphModelPair result = sourceService.handleUnsupportedDatasetFormat(dataset);

        // Perform assertions
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getGraph());
        Assertions.assertNull(result.getModel());
        // Add more assertions based on your expected behavior
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
}
