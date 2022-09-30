package edu.upc.essi.dtim.odin.newBootstrapping;

import edu.upc.essi.dtim.odin.projects.Project;
import edu.upc.essi.dtim.odin.projects.ProjectRepository;
import edu.upc.essi.dtim.odin.projects.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project/{id}/datasources")
public class PersistentDataSourceController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private final String EMPTY_INPUTS = "{}";

    @Autowired
    private newDataSourceService dataSourceService;

    @Autowired
    private newDataSourceRepository dataSourceRepo;

    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private ProjectService projectService;

    // maybe this can be done with @preauthorize isresourceowner
    // https://stackoverflow.com/questions/70096839/how-to-check-security-access-before-validation-valid-in-controller
    public Project validateAccess(String projectID, Authentication authentication){
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Map<String, Object> attributes = token.getTokenAttributes();
        Project project = projectRepo.findByID(projectID);
        // validate access
        if(!project.getCreatedBy().equals(attributes.get("username").toString())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not have accessed to this project");
        }
        return project;
    }


    @PostMapping("persist")
    public ResponseEntity<newDataSource> persistDataSource(Authentication authentication, @PathVariable("id") String id, @RequestBody newDataSource dataSource) {
        System.out.println("Persist data source...");
        Project project = validateAccess(id, authentication);
        // just for security
        dataSource.setProjectID(id);
        dataSourceService.persist(dataSource);


        if(project.getNumberOfDS().equals("0") ) {
            // if this is gonna be the first data source, we assign its source schema to the project schema (global)
            projectService.updateGraphicalSchema(project, dataSource.getGraphicalSchema());
        }
        projectService.increaseNumberDSBy1(project);

        return new ResponseEntity<>(dataSource, HttpStatus.CREATED);
    }


//    @PostMapping( consumes = {"multipart/form-data"})
//    public ResponseEntity<newDataSource> createDataSource(Authentication authentication, @PathVariable("id") String id, @RequestPart newDataSource dataSource, @RequestPart MultipartFile file) {
//        LOGGER.debug("inside of createDataSource method");
//        validateAccess(id, authentication);
//        // just for security
//        dataSource.setProjectID(id);
//        try {
//            newDataSource _dataSource = dataSourceService.create(dataSource, file);
//
//            String input = dataSource.toString().replaceAll("[\n\r\t]", "_");
//            String returnval = _dataSource.toString().replaceAll("[\n\r\t]", "_");
//
//            LOGGER.info(LOG_MSG, "createDataSource", input, returnval);
//            return new ResponseEntity<>(_dataSource, HttpStatus.CREATED);
//        } catch (Exception e) {
//            LOGGER.error(e.toString(), e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping
    public ResponseEntity<List<newDataSource>> getAllDataSources(Authentication authentication, @PathVariable("id") String id) {
//        System.out.println("Project is " + p.getCreatedBy());

        validateAccess(id, authentication);

        try {
            List<newDataSource> dataSources = dataSourceRepo.findAll(id);
            if (dataSources.isEmpty()) {
                ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                LOGGER.info(LOG_MSG, "getAllDataSources from project", "", response);
                return response;
            }
            LOGGER.info(LOG_MSG, "getAllDataSources from project", EMPTY_INPUTS, "" );
            return new ResponseEntity<>(dataSources, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{dsID}")
    public ResponseEntity<HttpStatus> deleteDataSources(Authentication authentication, @PathVariable("id") String id, @PathVariable("dsID") String dsID) throws IOException {
        System.out.println("delete persistent data source...");
        Project project = validateAccess(id, authentication);
        // TODO: think how to be sure graph is deleted. Maybe after delete it, check if exist?
        // TODO: RESET GLOBAL SCHEMA IF NO DATA SOURCES LEFT
        dataSourceService.delete(dsID);
        projectService.decreaseNumberDSBy1(project);

        LOGGER.info(LOG_MSG, "deleteDataSources", dsID, HttpStatus.NO_CONTENT.toString() );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
