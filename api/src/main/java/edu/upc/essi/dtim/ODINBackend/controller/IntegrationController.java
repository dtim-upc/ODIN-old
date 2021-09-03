package edu.upc.essi.dtim.ODINBackend.controller;

import edu.upc.essi.dtim.Nuupdi;
import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import edu.upc.essi.dtim.ODINBackend.models.rest.IntegrationData;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import edu.upc.essi.dtim.ODINBackend.utils.jena.parsers.OWLToWebVOWL;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integration")
public class IntegrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationController.class);

    @Autowired
    private GraphOperations graphOperations;

    @Autowired
    private DataSourcesRepository repository;

    @PostMapping
    public ResponseEntity<DataSource> integrate(@RequestBody IntegrationData iData) {

        Nuupdi n = new Nuupdi();

        Model graphA = graphOperations.getGraph(iData.getDsA().getIri());
        Model graphB = graphOperations.getGraph(iData.getDsB().getIri());

        String integratedIRI = "http://exaple.com";

        Model integratedModel = n.Integrate(graphA, graphB, iData.getAlignments());

        DataSource integratedDatasource = new DataSource();
        integratedDatasource.setType("integrated");
        integratedDatasource.setPath("");
        integratedDatasource.setName("namei1sa");
        integratedDatasource.setIri(integratedIRI);

        OWLToWebVOWL vowl = new OWLToWebVOWL();
        vowl.setNamespace(integratedDatasource.getIri());
        vowl.setTitle(integratedDatasource.getName());
        vowl.setPrefix("");
        String vowlJson = vowl.convertSchema(n.getMinimalGraph());
        integratedDatasource.setGraphicalGraph(vowlJson);


        graphOperations.addModel(integratedIRI, integratedModel);
        repository.insert(integratedDatasource);

        return new ResponseEntity<>(integratedDatasource, HttpStatus.OK);
    }


}
