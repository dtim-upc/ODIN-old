package edu.upc.essi.dtim.ODINBackend.services.impl;

import edu.upc.essi.dtim.ODINBackend.config.DataSourceTypes;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.SourceGraph;
import edu.upc.essi.dtim.ODINBackend.models.mongo.DataSource;
import edu.upc.essi.dtim.ODINBackend.models.mongo.Wrapper;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.repository.WrapperRepository;
import edu.upc.essi.dtim.ODINBackend.services.filestorage.StorageProperties;
import edu.upc.essi.dtim.ODINBackend.services.filestorage.StorageService;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import edu.upc.essi.dtim.ODINBackend.utils.jena.parsers.OWLToWebVOWL;
import edu.upc.essi.dtim.nextiadi.bootstraping.CSVBootstrap;
import edu.upc.essi.dtim.nextiadi.bootstraping.JSONBootstrap_new;
import edu.upc.essi.dtim.nextiadi.config.Vocabulary;
import org.apache.commons.io.FilenameUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class IntegrationService {

    @Autowired
    StorageProperties properties;
    @Autowired
    private GraphOperations graphOperations;

//
//    public getSubGraphs(String graphURI){
//
//
//
//
//    }



    public Map<String, Model>  retrieveSubGraphs(String uri){


        Map<String, Model> sourceGraphs = new HashMap<>();

//        Model integratedGraph = graphOperations.getGraph(uri);
            // get all graphs
            String querySTR = "SELECT ?sourceGraph ?id ?label WHERE { " +
                    " GRAPH ?sourceGraph { ?sourceGraph <"+DataSourceGraph.HAS_ID.val()+"> ?id. " +
                    "?sourceGraph <"+RDFS.label.getURI()+"> ?label." +
                    "} "+
                    "{" +
                    "GRAPH <"+uri+"> { <"+uri+"> <"+DataSourceGraph.INTEGRATION_OF.val()+"> ?sourceGraph. }" +
                    "}}";

            ResultSet results  = graphOperations.runAQuery(querySTR);
        System.out.println("hola");
            while(results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String sourceURI = solution.getResource("sourceGraph").getURI();
                String sourceID = solution.getLiteral("id").getString();
                String label = solution.getLiteral("label").getString();

                Model source = graphOperations.getGraph(sourceURI);
                Model integratedResources = graphOperations.getIntegratedResourcesOfSource(sourceID, uri);

                sourceGraphs.put(sourceURI, source.union(integratedResources));
                String f = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/"+label+"_subgraph.ttl";
                graphOperations.write(f, source.union(integratedResources) , sourceID  );


            }

        return sourceGraphs;

    }


    public Model retrieveIntegratedGraph(String uri, DataSourceTypes type){

        if (  type.equals(DataSourceTypes.INTEGRATED)  ) {
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
            return  graphOperations.getGraph(uri);
        }

    }





}
