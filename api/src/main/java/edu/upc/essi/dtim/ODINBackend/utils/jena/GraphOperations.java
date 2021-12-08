package edu.upc.essi.dtim.ODINBackend.utils.jena;

import edu.upc.essi.dtim.ODINBackend.config.db.JenaConnection;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.ODINBackend.utils.jena.query.SelectQuery;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.system.Txn;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

@Component
public class GraphOperations {


    @Autowired
    private JenaConnection jenaConnection;
    private Dataset ds;
    private SelectQuery selectQuery = new SelectQuery();

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphOperations.class);

    @PostConstruct
    public void init(){
        ds = jenaConnection.getTDBDataset();
    }

    public Dataset getDataset(){return ds;};

    public Model getGraph(String iri){

        return Txn.calculateRead(ds, ()-> {
            if(ds.containsNamedModel(iri)){
                Model m = ds.getNamedModel(iri);
//                return ModelFactory.createModelForGraph(m.getGraph());
                return ModelFactory.createDefaultModel().add(m);
            }
            return null;

        });


    }


    public void addModel(String namedGraph, Model model){
        Txn.executeWrite(ds, ()-> {
            Model graph = ds.getNamedModel(namedGraph);
            graph.add(model);
        });
    }


    public String[] getFeaturesWithConceptFromGraph(String namedGraph){
        List<String> features = new ArrayList<>();

        Query SPARQL = selectQuery.selectSubjectAndFeatureFromGraph(namedGraph,"http://www.essi.upc.edu/~snadal/BDIOntology/Global/hasFeature");

        runAQuery(SPARQL).forEachRemaining(t -> {
            features.add(t.get("o").asNode().getURI());
        });
        String[] featuresArray = features.toArray(new String[0]);
        for (String s : featuresArray) {
            LOGGER.info(s);
        }
        return featuresArray;
    }

    public void loadTTL(String namedGraph, String contentTTL) {
        Txn.executeWrite(ds, ()-> {
            Model graph = ds.getNamedModel(namedGraph);
            graph.read(new ByteArrayInputStream(contentTTL.getBytes()), null, "TTL");
        });
    }

    public void removeGraph(String iri){
        Txn.executeWrite(ds, ()-> {
            ds.removeNamedModel(iri);
        });
    }

    public void addTriple(String namedGraph, String s, String p, String o) {
        Txn.executeWrite(ds, ()-> {
            Model graph = ds.getNamedModel(namedGraph);
            graph.add(new ResourceImpl(s), new PropertyImpl(p), new ResourceImpl(o));
        });
    }


    public void addTripleLiteral(String namedGraph, String s, String p, String o) {
        Txn.executeWrite(ds, ()-> {
            Model graph = ds.getNamedModel(namedGraph);
            graph.add(new ResourceImpl(s), new PropertyImpl(p), o);
        });
    }

    public void deleteTriplesWithSubject(String graphIRI, String subjectIRI){
        runAnUpdateQuery("DELETE WHERE { GRAPH <" + graphIRI + ">" +
                " {<"+subjectIRI+"> ?p ?o} }");
    }

    public void deleteTriplesWithObject(String graphIRI, String objectIRI){
        runAnUpdateQuery("DELETE WHERE { GRAPH <" + graphIRI + ">" +
                " {?s ?p <"+objectIRI+"> } }");
    }




    public void write(String file, Model model) {

        Map<String, String> prefixes = new HashMap<>();
        prefixes.put("sourceSchema", DataSourceGraph.SCHEMA.val() + "/");
        prefixes.put("datasource", Namespaces.DataSource.val()+"/");
        prefixes.put("nextiaDI", Namespaces.NextiaDI.val() );
        prefixes.put("integration", Namespaces.Integration.val()+"/" );

        Model copyM = model;
        model.setNsPrefixes(prefixes);
//
        try {
            RDFDataMgr.write(new FileOutputStream(file), copyM, Lang.TURTLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write(String file, Model model, String id) {

        Map<String, String> prefixes = new HashMap<>();
        prefixes.put("sourceSchema", DataSourceGraph.SCHEMA.val() + "/"+id+"/");
        prefixes.put("datasource", Namespaces.DataSource.val()+"/");
        prefixes.put("nextiaDI", Namespaces.NextiaDI.val() );
        prefixes.put("integration", Namespaces.Integration.val()+"/" );

        Model copyM = model;
        model.setNsPrefixes(prefixes);
//
        try {
            RDFDataMgr.write(new FileOutputStream(file), copyM, Lang.TURTLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteTriples(String graphIRI,String subjectIRI, String predicateIRI, String objectIRI) {
        Txn.executeWrite(ds, ()->{
            Model graph = ds.getNamedModel(graphIRI);
            graph.remove(new ResourceImpl(subjectIRI), new PropertyImpl(predicateIRI), new ResourceImpl(objectIRI));
        });
    }
    public void deleteAllTriples(String namedGraph){
        Txn.executeWrite(ds, ()-> {
            Model graph = ds.getNamedModel(namedGraph);
            graph.removeAll();
        });
    }

    public void deleteAllGraphs(){

        try {

            Txn.executeWrite(ds, ()-> {
                Iterator<String> graphNames = ds.listNames();
                List<String> graphs = new ArrayList<>();
                while (graphNames.hasNext()) {
                    graphs.add(graphNames.next());

                }
                graphs.forEach(iri -> ds.removeNamedModel(iri));
            }) ;



//        removeGraph(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * QUERIES
     * */

    public ResultSet runAQuery(Query query) {

        ResultSet resultSet = Txn.calculateRead(ds, ()-> {
            try(QueryExecution qExec = QueryExecutionFactory.create(query, ds)) {
//                qExec.getContext().set(Symbols.unionDefaultGraph, true);

                return ResultSetFactory.copyResults(qExec.execSelect()) ;
            } catch ( Exception e) {
                e.printStackTrace();
                return null;
            }
        }) ;
        return resultSet;
    }

    public ResultSet runAQuery(String query) {

        return runAQuery(QueryFactory.create(query));
    }

    public  void runAnUpdateQuery(String sparqlQuery) {

        Txn.executeWrite(ds, ()->{

            try {
                UpdateAction.parseExecute(sparqlQuery, ds);
            } catch (Exception e) {
                LOGGER.error("Error occurred while Updating a quiery");
            }
        });

    }


    public Model getIntegratedResourcesOfSource(String id, String uri){

        String q = "PREFIX rdfs: <"+ RDFS.getURI()+">" +
                "CONSTRUCT {" +
                " ?sourceR ?dr ?integrated. " +
                " ?integrated ?p ?o." +
                "} WHERE { " +
                "VALUES ?dr { rdfs:subPropertyOf rdfs:subClassOf  }" +
                " ?sourceR ?dr ?integrated. " +
                " ?integrated ?p ?o." +
                "FILTER ( CONTAINS( STR(?sourceR), '"+id+"' )  )" +
                "} ";

        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.create(query, getGraph(uri) );
        Model results = qexec.execConstruct();
        return results;


    }


}
