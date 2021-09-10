package edu.upc.essi.dtim.ODINBackend.controller;

import edu.upc.essi.dtim.Nuupdi;
import edu.upc.essi.dtim.ODINBackend.config.DataSourceTypes;
import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import edu.upc.essi.dtim.ODINBackend.models.rest.IntegrationData;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import edu.upc.essi.dtim.ODINBackend.utils.jena.parsers.OWLToWebVOWL;
import edu.upc.essi.dtim.nuupdi.config.Namespaces;
import edu.upc.essi.dtim.nuupdi.jena.Graph;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
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

        String integratedIRI = Namespaces.G.val() + iData.getIntegratedName();

        Model integratedModel = n.Integrate(graphA, graphB, iData.getAlignments());

        Graph g = new Graph();
        g.setModel(integratedModel);
        g.write("/Users/javierflores/Documents/UPC_projects/new/newODIN/api/uploads/int/"+iData.getIntegratedName()+".ttl", Lang.TURTLE);


        DataSource integratedDatasource = new DataSource();
        integratedDatasource.setType(DataSourceTypes.INTEGRATED);
        integratedDatasource.setPath("");
        integratedDatasource.setName(iData.getIntegratedName());
        integratedDatasource.setIri(integratedIRI);
        integratedDatasource.setUnusedA( n.getUnused() );

        LOGGER.info("unused are: {}",n.getUnused());

        OWLToWebVOWL vowl = new OWLToWebVOWL(integratedDatasource.getIri(),integratedDatasource.getName() );
        String vowlJson = vowl.convertSchema(integratedModel);

        integratedDatasource.setGraphicalIntegration(vowlJson);


        OWLToWebVOWL vowl2 = new OWLToWebVOWL(integratedDatasource.getIri(),integratedDatasource.getName() );
        String vowlJson2 = vowl2.convertSchema(n.getMinimalGraph());

        integratedDatasource.setGraphicalMinimalIntegration(vowlJson2);





        graphOperations.addModel(integratedIRI, integratedModel);
        repository.insert(integratedDatasource);

        return new ResponseEntity<>(integratedDatasource, HttpStatus.OK);
    }


}
