package edu.upc.essi.dtim.metadatastorage.utils.jena;

import edu.upc.essi.dtim.metadatastorage.config.db.JenaConnection;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.system.Txn;
import org.apache.jena.update.UpdateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;

@Component
public class GraphOperations {


    @Autowired
    private JenaConnection jenaConnection;
    private Dataset ds;

    @PostConstruct
    public void init(){
        ds = jenaConnection.getTDBDataset();
    }

    public void loadTTL(String namedGraph, String contentTTL) {
        Txn.executeWrite(ds, ()-> {
            Model graph = ds.getNamedModel(namedGraph);
            graph.read(new ByteArrayInputStream(contentTTL.getBytes()), null, "TTL");
        });

    }
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

    public void deleteTriplesWithSubject(String graphIRI, String subjectIRI){
        runAnUpdateQuery("DELETE WHERE { GRAPH <" + graphIRI + ">" +
                " {<"+subjectIRI+"> ?p ?o} }");
    }

    public void deleteTriplesWithObject(String graphIRI, String objectIRI){
        runAnUpdateQuery("DELETE WHERE { GRAPH <" + graphIRI + ">" +
                " {?s ?p <"+objectIRI+"> } }");
    }

    public  void runAnUpdateQuery(String sparqlQuery) {

        Txn.executeWrite(ds, ()->{

            try {
                UpdateAction.parseExecute(sparqlQuery, ds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
