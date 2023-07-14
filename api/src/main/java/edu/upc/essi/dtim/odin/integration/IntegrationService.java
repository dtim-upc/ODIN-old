package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.odin.NextiaStore.RelationalStore.ORMStoreFactory;
import edu.upc.essi.dtim.odin.config.AppConfig;
import edu.upc.essi.dtim.odin.project.Project;
import edu.upc.essi.dtim.odin.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegrationService {
    public IntegrationService(){

    }

    public void integrateData() {
    }

    public Project getProject(String projectId) {
        ProjectService projectService = new ProjectService();
        return projectService.findById(projectId);
    }
}
