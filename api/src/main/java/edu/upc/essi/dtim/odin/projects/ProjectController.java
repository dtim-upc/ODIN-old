package edu.upc.essi.dtim.odin.projects;

import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private static final String EMPTY_INPUTS = "{}";

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepo;

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project){
        // TODO: might need some validation of user credentials
        Project _project = projectService.create(project);
        return new ResponseEntity<>(_project, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") String id){
        System.out.println("getProject()");
        Project p = projectRepo.findByID(id);
        if(p == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Project does not exist");
        }
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(Authentication authentication){

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Map<String, Object> attributes = token.getTokenAttributes();

        List<Project> projects = projectRepo.findAll(attributes.get("username").toString());

        LOGGER.info(LOG_MSG, "getAllProjects", EMPTY_INPUTS, "" );
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    public void updateProject(){

    }

    public void deleteProject(){

    }

}
