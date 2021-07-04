package edu.upc.essi.dtim.metadatastorage.controller.manualBootstraping;

import edu.upc.essi.dtim.metadatastorage.controller.AdminController;
import edu.upc.essi.dtim.metadatastorage.models.DataSources;
import edu.upc.essi.dtim.metadatastorage.models.GlobalGraph;
import edu.upc.essi.dtim.metadatastorage.repository.GlobalGraphRespository;
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
@RequestMapping("/globalGraph")
public class GlobalGraphController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private static final String EMPTY_INPUTS = "{}";

    @Autowired
    private GlobalGraphRespository repository;

    @GetMapping
    public ResponseEntity<List<GlobalGraph>> getAllGlobalGraphs() {

        try {
            List<GlobalGraph> globalGraphs = new ArrayList<GlobalGraph>();

            repository.findAll().forEach(globalGraphs::add);

            if (globalGraphs.isEmpty()) {
                ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                LOGGER.info(LOG_MSG, "getAllGlobalGraphs", "", response);
                return response;
            }
            LOGGER.info(LOG_MSG, "getAllGlobalGraphs", EMPTY_INPUTS, "" );
            return new ResponseEntity<>(globalGraphs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<GlobalGraph> createGlobalGraph(@RequestBody GlobalGraph globalGraph) {
        try {
            GlobalGraph _globalGraph = repository.save(new GlobalGraph(globalGraph.getName(), globalGraph.getNamespace() ));
            LOGGER.info(LOG_MSG, "createGlobalGraph", globalGraph.toString(), _globalGraph.toString() );
            return new ResponseEntity<>(_globalGraph, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<HttpStatus> editGlobalGraph(@PathVariable("id") String id, @RequestBody GlobalGraph globalGraph) {
        try {
            Optional<GlobalGraph> optionalGlobalGraph = repository.findById(id);
            if (optionalGlobalGraph.isPresent()) {
                GlobalGraph gg = optionalGlobalGraph.get();
                gg.setName(globalGraph.getName());
                gg.setNamespace(globalGraph.getNamespace());
                gg.setNamedGraph(globalGraph.getNamedGraph());
                gg.setGraphicalGraph(globalGraph.getGraphicalGraph());
                repository.save(gg);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalGraph> updateGlobalGraph(@PathVariable("id") String id, @RequestBody GlobalGraph globalGraph) {
        Optional<GlobalGraph> tutorialData = repository.findById(id);

        if (tutorialData.isPresent()) {
            GlobalGraph _globalGraph = tutorialData.get();
            _globalGraph.setName(globalGraph.getName());
            _globalGraph.setGraphicalGraph(globalGraph.getGraphicalGraph());
            _globalGraph.setNamedGraph(globalGraph.getNamedGraph());
            _globalGraph.setNamespace(globalGraph.getNamespace());
            return new ResponseEntity<>(repository.save(_globalGraph), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteGlobalGraph(@PathVariable("id") String id) {
        try {
            repository.deleteById(id);
            LOGGER.info(LOG_MSG, "deleteGlobalGraph", id, HttpStatus.NO_CONTENT.toString() );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllGlobalGraphs() {
        try {
            repository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
