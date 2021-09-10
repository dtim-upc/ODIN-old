package edu.upc.essi.dtim.ODINBackend.controller.manualBootstraping;

import edu.upc.essi.dtim.ODINBackend.models.GlobalGraph;
import edu.upc.essi.dtim.ODINBackend.models.GlobalGraphUpdate;
import edu.upc.essi.dtim.ODINBackend.models.JenaPropertyTriplet;
import edu.upc.essi.dtim.ODINBackend.models.NodeIRI;
import edu.upc.essi.dtim.ODINBackend.repository.GlobalGraphRespository;
import edu.upc.essi.dtim.ODINBackend.services.impl.GlobalGraphService;
import edu.upc.essi.dtim.ODINBackend.services.impl.LAVMappingService;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.upc.essi.dtim.ODINBackend.config.Namespaces.rdf;

@RestController
@RequestMapping("/globalGraph")
public class GlobalGraphController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalGraphController.class);
    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private static final String EMPTY_INPUTS = "{}";
    @Autowired
    private GraphOperations graphOperations;
    @Autowired
    private GlobalGraphService globalGraphService;
    @Autowired
    private LAVMappingService lavMappingService;


    @Autowired
    private GlobalGraphRespository repository;

    @GetMapping
    public ResponseEntity<List<GlobalGraph>> getAllGlobalGraphs() {

        try {
            List<GlobalGraph> globalGraphs = new ArrayList<>();

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

    @GetMapping("{id}/featuresConcepts")
    public ResponseEntity<String[]> getFeaturesConceptsByNamedGraph(@PathVariable("id") String id) {
        Optional<GlobalGraph> optionalGlobalGraph = repository.findById(id);
        if (optionalGlobalGraph.isPresent()) {
            GlobalGraph globalGraph = optionalGlobalGraph.get();
            String[] features = graphOperations.getFeaturesWithConceptFromGraph(globalGraph.getNamedGraph());
            return new ResponseEntity<>(features, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
            String input = globalGraph.toString().replaceAll("[\n\r\t]", "_");
            String returnval = _globalGraph.toString().replaceAll("[\n\r\t]", "_");
            LOGGER.info(LOG_MSG, "createGlobalGraph", input, returnval);
            return new ResponseEntity<>(_globalGraph, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalGraph> updateGlobalGraph(@PathVariable("id") String id, @RequestBody GlobalGraphUpdate data) {
        LOGGER.info( "@PutMapping(/{id})" );
        Optional<GlobalGraph> optionalGlobalGraph = repository.findById(id);

        if (optionalGlobalGraph.isPresent()) {
            //Global Graph stored at Mongodb
            GlobalGraph _globalGraph = optionalGlobalGraph.get();
            //Global Graph Body
            GlobalGraph globalGraph = data.getGlobalGraph();
            boolean first_save = _globalGraph.getGraphicalGraph().equals("");
            if (!globalGraph.getName().equals(""))
                _globalGraph.setName(globalGraph.getName());
            if (!globalGraph.getNamedGraph().equals(""))
                _globalGraph.setNamedGraph(globalGraph.getNamedGraph());
            if (!globalGraph.getNamespace().equals(""))
                _globalGraph.setNamespace(globalGraph.getNamespace());

            //IT HAS BEEN MODIFIED
            if (data.getIsModified().equals("true")) {
                LOGGER.info( "[\"@PutMapping(\"/{id}\")\"] Modified" );
                // The field deleted should contain keys “classes” and “properties”.
                LOGGER.info("Modified equals true");
                //Saves ttl to MongoDB
                _globalGraph.setGraphicalGraph(globalGraph.getGraphicalGraph());
                if (first_save) {
                    //Saves TTL to JENA
                    graphOperations.loadTTL(globalGraph.getNamedGraph(), data.getTtl());
                } else {
                    //----------------- Not first save --------------------
                    //Delete Nodes
                    for (String subject : data.getDeleted().getClasses()) {
                        globalGraphService.deleteNode(globalGraph.getNamedGraph(), subject);
                    }
                    //Delete Properties
                    for (JenaPropertyTriplet jenaPropertyTriplet : data.getDeleted().getProperties()) {
                        globalGraphService.deleteProperty(globalGraph.getNamedGraph(),
                                                            jenaPropertyTriplet.getSubject(),
                                                            jenaPropertyTriplet.getProperty(),
                                                            jenaPropertyTriplet.getObject());
                    }
                    //New Nodes
                    for (NodeIRI nodeIRI : data.getAdded().getClasses()) {
                        graphOperations.addTriple(globalGraph.getNamedGraph(), nodeIRI.getNodeIri(), rdf.val() + "type", nodeIRI.getNodeIriType());
                    }
                    //New Propeties
                    for (JenaPropertyTriplet jenaPropertyTriplet: data.getAdded().getProperties()) {
                        graphOperations.addTriple(globalGraph.getNamedGraph(), jenaPropertyTriplet.getProperty(), rdf.val() + "type", jenaPropertyTriplet.getIriType());
                        graphOperations.addTriple(globalGraph.getNamedGraph(), jenaPropertyTriplet.getSubject(), jenaPropertyTriplet.getProperty(), jenaPropertyTriplet.getObject());
                    }
                }

            }
            //IT HAS NOT BEEN MODIFIED
            else {
                LOGGER.info( "[\"@PutMapping(\"/{id}\")\"] Not modified" );
            }

            return new ResponseEntity<>(repository.save(_globalGraph), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/flagGET")
    public ResponseEntity<HttpStatus> GET_graph() {
        LOGGER.info("flagGET");
        String out = "";

        try{
            ResultSet rs = graphOperations.runAQuery("SELECT * WHERE {GRAPH ?g {?s ?p ?o.}}");
            out = ResultSetFormatter.asText(rs);
            LOGGER.info("RESULT:");
            LOGGER.info(out);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/graphicalGraph")
    public ResponseEntity<GlobalGraph> updateGraphicalGraph(@PathVariable("id") String id, @RequestBody String graphicalGraph) {
        LOGGER.info("@PutMapping(\"/{id}/graphicalGraph\")");
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
            //1. Delete LavMappings
            //2. Delete From Jena
            //3. Delete at Mongodb
            Optional<GlobalGraph> optionalGlobalGraph = repository.findById(id);
            if (optionalGlobalGraph.isPresent()) {
                GlobalGraph globalGraph = optionalGlobalGraph.get();
                lavMappingService.deleteLavMappingByGlobalGraphId(id);
                graphOperations.removeGraph(globalGraph.getNamedGraph());
                repository.deleteById(id);
            }
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
