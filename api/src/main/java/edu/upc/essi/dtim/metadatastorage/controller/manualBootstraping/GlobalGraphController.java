package edu.upc.essi.dtim.metadatastorage.controller.manualBootstraping;

import edu.upc.essi.dtim.metadatastorage.controller.AdminController;
import edu.upc.essi.dtim.metadatastorage.models.DataSources;
import edu.upc.essi.dtim.metadatastorage.models.GlobalGraph;
import edu.upc.essi.dtim.metadatastorage.models.GlobalGraphUpdate;
import edu.upc.essi.dtim.metadatastorage.repository.GlobalGraphRespository;
import org.bson.json.JsonObject;
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

    @GetMapping("/view/{id}")
    public ResponseEntity<GlobalGraph> getGlobalGraph(@PathVariable("id") String id) {

        try {
            Optional<GlobalGraph> optionalGlobalGraph = repository.findById(id);
            if (optionalGlobalGraph.isPresent()) {
                LOGGER.info(LOG_MSG, "getGlobalGraph", id, "" );
                return new ResponseEntity<>(optionalGlobalGraph.get(), HttpStatus.OK);
            }
            LOGGER.info(LOG_MSG, "getGlobalGraphs", id, "" );
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalGraph> getGraphicalGraph(@PathVariable("id") String id) {

        try {
            Optional<GlobalGraph> optionalGlobalGraph = repository.findById(id);
            if (optionalGlobalGraph.isPresent()) {
                LOGGER.info(LOG_MSG, "getGraphicalGraph", id, "" );
                return new ResponseEntity<>(optionalGlobalGraph.get(), HttpStatus.OK);
            }
            LOGGER.info(LOG_MSG, "getGraphicalGraph", id, "" );
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
/*
    Once save a graphical graph for the first time, we should also save the triples generated in Jena TDB.

    @PutMapping(“{id}”) in the data sent to this API should contain the keys: “GlobalGraph” and “isModified” and “ttl”, “deleted”.
    The former contains the keys from the GlobalGraph POJO,
    Then “isModified” is a boolean to indicate if the graph was modified (adding/deleting nodes) true or not false.
        In case of false, we shouldn’t update any data in mongodb or jena.
        The “isModified” value can be obtained from graph.prepareChangesObject().
    Finally the ttl is a string containing the RDF triples.
    This is generated by Webvowl and can be obtained from exportMenu.exportTurtleText();.
    If it is not modified, this field should be empty and the Java API should not attempt to update the triples in Jena.
    You can have an example how this operation is handle in java in the old code, in line 216 we load the ttl into jena to insert all triples.
    You will have to copy some files from the old code, graphOperations class. DO NOT COPY ALL METHODS JUST THE ESSENTIAL TO DO THIS OPERATION.
    The field deleted should contain keys “classes” and “properties”.
    The former is a list of URIs that were deleted and the latter a list of URIs properties that were deleted.
    There’s no example for this in the actual code, you will have to find all triples where the node URI appear and deleted from Jena, same for properties
    Example of the data:
    {
    "GlobalGraph": {
    "name": "value",
    ....
    },
    "isModified": true
    },
    “ttl”: {
    ...
    },
    “deleted”: {
    “classes”: [“URI1”, “URI2”, ...],
    “properties”: [“URI1”, “URI2”, ...]

    }
*/
    @PutMapping("/{id}")
    public ResponseEntity<GlobalGraph> updateGlobalGraph(@PathVariable("id") String id, @RequestBody GlobalGraphUpdate data) {
        System.out.println("@PutMapping(\"/{id}\")");
        //System.out.println("isModified: " + data.getIsModified());
        //System.out.println("ttl: " + data.getTtl());
        Optional<GlobalGraph> optionalGlobalGraph = repository.findById(id);

        if (optionalGlobalGraph.isPresent()) {
            GlobalGraph _globalGraph = optionalGlobalGraph.get();
            GlobalGraph globalGraph = data.getGlobalGraph();
            if (!globalGraph.getName().equals(""))
                _globalGraph.setName(globalGraph.getName());
            if (!globalGraph.getNamedGraph().equals(""))
                _globalGraph.setNamedGraph(globalGraph.getNamedGraph());
            if (!globalGraph.getNamespace().equals(""))
                _globalGraph.setNamespace(globalGraph.getNamespace());
            // First save
            if (globalGraph.getGraphicalGraph().equals("")) {
                _globalGraph.setGraphicalGraph(globalGraph.getGraphicalGraph()); //Saving for the first time
                //TODO: Save the triples generated in Jena TDB
                //[...]
            }
            // Not the first save:
            else {
                //IT HAS BEEN MODIFIED
                if (data.getIsModified().equals("true")) {
                    // The field deleted should contain keys “classes” and “properties”.
                    System.out.println("Modified equals true");
                    //Overwritten field (Temporal)
                    _globalGraph.setGraphicalGraph(globalGraph.getGraphicalGraph());
                }
                //IT HAS NOT BEEN MODIFIED
                else {
                    // We shouldn’t update any data in mongodb or jena
                    System.out.println("Modified equals false");
                }
            }

            return new ResponseEntity<>(repository.save(_globalGraph), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/graphicalGraph")
    public ResponseEntity<GlobalGraph> updateGraphicalGraph(@PathVariable("id") String id, @RequestBody String graphicalGraph) {
        System.out.println("@PutMapping(\"/{id}/graphicalGraph\")");
        Optional<GlobalGraph> tutorialData = repository.findById(id);

        if (tutorialData.isPresent()) {
            GlobalGraph _globalGraph = tutorialData.get();
            _globalGraph.setGraphicalGraph(graphicalGraph);
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
