package edu.upc.essi.dtim.ODINBackend.controller.manualBootstraping;

import edu.upc.essi.dtim.ODINBackend.controller.AdminController;
import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import edu.upc.essi.dtim.ODINBackend.models.LavMapping;
import edu.upc.essi.dtim.ODINBackend.repository.LavMappingRepository;
import edu.upc.essi.dtim.ODINBackend.models.LavMappingSubgraph;

import edu.upc.essi.dtim.ODINBackend.services.impl.LAVMappingService;
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
    @Autowired
    private LAVMappingService lavMappingService;

    @GetMapping
    public ResponseEntity<List<LavMapping>> getAllLavMappings() {

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
    @PutMapping("id")
    public ResponseEntity<HttpStatus> editDataSources(@PathVariable("id") String id, @RequestBody LavMapping lavMapping) {
        try {
            Optional<LavMapping> optionalLavMapping = repository.findById(id);
            if (optionalLavMapping.isPresent()) {
                LavMapping lm  = optionalLavMapping.get();
                lm.setSameAs(lm.getSameAs());
                lm.setGraphicalSubgraph(lm.getGraphicalSubgraph());
                lm.setWrapperId(lm.getWrapperId());
                lm.setGlobalGraphId(lm.getGlobalGraphId());
                repository.save(lm);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    @PostMapping
    public ResponseEntity<LavMapping> createLavMapping(@RequestBody LavMapping lavmapping) {
        try {
            LavMapping _lavmapping = new LavMapping(lavmapping.getGlobalGraphId(),
                                                                        lavmapping.getWrapperId(),
                                                                        lavmapping.getSameAs(),
                                                                        lavmapping.getGraphicalSubgraph());
            lavMappingService.createLAVMappingMapsTo(_lavmapping);
            String input = lavmapping.toString().replaceAll("[\n\r\t]", "_");
            String returnval = _lavmapping.toString().replaceAll("[\n\r\t]", "_");
            LOGGER.info(LOG_MSG, "createGlobalGraph", input, returnval );
            return new ResponseEntity<>(_lavmapping, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLavMapping(@PathVariable("id") String id) {
        System.out.println(id);
        try {
            repository.deleteById(id);
            LOGGER.info(LOG_MSG, "deleteGlobalGraph", id, HttpStatus.NO_CONTENT.toString() );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/subgraph")
    public ResponseEntity<HttpStatus> saveSubGraph(@RequestBody LavMappingSubgraph lavMappingSubgraph) {
        Optional<LavMapping> optionalLavMapping = repository.findById(lavMappingSubgraph.getLAVMappingID());
        if (optionalLavMapping.isPresent()) {
            LavMapping lavMapping = optionalLavMapping.get();
            lavMapping.setGraphicalSubgraph(String.join(", ",  lavMappingSubgraph.getGraphicalSubGraph()));
            repository.save(lavMapping);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
