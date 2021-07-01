package edu.upc.essi.dtim.metadatastorage.controller.manualBootstraping;

import edu.upc.essi.dtim.metadatastorage.controller.AdminController;
import edu.upc.essi.dtim.metadatastorage.models.Wrapper;
import edu.upc.essi.dtim.metadatastorage.repository.WrapperRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/wrapper")
public class WrapperController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private static final String EMPTY_INPUTS = "{}";

    @Autowired
    private WrapperRepository repository;

    @PostMapping
    public ResponseEntity<Wrapper> createWrapper(@RequestBody Wrapper wrapper) {
        try {
            Wrapper _wrapper = new Wrapper(wrapper.getName(), wrapper.getAttributes(), wrapper.getDataSourcesId());
            repository.save(_wrapper);
            LOGGER.info(LOG_MSG, "createDataSources", wrapper.toString(), _wrapper.toString() );
            return new ResponseEntity<>(_wrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Wrapper>> getAllWrappers() {

        try {
            List<Wrapper> wrappers = new ArrayList<Wrapper>();

            repository.findAll().forEach(wrappers::add);

            if (wrappers.isEmpty()) {
                ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                LOGGER.info(LOG_MSG, "getAllWrappers", "", response);
                return response;
            }
            LOGGER.info(LOG_MSG, "getAllWrappers", EMPTY_INPUTS, "" );
            return new ResponseEntity<>(wrappers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteWrapper(@PathVariable("id") String id) {
        try {
            repository.deleteById(id);
            LOGGER.info(LOG_MSG, "deleteWrapper", id, HttpStatus.NO_CONTENT.toString() );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteWrapper() {
        try {
            repository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
