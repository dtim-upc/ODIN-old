package edu.upc.essi.dtim.odin.newBootstrapping;

import edu.upc.essi.dtim.odin.projects.Project;
import edu.upc.essi.dtim.odin.projects.ProjectRepository;
import org.apache.jena.tdb.TDB;
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
@RequestMapping("/project/{id}/temp/ds")
public class nTemporalDSController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private final String EMPTY_INPUTS = "{}";

    @Autowired
    private newDataSourceService dataSourceService;

    @Autowired
    private newDataSourceRepository dataSourceRepo;

    @Autowired
    ProjectRepository projectRepo;


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



    @PostMapping( consumes = {"multipart/form-data"})
    public ResponseEntity<newDataSource> createDataSource(Authentication authentication, @PathVariable("id") String id, @RequestPart newDataSource dataSource, @RequestPart MultipartFile file) {
        LOGGER.debug("inside of createDataSource method");
        Project p = validateAccess(id, authentication);
        // just for security
        dataSource.setProjectID(id);
        try {
            newDataSource _dataSource = dataSourceService.create(dataSource, file);

            String input = dataSource.toString().replaceAll("[\n\r\t]", "_");
            String returnval = _dataSource.toString().replaceAll("[\n\r\t]", "_");

            LOGGER.info(LOG_MSG, "createDataSource", input, returnval);

            Boolean flag = dataSourceRepo.existsNotEmpty(_dataSource);

            if(!flag){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Data source not created sucessfully");
            }

//            projectRepo.getTemporalG(_dataSource.getIri()).write("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/"+dataSource.getName()+"_"+p.getCreatedBy()+".ttl");
            System.out.println("file written temporal");
//            System.out.println( graph.temporal().getGraph(dataSource.getIri()).size());




            return new ResponseEntity<>(_dataSource, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{dsID}")
    public ResponseEntity<HttpStatus> deleteDataSources(Authentication authentication, @PathVariable("id") String id, @PathVariable("dsID") String dsID) throws IOException {
        // TODO: think how to be sure graph is deleted. Maybe after delete it, check if exist?
        validateAccess(id, authentication);

        dataSourceService.deleteTemporal(dsID);
        LOGGER.info(LOG_MSG, "deleteDataSources temporal", id, HttpStatus.NO_CONTENT.toString() );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping
    public ResponseEntity<List<newDataSource>> getAllTemporalDataSources(Authentication authentication, @PathVariable("id") String id) {
        validateAccess(id, authentication);

        try {
            List<newDataSource> dataSources = dataSourceRepo.findAllTemporal(id);
            if (dataSources.isEmpty()) {
                ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                LOGGER.info(LOG_MSG, "getAllTemporalDataSources from project", "", response);
                return response;
            }
            LOGGER.info(LOG_MSG, "getAllTemporalDataSources from project", EMPTY_INPUTS, "" );
            return new ResponseEntity<>(dataSources, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
