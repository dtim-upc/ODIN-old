package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.CsvDataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.JsonDataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.LocalGraph;
import edu.upc.essi.dtim.NextiaCore.graph.Triple;
import edu.upc.essi.dtim.NextiaCore.graph.URI;
import edu.upc.essi.dtim.nextiadi.bootstraping.CSVBootstrap;
import edu.upc.essi.dtim.nextiadi.bootstraping.JSONBootstrapSWJ;
import edu.upc.essi.dtim.odin.NextiaGraphy.NextiaGraphy;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreInterface;
import edu.upc.essi.dtim.odin.NextiaStore.RelationalStore.ORMStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.RelationalStore.ORMStoreInterface;
import edu.upc.essi.dtim.odin.config.AppConfig;
import edu.upc.essi.dtim.odin.project.ProjectService;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The service class for managing datasources in a project.
 */
@Service
public class SourceService {
    /**
     * The dependency on the ProjectService class.
     */
    private final ProjectService projectService;
    /**
     * The AppConfig dependency for accessing application configuration.
     */
    private final AppConfig appConfig;
    /**
     * The ORMStoreInterface dependency for storing datasets.
     */
    private final ORMStoreInterface ormDataset;
    /**
     * Constructs a new instance of SourceService.
     *
     * @param appConfig      The AppConfig dependency for accessing application configuration.
     * @param projectService The ProjectService dependency.
     */
    public SourceService(@Autowired AppConfig appConfig,
                         @Autowired ProjectService projectService){
        this.appConfig = appConfig;
        this.projectService = projectService;
        try {
            this.ormDataset = ORMStoreFactory.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stores a multipart file in the specified disk path and returns the absolute path of the file.
     *
     * @param multipartFile The multipart file to store.
     * @return The absolute path of the stored file.
     */
    public String reconstructFile(MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }

            final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuilder sb = new StringBuilder(16);
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < 16; i++) {
                int randomIndex = random.nextInt(characters.length());
                sb.append(characters.charAt(randomIndex));
            }

            String filename = sb +"_"+ multipartFile.getOriginalFilename();

            // Get the disk path from the app configuration
            Path diskPath = Path.of(appConfig.getDiskPath());

            // Resolve the destination file path using the disk path and the generated filename
            Path destinationFile = diskPath.resolve(Paths.get(filename));

            // Perform a security check to ensure that the destination file is within the disk path
            if (!destinationFile.getParent().equals(diskPath)) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            // Copy the input stream of the multipart file to the destination file
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, destinationFile,StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    /**
     * Extracts data from a file and returns a Dataset object with the extracted data.
     *
     * @param filePath           The path of the file to extract data from.
     * @param datasetName        The name of the dataset.
     * @param datasetDescription The description of the dataset.
     * @return A Dataset object with the extracted data.
     */
    public Dataset extractData(String filePath, String datasetName, String datasetDescription) {
        // Extract the extension of the file from the file path
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);

        Dataset dataset;

        // Create a new dataset object with the extracted data
        switch (extension.toLowerCase()){
            case "csv":
                dataset = new CsvDataset(null, datasetName, datasetDescription, filePath);
                break;
            case "json":
                dataset = new JsonDataset(null, datasetName, datasetDescription, filePath);
                break;
            default:
                throw new IllegalArgumentException("Unsupported file format: " + extension);
        }

        return dataset;
    }

    /**
     * Transforms a Dataset object into a Graph object representing the data in RDF format.
     *
     * @param dataset The Dataset object to transform.
     * @return A GraphModelPair object containing the transformed Graph and the corresponding Model.
     */
    public GraphModelPair transformToGraph(Dataset dataset) {
        try {
            Model bootstrapM = convertDatasetToModel(dataset);
            Graph bootstrappedGraph = adapt(bootstrapM, new URI(dataset.getDatasetName()));
            return new GraphModelPair(bootstrappedGraph, bootstrapM);
        } catch (UnsupportedOperationException e) {
            return handleUnsupportedDatasetFormat(dataset);
        }
    }

    Model convertDatasetToModel(Dataset dataset) {
        Model bootstrapM = ModelFactory.createDefaultModel();
        if (dataset.getClass().equals(CsvDataset.class)) {
            CSVBootstrap bootstrap = new CSVBootstrap();
            try {
                bootstrapM = bootstrap.bootstrapSchema(dataset.getDatasetId(), dataset.getDatasetName(), ((CsvDataset) dataset).getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (dataset.getClass().equals(JsonDataset.class)) {
            JSONBootstrapSWJ j = new JSONBootstrapSWJ();
            try {
                bootstrapM = j.bootstrapSchema(dataset.getDatasetName(), dataset.getDatasetId(), ((JsonDataset) dataset).getPath());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return bootstrapM;
    }

    GraphModelPair handleUnsupportedDatasetFormat(Dataset dataset) {
        Graph errorGraph = new LocalGraph(null, new URI("ERROR"), new HashSet<>());
        errorGraph.addTriple(new Triple(
                new URI(dataset.getDatasetId()),
                new URI("https://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
                new URI("https://example.org/error#UnsupportedDatasetFormat")
        ));

        return new GraphModelPair(errorGraph, null);
    }


    /**
     * Generates a visual representation of a Graph using NextiaGraphy library.
     *
     * @param graph The GraphModelPair object containing the Graph.
     * @return A String representing the visual schema of the Graph.
     */
    public String generateVisualSchema(GraphModelPair graph) {
        NextiaGraphy visualLib = new NextiaGraphy();
        return visualLib.generateVisualGraphNew(graph.getModel());
    }

    /**
     * Saves a Graph object to the database using a GraphStoreInterface.
     *
     * @param graph The Graph object to save.
     * @return A boolean indicating whether the saving operation was successful.
     */
    public boolean saveGraphToDatabase(GraphModelPair graph) {
        GraphStoreInterface graphStore;
        try {
            graphStore = GraphStoreFactory.getInstance(appConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assert graphStore != null;
        graphStore.saveGraph(graph);
        return true;
    }

    /**
     * Adds a dataset ID to a project using the ProjectService class.
     *
     * @param projectId The ID of the project to add the dataset ID to.
     * @param dataset   The Dataset object to add the ID of.
     */
    public void addDatasetIdToProject(String projectId, Dataset dataset) {
        projectService.addDatasetIdToProject(projectId, dataset);
    }

    /**
     * Deletes a dataset from a project using the ProjectService class.
     *
     * @param projectId The ID of the project to delete the dataset from.
     * @param datasetId The ID of the dataset to delete.
     */
    public void deleteDatasetFromProject(String projectId, String datasetId) {
        projectService.deleteDatasetFromProject(projectId, datasetId);
    }

    /**
     * Saves a Dataset object using the ORMStoreInterface.
     *
     * @param dataset The Dataset object to save.
     * @return The saved Dataset object.
     */
    public Dataset saveDataset(Dataset dataset) {
        return ormDataset.save(dataset);
    }

    /**
     * Retrieves all datasets from the ORMStoreInterface.
     *
     * @return A list of Dataset objects.
     */
    public List<Dataset> getDatasets() {
        return ormDataset.getAll(Dataset.class);
    }

    /**
     * Deletes a datasource from the ORMStoreInterface. NOT USED. When deleteDatasetFromProject(...) cascade all does this implicit.
     *
     * @param id The ID of the datasource to delete.
     * @return A boolean indicating whether the deletion was successful.
     */
    public boolean deleteDatasource(String id) {
        return ormDataset.deleteOne(Dataset.class, id);
    }

    /**
     * Checks if a project contains a specific dataset using the ProjectService class.
     *
     * @param projectId The ID of the project to check.
     * @param id        The ID of the dataset to check.
     * @return A boolean indicating whether the project contains the dataset.
     */
    public boolean projectContains(String projectId, String id) {
        return projectService.projectContains(projectId, id);
    }

    /**
     * Retrieves all datasets of a project using the ProjectService class.
     *
     * @param id The ID of the project to retrieve datasets from.
     * @return A list of Dataset objects associated with the project.
     */
    public List<Dataset> getDatasetsOfProject(String id) {
        // Returning the list of datasets associated with the project
        return projectService.getDatasetsOfProject(id);
    }


    /**
     * Adapts a Model object into a Graph object with Triples.
     *
     * @param model The Model object to adapt.
     * @param name  The name of the Graph.
     * @return The adapted Graph object.
     */
    private Graph adapt(Model model, URI name) {
        Set<Triple> triples = new HashSet<>();

        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            triples.add(new Triple(
                    new URI(stmt.getSubject().getURI()),
                    new URI(stmt.getPredicate().getURI()),
                    new URI(stmt.getObject().isLiteral() ? stmt.getObject().asLiteral().toString():stmt.getObject().asResource().getURI())
            ));
        }

        return new LocalGraph(null, name, triples);
    }
}


