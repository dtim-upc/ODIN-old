package edu.upc.essi.dtim.odin.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepo;

    public Project create(Project project){

        // this generates an id for the project
        Project _project = new Project();
        _project.setName(project.getName());
        _project.setColor(project.getColor());
        _project.setDescription(project.getDescription());
        _project.setPrivacy(project.getPrivacy());
        _project.setCreatedBy(project.getCreatedBy());

        return projectRepo.save(project);
    }

    public Project increaseNumberDSBy1(Project project) {


        int nDS = Integer. parseInt(project.getNumberOfDS()) + 1;
        return projectRepo.updateNumberDataSources(project, nDS+"");

    }

    public Project decreaseNumberDSBy1(Project project) {
        int nDS = Integer. parseInt(project.getNumberOfDS()) - 1;
        //TODO: check if it's possible to get -1, some tests are required!!
        if(nDS < 0) {
            nDS = 0;
        }
        Project newP =  projectRepo.updateNumberDataSources(project, nDS+"");
        if(newP.getNumberOfDS().equals("0") ) {
            updateGraphicalSchema(project,"");
            updateGraphicalSchemaIntegration(project, "");
        }
        return newP;
    }

    public Project updateGraphicalSchema(Project project, String newGraphicalSchema) {
        return projectRepo.updateGraphicalGlobalSchema(project, newGraphicalSchema);
    }

    public Project updateGraphicalSchemaIntegration(Project project, String newGraphicalSchemaIntegration) {
        return projectRepo.updateGraphicalIntegratedSchema(project, newGraphicalSchemaIntegration);
    }

}
