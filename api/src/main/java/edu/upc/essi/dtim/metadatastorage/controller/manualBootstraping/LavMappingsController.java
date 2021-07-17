package edu.upc.essi.dtim.metadatastorage.controller.manualBootstraping;

import edu.upc.essi.dtim.metadatastorage.controller.AdminController;
import edu.upc.essi.dtim.metadatastorage.models.GlobalGraph;
import edu.upc.essi.dtim.metadatastorage.models.LavMapping;
import edu.upc.essi.dtim.metadatastorage.repository.LavMappingRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lavMapping")
public class LavMappingsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private static final String EMPTY_INPUTS = "{}";

    @Autowired
    private LavMappingRepository repository;

    @GetMapping
    public ResponseEntity<List<LavMapping>> getAllGlobalGraphs() {

        try {
            List<LavMapping> lavMappings = new ArrayList<LavMapping>();

            repository.findAll().forEach(lavMappings::add);

            if (lavMappings.isEmpty()) {
                ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                LOGGER.info(LOG_MSG, "getAllLavMappings", "", response);
                return response;
            }
            LOGGER.info(LOG_MSG, "getAllLavMappings", EMPTY_INPUTS, "" );
            return new ResponseEntity<>(lavMappings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<LavMapping> createLavMapping(@RequestBody LavMapping lavmapping) {
        try {
            LavMapping _lavmapping = repository.save(new LavMapping(lavmapping.getGlobalGraphId(), lavmapping.getWrapperId() ));
            LOGGER.info(LOG_MSG, "createGlobalGraph", lavmapping.toString(), _lavmapping.toString() );
            return new ResponseEntity<>(_lavmapping, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
