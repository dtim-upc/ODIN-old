package edu.upc.essi.dtim.ODINBackend.utils.jena;

import edu.upc.essi.dtim.ODINBackend.config.db.JenaConnection;
import edu.upc.essi.dtim.ODINBackend.utils.jena.query.SelectQuery;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.system.Txn;
import org.apache.jena.update.UpdateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class GraphOperations {


    @Autowired
    private JenaConnection jenaConnection;
    private Dataset ds;
    private SelectQuery selectQuery = new SelectQuery();

    @PostConstruct
    public void init(){
        ds = jenaConnection.getTDBDataset();
    }


    public String[] getFeaturesWithConceptFromGraph(String namedGraph){
        List<String> features = new ArrayList<String>();

        Query SPARQL = selectQuery.selectSubjectAndFeatureFromGraph(namedGraph,"http://www.essi.upc.edu/~snadal/BDIOntology/Global/hasFeature");

        runAQuery(SPARQL).forEachRemaining(t -> {
            features.add(t.get("o").asNode().getURI());
        });
        String[] featuresArray = features.toArray(new String[0]);
        System.out.println("THIS");
        for (String s : featuresArray) {
            System.out.println(s);
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

    public void deleteTriplesWithSubject(String graphIRI, String subjectIRI){
        runAnUpdateQuery("DELETE WHERE { GRAPH <" + graphIRI + ">" +
                " {<"+subjectIRI+"> ?p ?o} }");
    }

    public void deleteTriplesWithObject(String graphIRI, String objectIRI){
        runAnUpdateQuery("DELETE WHERE { GRAPH <" + graphIRI + ">" +
                " {?s ?p <"+objectIRI+"> } }");
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

    /*
    * QUERIES
    * */

    public ResultSet runAQuery(Query query) {

        ResultSet resultSet = Txn.calculateRead(ds, ()-> {
            try(QueryExecution qExec = QueryExecutionFactory.create(query, ds)) {
                return ResultSetFactory.copyResults(qExec.execSelect()) ;
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
                System.out.println("Error occurred while Updating a quiery");
            }
        });

    }

}
