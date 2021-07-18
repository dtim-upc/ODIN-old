package edu.upc.essi.dtim.metadatastorage.services.impl;

import edu.upc.essi.dtim.metadatastorage.utils.jena.GraphOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalGraphService {
    @Autowired
    private GraphOperations graphOperations;
    
    public void deleteNode(String graphIRI, String subjectIRI) {
        graphOperations.deleteTriplesWithObject(graphIRI, subjectIRI);
        graphOperations.deleteTriplesWithSubject(graphIRI, subjectIRI);
    }

    public void deleteProperty(String graphIRI,String subjectIRI, String predicateIRI, String objectIRI) {
        graphOperations.deleteTriples(graphIRI, subjectIRI, predicateIRI, objectIRI);
    }
}
