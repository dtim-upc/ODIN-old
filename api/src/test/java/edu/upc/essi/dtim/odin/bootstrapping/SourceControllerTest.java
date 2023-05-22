package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.LocalGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SourceControllerTest {
    @Mock
    private SourceService sourceService;

    @InjectMocks
    private SourceController sourceController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBootstrap_Success() throws Exception {
        // Mock input parameters
        String projectId = "123";
        String datasetName = "Test Dataset";
        String datasetDescription = "Test Description";
        MultipartFile attach_file = mock(MultipartFile.class);

        // Mock SourceService methods
        String filePath = "api/src/test/resources/csvTestFile.csv";
        Dataset extractedData = mock(Dataset.class);
        Dataset savedDataset = mock(Dataset.class);
        GraphModelPair graph = mock(GraphModelPair.class);
        String visualSchema = "visual schema";
        Graph savedGraph = mock(LocalGraph.class);

        when(sourceService.reconstructFile(attach_file)).thenReturn(filePath);
        when(sourceService.extractData(filePath, datasetName, datasetDescription)).thenReturn(extractedData);
        when(sourceService.saveDataset(extractedData)).thenReturn(savedDataset);
        when(sourceService.transformToGraph(savedDataset)).thenReturn(graph);
        when(sourceService.generateVisualSchema(graph)).thenReturn(visualSchema);
        when(graph.getGraph()).thenReturn(savedGraph);
        //when(sourceService.addDatasetIdToProject(projectId, savedDataset)).thenReturn(true);

        // Perform the bootstrap operation
        ResponseEntity<?> response = sourceController.bootstrap(projectId, datasetName, datasetDescription, attach_file);

        // Verify the expected interactions and assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedDataset, response.getBody());
        verify(sourceService).reconstructFile(attach_file);
        verify(sourceService).extractData(filePath, datasetName, datasetDescription);
        verify(sourceService).saveDataset(extractedData);
        verify(sourceService).transformToGraph(savedDataset);
        verify(sourceService).generateVisualSchema(graph);
        verify(savedGraph).setGraphicalSchema(visualSchema);
        verify(sourceService).addDatasetIdToProject(projectId, savedDataset);
        verifyNoMoreInteractions(sourceService);
    }

    @Test
    void testBootstrap_UnsupportedOperationException() throws Exception {
        // Mock input parameters
        String projectId = "123";
        String datasetName = "Test Dataset";
        String datasetDescription = "Test Description";
        MultipartFile attach_file = mock(MultipartFile.class);

        // Mock SourceService method
        when(sourceService.reconstructFile(attach_file)).thenThrow(new UnsupportedOperationException());

        // Perform the bootstrap operation and verify the expected exception
        ResponseStatusException exception = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class,
                () -> sourceController.bootstrap(projectId, datasetName, datasetDescription, attach_file));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Data source not created successfully", exception.getReason());

        verify(sourceService).reconstructFile(attach_file);
        verifyNoMoreInteractions(sourceService);
    }

    @Test
    void testBootstrap_Exception() throws Exception {
        // Mock input parameters
        String projectId = "123";
        String datasetName = "Test Dataset";
        String datasetDescription = "Test Description";
        MultipartFile attach_file = mock(MultipartFile.class);

        // Mock SourceService method
        when(sourceService.reconstructFile(attach_file)).thenThrow(new RuntimeException());

        // Perform the bootstrap operation and verify the expected exception
        ResponseStatusException exception = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class,
                () -> sourceController.bootstrap(projectId, datasetName, datasetDescription, attach_file));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("An error occurred while creating the data source", exception.getReason());

        verify(sourceService).reconstructFile(attach_file);
        verifyNoMoreInteractions(sourceService);
    }
}
