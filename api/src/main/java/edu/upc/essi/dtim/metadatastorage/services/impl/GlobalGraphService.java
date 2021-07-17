package edu.upc.essi.dtim.metadatastorage.services.impl;

import edu.upc.essi.dtim.metadatastorage.utils.jena.GraphOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalGraphService {
    @Autowired
    private GraphOperations graphOperations;

    /*
    * En la clase GlobalGraphService, crea el método deleteNode()
    * que deberá llamar deleteTriplesWithSubject()
    * y deleteTriplesWithObject() en GraphOperations.
    * */
    public void deleteNode(String graphIRI, String subjectIRI) {
        graphOperations.deleteTriplesWithObject(graphIRI, subjectIRI);
        graphOperations.deleteTriplesWithSubject(graphIRI, subjectIRI);
    }
    /*
    * En la clase GlobalGraphService, crea el método deleteProperty()
    * deberá llamar al método deleteTriples() en GraphOperations.
    * He olvidado que necesitamos las URIs de los nodos que conecta la property,
    * tendrás que actualizar la key "delete"."property" para incluir las URIs que
    * conectaba la property. Me parece que esto lo puedes hacer en webvowl
    * llamando desde el objeto property .domain() y .range(). Revisalo y si no me comentas.
    * */
    public void deleteProperty(String graphIRI,String subjectIRI, String predicateIRI, String objectIRI) {
        graphOperations.deleteTriples(graphIRI, subjectIRI, predicateIRI, objectIRI);
    }
}
