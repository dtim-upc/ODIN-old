package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.nextiadi.config.DataSourceVocabulary;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.models.query.ODINQuery;
import edu.upc.essi.dtim.odin.models.rest.QueryDataSelection;
import edu.upc.essi.dtim.odin.projects.Project;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class QueryService {

    @Autowired
    JenaConnection graph;

//    public ODINQuery retrieveConstructs(QueryDataSelection idata){
//
//
//    }

    public ODINQuery retrieveConstructs(Project p, QueryDataSelection idata){

//        String uri = idata.getGraphID();


        ODINQuery odin = new ODINQuery();
        Map<String, Model> sourceGraphs = new HashMap<>();
        Map<String, Model> subGraphs = new HashMap<>();

        List<String> ids = new ArrayList<>();
        // get all graphs
        String querySTR = "SELECT ?sourceGraph ?id ?label ?minimal WHERE { " +
                " GRAPH ?sourceGraph { ?sourceGraph <"+ DataSourceVocabulary.HAS_ID.val()+"> ?id. " +
                "?sourceGraph <"+ RDFS.label.getURI()+"> ?label." +
                "} "+
                "{" +
                "GRAPH <"+p.getSchemaIntegrationIRI()+"> { <"+p.getSchemaIntegrationIRI()+"> <"+ DataSourceGraph.INTEGRATION_OF.val()+"> ?sourceGraph." +
                " <"+p.getSchemaIntegrationIRI()+"> <"+DataSourceGraph.MINIMAL.val()+"> ?minimal. }" +
                "}}";

        ResultSet results  = graph.persistent().runAQuery(querySTR);
        String minimalU = "";
        while(results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            String sourceURI = solution.getResource("sourceGraph").getURI();
            String minimalURI = solution.getResource("minimal").getURI();
            String sourceID = solution.getLiteral("id").getString();
            String label = solution.getLiteral("label").getString();

            Model source = graph.persistent().getGraph(sourceURI);
            // sameAs of integrated
            Model integratedResources = graph.persistent().getIntegratedResourcesOfSource(sourceID, p.getSchemaIntegrationIRI());
            Model sourceG = graph.persistent().simplify(source.union(integratedResources) );

            // sameAs of normal properties
            Model propertiesSameAs = graph.persistent().getPropertiesSameAs(sourceG);
            sourceG = sourceG.union(propertiesSameAs);

            List<String> identifiersL = getIdentifiers(sourceG);
            ids = Stream.concat(ids.stream(), identifiersL.stream())
                    .collect(Collectors.toList());

            Model subG = graph.persistent().getSubGraph(getIAndSourceURIs(sourceID, idata), minimalURI, identifiersL);
            minimalU = minimalURI;

            sourceGraphs.put(sourceURI, sourceG);
            subGraphs.put(sourceURI, subG);

        }
        odin.setSourceGraphs(sourceGraphs);
        odin.setSubGraphs(subGraphs);

        Model minimal = graph.persistent().getGraph(minimalU);
        ids.forEach(i -> minimal.add(new ResourceImpl(i), RDFS.subClassOf, new ResourceImpl(Namespaces.SCHEMA.val()+"identifier")) );
        odin.setMinimal(minimal);

        return odin;

    }

    public List<String> getIdentifiers( Model model){
        List<String> lista  = new ArrayList<>();
        String q = "SELECT ?s WHERE {  ?s <"+RDFS.subClassOf.getURI()+"> <"+ Namespaces.SCHEMA.val()+"identifier>. ?s <"+ OWL.sameAs.getURI()+"> ?o  }";
        ResultSet results  = graph.persistent().runAQuery(q, model);
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


}
