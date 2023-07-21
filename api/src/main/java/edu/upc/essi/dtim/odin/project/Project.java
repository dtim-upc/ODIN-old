package edu.upc.essi.dtim.odin.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.jena.GraphJenaImpl;

import java.util.List;

public class Project {

    private String projectId;
    private String projectName;
    private String projectDescription;
    private String projectPrivacy;
    private String projectColor;
    private String createdBy;
    private List<Dataset> datasets;
    private GraphJenaImpl integratedGraph;
    private GraphJenaImpl globalGraph;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectPrivacy() {
        return projectPrivacy;
    }

    public void setProjectPrivacy(String projectPrivacy) {
        this.projectPrivacy = projectPrivacy;
    }

    public String getProjectColor() {
        return projectColor;
    }

    public void setProjectColor(String projectColor) {
        this.projectColor = projectColor;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<Dataset> datasets) {
        this.datasets = datasets;
    }
    public GraphJenaImpl getIntegratedGraph() {
        return integratedGraph;
    }

    public void setIntegratedGraph(GraphJenaImpl integratedGraph) {
        this.integratedGraph = integratedGraph;
    }

    public GraphJenaImpl getGlobalGraph() {
        return globalGraph;
    }

    public void setGlobalGraph(GraphJenaImpl globalGraph) {
        this.globalGraph = globalGraph;
    }
}
