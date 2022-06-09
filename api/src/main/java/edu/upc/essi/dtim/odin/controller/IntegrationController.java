package edu.upc.essi.dtim.odin.controller;

import edu.upc.essi.dtim.NextiaDI;
import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.models.mongo.DataSource;
import edu.upc.essi.dtim.odin.models.rest.IntegrationData;
import edu.upc.essi.dtim.odin.repository.DataSourcesRepository;
import edu.upc.essi.dtim.odin.utils.jena.GraphOperations;
import edu.upc.essi.dtim.odin.utils.jena.parsers.OWLToWebVOWL;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
        System.out.println("INTEGRATING..");
        NextiaDI n = new NextiaDI();

        String dsA = iData.getDsA().getIri();
        String dsB = iData.getDsB().getIri();

        Model graphA = retrieveGraph(dsA, iData.getDsA(), iData.getAlignments());
        Model graphB = retrieveGraph(dsB, iData.getDsB(), iData.getAlignments());



        Model integratedModel = n.Integrate(graphA, graphB, iData.getAlignments());


//        String f = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/integrated_new.ttl";
//        graphOperations.write(f, integratedModel);
        /*Graph g = new Graph();
        g.setModel(integratedModel);*/
//        g.write("/Users/javierflores/Documents/UPC_projects/new/newODIN/api/uploads/int/"+iData.getIntegratedName()+".ttl", Lang.TURTLE);


        DataSource integratedDatasource = new DataSource(iData.getIntegratedName(),DataSourceTypes.INTEGRATED );
        integratedDatasource.setPath("");
        integratedDatasource.setUnusedA( n.getUnused() );

        LOGGER.info("unused are: {}",n.getUnused());

        OWLToWebVOWL vowl = new OWLToWebVOWL(integratedDatasource.getIri(),integratedDatasource.getName() );
        String vowlJson = vowl.convertSchema(integratedModel);

        integratedDatasource.setGraphicalIntegration(vowlJson);


        OWLToWebVOWL vowl2 = new OWLToWebVOWL(integratedDatasource.getIri(),integratedDatasource.getName() );
        Model minimal = n.getMinimalGraph();
//        String f2 = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/minimal.ttl";
//        graphOperations.write(f2, minimal);
        String vowlJson2 = vowl2.convertSchema(minimal);


        Model simplifyI = n.getOnlyIntegrationResources();
//        String f3 = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/integrated_simple.ttl";
//        graphOperations.write(f3, simplifyI);

        integratedDatasource.setGraphicalMinimalIntegration(vowlJson2);
        integratedDatasource.setGraphicalGraph(vowlJson2);


        graphOperations.addModel(integratedDatasource.getIri(), simplifyI);
        graphOperations.addModel(integratedDatasource.getMinimalIRI(), minimal);

        graphOperations.addTriple(integratedDatasource.getIri(), integratedDatasource.getIri(), RDF.type.getURI(), Namespaces.Integration.val());
        graphOperations.addTripleLiteral(integratedDatasource.getIri(), integratedDatasource.getIri(), RDFS.label.getURI(), integratedDatasource.getName());
        graphOperations.addTripleLiteral(integratedDatasource.getIri(), integratedDatasource.getIri(), DataSourceGraph.HAS_ID.val(), integratedDatasource.getId());

        graphOperations.addTriple(integratedDatasource.getIri(), integratedDatasource.getIri(), DataSourceGraph.INTEGRATION_OF.val(), dsA  );
        graphOperations.addTriple(integratedDatasource.getIri(), integratedDatasource.getIri(), DataSourceGraph.INTEGRATION_OF.val(), dsB  );

        graphOperations.addTriple(integratedDatasource.getIri(), integratedDatasource.getIri(), DataSourceGraph.MINIMAL.val(), integratedDatasource.getMinimalIRI()  );

        graphOperations.addTriple(integratedDatasource.getMinimalIRI(), integratedDatasource.getMinimalIRI(), DataSourceGraph.IS_MINIMAL_OF.val(), integratedDatasource.getIri()  );

//        String f = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/integrated.ttl";
//        graphOperations.write(f, graphOperations.getGraph(integratedDatasource.getIri()));

//        String f4 = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/integrated_full.ttl";
//        graphOperations.write(f4, integratedModel );

        repository.insert(integratedDatasource);

        return new ResponseEntity<>(integratedDatasource, HttpStatus.OK);
    }


    public Model retrieveGraph(String uri, DataSource data, List<Alignment> alignments){

        if (  data.getType().equals(DataSourceTypes.INTEGRATED)  ) {
            // generate view of all sources

            Model integratedGraph = graphOperations.getGraph(uri);

            // get all graphs
            String querySTR = "SELECT ?graph WHERE {  <"+uri+"> <"+DataSourceGraph.INTEGRATION_OF.val()+"> ?graph. }";

            ResultSet results  = graphOperations.runAQuery(querySTR);


            while(results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String gURI = solution.getResource("graph").getURI();
                integratedGraph = integratedGraph.union( graphOperations.getGraph(gURI) );

            }
            return integratedGraph;
        } else {

            // Todo think in a better way to do this. Maybe identifiers should be declared when loading data
//            List<Alignment> aligId= alignments.stream().filter(x -> x.getIdentifier()  ).collect(Collectors.toList());;
            List<Alignment> aligId= alignments.stream().filter(x -> x.getType().contains("datatype")  ).collect(Collectors.toList());;

            Model sourceG = graphOperations.getGraph(uri);

            for ( Alignment a : aligId) {

                Resource rA = sourceG.createResource(a.getIriA());
                Resource rB = sourceG.createResource(a.getIriB());

                if (sourceG.containsResource(rA) ) {
                    graphOperations.addTriple(uri, rA.getURI(), RDFS.subClassOf.getURI(),Namespaces.SCHEMA.val()+"identifier");
                } else {
                    graphOperations.addTriple(uri, rB.getURI(), RDFS.subClassOf.getURI(),Namespaces.SCHEMA.val()+"identifier");
                }
            }

            sourceG = graphOperations.getGraph(uri);
//            String f = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/"+data.getName()+"_sourceGraph_identifier.ttl";
//            graphOperations.write(f, sourceG, data.getId() );

            return  sourceG;
        }

    }


}
