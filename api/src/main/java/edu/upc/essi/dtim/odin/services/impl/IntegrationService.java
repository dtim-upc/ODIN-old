package edu.upc.essi.dtim.odin.services.impl;

import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.models.query.ODINQuery;
import edu.upc.essi.dtim.odin.models.rest.QueryDataSelection;
import edu.upc.essi.dtim.odin.services.filestorage.StorageProperties;
import edu.upc.essi.dtim.odin.utils.jena.GraphOperations;
import edu.upc.essi.dtim.nextiadi.config.DataSourceVocabulary;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class IntegrationService {

    @Autowired
    StorageProperties properties;
    @Autowired
    private GraphOperations graphOperations;

    public Map<String, List<Pair<String,String>>> getSourceAtts(String integratedURI) {

        Map<String, List<Pair<String,String>>> sourceGraphs = new HashMap<>();
        String querySTR = "SELECT ?sourceGraph WHERE { GRAPH <"+integratedURI+"> { <"+integratedURI+"> <"+DataSourceGraph.INTEGRATION_OF.val()+"> ?sourceGraph. }  }";
        ResultSet results  = graphOperations.runAQuery(querySTR);


        while(results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            String sourceURI = solution.getResource("sourceGraph").getURI();
            List<Pair<String, String>> sourceA = graphOperations.getSourceAtts(sourceURI);
            sourceGraphs.put(sourceURI, sourceA);
        }
        return sourceGraphs;
    }


//    public ODINQuery constructs(String integratedURI, String id){
//
//        ODINQuery odin = new ODINQuery();
//        odin.setMinimal(  graphOperations.getGraph( id ) );
//
//        String querySTR = "SELECT ?sourceGraph ?id ?label WHERE { " +
//                " GRAPH ?sourceGraph { ?sourceGraph <"+ DataSourceVocabulary.HAS_ID.val()+"> ?id. " +
//                "?sourceGraph <"+RDFS.label.getURI()+"> ?label." +
//                "} "+
//                "{" +
//                "GRAPH <"+integratedURI+"> { <"+integratedURI+"> <"+DataSourceGraph.INTEGRATION_OF.val()+"> ?sourceGraph. }" +
//                "}}";
//
//        ResultSet results  = graphOperations.runAQuery(querySTR);
//        while(results.hasNext()) {
//            QuerySolution solution = results.nextSolution();
//            String sourceURI = solution.getResource("sourceGraph").getURI();
//            String sourceID = solution.getLiteral("id").getString();
//            String label = solution.getLiteral("label").getString();
//
//            Model source = graphOperations.getGraph(sourceURI);
//            Model integratedResources = graphOperations.getIntegratedResourcesOfSource(sourceID, integratedURI);
//            source.union(integratedResources);
//
//            //metadata
//            String q = "CONSTRUCT {} WHERE {" +
//                    "?s <"+ RDF.type.getURI()+">  <"+DataSourceVocabulary.DataSource.val()+">." +
////                    "?s <"+DataSourceVocabulary.HAS_FORMAT.val()+"> ?format." +
////                    "?s <"+DataSourceVocabulary.HAS_WRAPPER.val()+" ?wrapper> ."+
////                    "?s <"+DataSourceVocabulary.HAS_PATH.val()+" ?path> ."+
//                    "}";
//
//            String q2 = "CONSTRUCT {} WHERE {" +
//                    "?s ?p ?o." +
//                    "?s <"+RDFS.domain.getURI()+"> ?range"
//                    "?s <"+DataSourceVocabulary.HAS_FORMAT+"> ?format." +
//                    "?s <"+DataSourceVocabulary.HAS_WRAPPER+" ?wrapper> ."+
//                    "}";
//
//
//
//        }
//
//        odin.setSourceGraphs();
//        return odin;
//
//    }


    public ODINQuery retrieveConstructs(QueryDataSelection idata){
        String uri = idata.getGraphIRI();
        ODINQuery odin = new ODINQuery();
        Map<String, Model> sourceGraphs = new HashMap<>();
        Map<String, Model> subGraphs = new HashMap<>();

        List<String> ids = new ArrayList<>();
//        Model integratedGraph = graphOperations.getGraph(uri);
            // get all graphs
            String querySTR = "SELECT ?sourceGraph ?id ?label ?minimal WHERE { " +
                    " GRAPH ?sourceGraph { ?sourceGraph <"+ DataSourceVocabulary.HAS_ID.val()+"> ?id. " +
                    "?sourceGraph <"+RDFS.label.getURI()+"> ?label." +
                    "} "+
                    "{" +
                    "GRAPH <"+uri+"> { <"+uri+"> <"+DataSourceGraph.INTEGRATION_OF.val()+"> ?sourceGraph." +
                    " <"+uri+"> <"+DataSourceGraph.MINIMAL.val()+"> ?minimal. }" +
                    "}}";

            ResultSet results  = graphOperations.runAQuery(querySTR);
            String minimalU = "";
            while(results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String sourceURI = solution.getResource("sourceGraph").getURI();
                String minimalURI = solution.getResource("minimal").getURI();
                String sourceID = solution.getLiteral("id").getString();
                String label = solution.getLiteral("label").getString();

                Model source = graphOperations.getGraph(sourceURI);
                // sameAs of integrated
                Model integratedResources = graphOperations.getIntegratedResourcesOfSource(sourceID, uri);
                Model sourceG = graphOperations.simplify(source.union(integratedResources) );

//                try {
//                    RDFDataMgr.write(new FileOutputStream("/Users/javierflores/Documents/upc/projects/newODIN/api/uploads/prueba.ttl"), sourceG, Lang.TURTLE);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                // sameAs of normal properties
                Model propertiesSameAs = graphOperations.getPropertiesSameAs(sourceG);
                sourceG = sourceG.union(propertiesSameAs);

                List<String> identifiersL = getIdentifiers(sourceG);
                ids = Stream.concat(ids.stream(), identifiersL.stream())
                        .collect(Collectors.toList());

                Model subG = graphOperations.getSubGraph(getIAndSourceURIs(sourceID, idata), minimalURI, identifiersL);
                minimalU = minimalURI;

                sourceGraphs.put(sourceURI, sourceG);
                subGraphs.put(sourceURI, subG);
//                String f = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/"+label+"_sourceGraph.ttl";
//                graphOperations.write(f, sourceG , sourceID  );

//                String f2 = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/"+label+"_subGraph.ttl";
//                graphOperations.write(f2, subG , sourceID  );

            }
            odin.setSourceGraphs(sourceGraphs);
            odin.setSubGraphs(subGraphs);

        Model minimal = graphOperations.getGraph(minimalU);
        ids.forEach(i -> minimal.add(new ResourceImpl(i), RDFS.subClassOf, new ResourceImpl(Namespaces.SCHEMA.val()+"identifier")) );
            odin.setMinimal(minimal);

//        String f2 = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/minimalNew.ttl";
//        graphOperations.write(f2, minimal , ""  );


        return odin;

    }



    public List<String> getIdentifiers( Model model){
        List<String> lista  = new ArrayList<>();
        String q = "SELECT ?s WHERE {  ?s <"+RDFS.subClassOf.getURI()+"> <"+ Namespaces.SCHEMA.val()+"identifier>. ?s <"+ OWL.sameAs.getURI()+"> ?o  }";
        ResultSet results  = graphOperations.runAQuery(q, model);
        while(results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            String identifierR = solution.getResource("s").getURI();
            lista.add(identifierR);
        }
        return  lista;
    }

    public List<String> getIAndSourceURIs(String id, QueryDataSelection idata){

        List<String> resources = new ArrayList<>();

        idata.getClasses().forEach( x -> {
            if(x.getIsIntegrated() || x.getIri().contains(id)) {
                resources.add(x.getIri() );
            }
                });
        idata.getProperties().forEach( x -> {
            if(x.getIsIntegrated() || x.getIri().contains(id)) {
                resources.add(x.getIri() );
            }
        });
        return resources;
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
