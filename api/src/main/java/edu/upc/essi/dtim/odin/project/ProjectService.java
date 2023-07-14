package edu.upc.essi.dtim.odin.project;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.pruebaORMinterface.GraphJenaImpl;
import edu.upc.essi.dtim.odin.NextiaStore.RelationalStore.ORMStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.RelationalStore.ORMStoreInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    ORMStoreInterface ormProject;

    /**
     * Constructs a new ProjectService.
     */
    public ProjectService() {
        try {
            this.ormProject = ORMStoreFactory.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a dataset ID to the specified project.
     *
     * @param projectId The ID of the project to add the dataset ID to.
     * @param dataset The dataset to add.
     * @throws IllegalArgumentException If the project with the given ID is not found.
     */
    public void addDatasetIdToProject(String projectId, Dataset dataset) {
        // Retrieve the project with the given ID
        Project project = findById(projectId);

        // If the project is not found, throw an exception
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        // Add the URI of the local graph to the project's list of local graph IDs
        project.getDatasets().add(dataset);

        if(project.getDatasets().size() == 1){
            Graph globalGraph = new GraphJenaImpl();
            globalGraph.setGraphicalSchema(dataset.getLocalGraph().getGraphicalSchema());
            project.setIntegratedGraph(((GraphJenaImpl) globalGraph));
        }

        //saving the updated project
        saveProject(project);
    }

    /**
     * Deletes a dataset from the specified project.
     *
     * @param projectId The ID of the project to delete the dataset from.
     * @param datasetId The ID of the dataset to delete.
     * @throws IllegalArgumentException If the project with the given ID is not found.
     */
    public void deleteDatasetFromProject(String projectId, String datasetId) {
        Project project = findById(projectId);

        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        List<Dataset> datasetsOfProjectToUpload = project.getDatasets();
        boolean datasetFound = false;
        for (Dataset datasetInProject : datasetsOfProjectToUpload) {
            if (datasetId.equals(datasetInProject.getDatasetId())) {
                datasetFound = true;
                datasetsOfProjectToUpload.remove(datasetInProject);
                project.setDatasets(datasetsOfProjectToUpload);
                break; // Rompemos el bucle después de eliminar el objeto
            }
        }
        if(!datasetFound) {
            throw new IllegalArgumentException("Dataset not found");
        }

        saveProject(project);
    }

    /**
     * Saves a project.
     *
     * @param project The project to save.
     * @return The saved project.
     */
    public Project saveProject(Project project) {
        return ormProject.save(project);
    }

    /**
     * Finds a project by its ID.
     *
     * @param projectId The ID of the project to find.
     * @return The found project, or null if not found.
     */
    public Project findById(String projectId) {
        return ormProject.findById(Project.class, projectId);
    }

    /**
     * Retrieves all projects.
     *
     * @return A list of all projects.
     */
    public List<Project> getAllProjects() {
        return ormProject.getAll(Project.class);
    }

    /**
     * Deletes a project by its ID.
     *
     * @param id The ID of the project to delete.
     * @return true if the project was deleted successfully, false otherwise.
     */
    public boolean deleteProject(String id) {
        return ormProject.deleteOne(Project.class, id);
    }

    /**
     * Checks if a project contains a dataset with the given ID.
     *
     * @param projectId The ID of the project to check.
     * @param datasetId The ID of the dataset to check.
     * @return true if the project contains the dataset, false otherwise.
     */
    public boolean projectContains(String projectId, String datasetId) {
        Project project = ormProject.findById(Project.class, projectId);
        for (Dataset datasetInProject : project.getDatasets()) {
            if (datasetId.equals(datasetInProject.getDatasetId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the datasets of a project.
     *
     * @param id The ID of the project.
     * @return A list of datasets belonging to the project.
     */
    public List<Dataset> getDatasetsOfProject(String id) {
        Project project = ormProject.findById(Project.class, id);
        return project.getDatasets();
    }
}

