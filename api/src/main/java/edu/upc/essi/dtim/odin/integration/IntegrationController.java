package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.odin.integration.pojos.IntegrationData;
import edu.upc.essi.dtim.odin.integration.pojos.IntegrationTemporalResponse;
import edu.upc.essi.dtim.odin.integration.pojos.JoinAlignment;
import edu.upc.essi.dtim.odin.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IntegrationController {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationController.class);

    private final IntegrationService integrationService;

    /**
     * Constructs a new instance of IntegrationController.
     *
     * @param integrationService the IntegrationService dependency for performing integration operations
     */
    IntegrationController(@Autowired IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @PostMapping(value = "/project/{id}/integration")
    public ResponseEntity<IntegrationTemporalResponse> integrate(@PathVariable("id") String projectId,
                                                                 @RequestBody IntegrationData iData) {
        logger.info("INTEGRATING temporal with project: "+projectId);

        Project project = integrationService.getProject(projectId);

        //miramos si hay datasets suficientes a integrar en el proyecto
        if(project.getDatasets().size() > 1){
            //integramos la nueva fuente de datos sobre el grafo integrado existente y lo sobreescrivimos
            Graph integratedGraph = integrationService.integrateData(project.getIntegratedGraph(), iData.getDsB(), iData.getAlignments());

            String path = "C:\\Users\\victo\\Documents\\GitHub\\newODIN\\api\\dbFiles\\ttl\\";
            project.getIntegratedGraph().write(path+"graphA.ttl");
            iData.getDsB().getLocalGraph().write(path+"graphB.ttl");
            integratedGraph.write(path+"integrated");

            Project projectToSave = integrationService.updateGraphProject(project, integratedGraph);

            checkGraphicalSchema(project.getIntegratedGraph().getGraphicalSchema(), integratedGraph.getGraphicalSchema(), projectToSave.getIntegratedGraph().getGraphicalSchema());
            checkGraphicalSchema(project.getIntegratedGraph().getGraphicalSchema(), integratedGraph.getGraphicalSchema(), iData.getDsB().getLocalGraph().getGraphicalSchema());

            Project project1 = integrationService.saveProject(projectToSave);
            logger.info("PROJECT SAVED WITH THE NEW INTEGRATED GRAPH");

            List<JoinAlignment> joinProperties =  integrationService.generateJoinAlignments(project.getIntegratedGraph(), iData.getDsB().getLocalGraph(), iData);

            return new ResponseEntity<>(new IntegrationTemporalResponse(project1, joinProperties), HttpStatus.OK);
        }
        //si no hay suficientes ERROR
        else{
            return new ResponseEntity<>(new IntegrationTemporalResponse(null,null), HttpStatus.BAD_REQUEST);
        }
    }

    private static void checkGraphicalSchema(String project, String integratedGraph, String projectToSave) {
        String oldGraphicalSchema = project;
        String newGraphicalSchema = integratedGraph;
        String newGeneratedSchema = projectToSave;

        if (oldGraphicalSchema.equals(newGraphicalSchema) && newGraphicalSchema.equals(newGeneratedSchema)) {
            // All three strings are the same
            logger.warn("All three graphical schemas are the same.");
        } else if (oldGraphicalSchema.equals(newGraphicalSchema)) {
            // The oldGraphicalSchema and newGraphicalSchema are the same, while newGeneratedSchema is different
            logger.warn("The oldGraphicalSchema and newGraphicalSchema are the same, while newGeneratedSchema is different.");
        } else if (oldGraphicalSchema.equals(newGeneratedSchema)) {
            // The oldGraphicalSchema and newGeneratedSchema are the same, while newGraphicalSchema is different
            logger.warn("The oldGraphicalSchema and newGeneratedSchema are the same, while newGraphicalSchema is different.");
        } else if (newGraphicalSchema.equals(newGeneratedSchema)) {
            // The newGraphicalSchema and newGeneratedSchema are the same, while oldGraphicalSchema is different
            logger.warn("The newGraphicalSchema and newGeneratedSchema are the same, while oldGraphicalSchema is different.");
        } else {
            // All three strings are different
            logger.info("All three graphical schemas are different.");
        }
    }

    @PostMapping(value = "/project/{id}/integration/persist")
    public ResponseEntity<Project> acceptIntegration(@PathVariable("id") String id) {
        System.out.println("entering saving integration persistent");
        //todo: delete this call
        return new ResponseEntity(integrationService.getProject(id), HttpStatus.CREATED);
    }
}