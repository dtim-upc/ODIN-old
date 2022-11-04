package edu.upc.essi.dtim.odin.newBootstrapping;

import edu.upc.essi.dtim.odin.projects.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/datasource")
public class newDataSourceController {


    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private final String EMPTY_INPUTS = "{}";

    @Autowired
    private newDataSourceService dataSourceService;

    @Autowired
    private newDataSourceRepository dataSourceRepo;

    @PostMapping( consumes = {"multipart/form-data"})
    public ResponseEntity<newDataSource> createDataSource(@RequestPart newDataSource dataSource, @RequestPart MultipartFile file) {
        LOGGER.debug("inside of createDataSource method");
        try {
            newDataSource _dataSource = dataSourceService.create(dataSource, file);

            String input = dataSource.toString().replaceAll("[\n\r\t]", "_");
            String returnval = _dataSource.toString().replaceAll("[\n\r\t]", "_");

            LOGGER.info(LOG_MSG, "createDataSource", input, returnval);
            return new ResponseEntity<>(_dataSource, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("persist")
//    public ResponseEntity<newDataSource> persistDataSource(@RequestBody newDataSource dataSource) {
//        try {
//            LOGGER.debug("inside of persist data source");
//            dataSourceService.persist(dataSource);
//            return new ResponseEntity<>(dataSource, HttpStatus.CREATED);
//        } catch (Exception e) {
//            LOGGER.error("something went wrong in persistDataSource()", e);
//            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDataSources(@PathVariable("id") String id) throws IOException {
        // TODO: think how to be sure graph is deleted. Maybe after delete it, check if exist?
        dataSourceService.delete(id);
        LOGGER.info(LOG_MSG, "deleteDataSources", id, HttpStatus.NO_CONTENT.toString() );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<newDataSource>> getAllDataSources() {
//        System.out.println("Project is " + p.getCreatedBy());
        try {
            List<newDataSource> dataSources = dataSourceRepo.findAll();
            if (dataSources.isEmpty()) {
                ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                LOGGER.info(LOG_MSG, "getAllDataSources", "", response);
                return response;
            }
            LOGGER.info(LOG_MSG, "getAllDataSources", EMPTY_INPUTS, "" );
            return new ResponseEntity<>(dataSources, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
