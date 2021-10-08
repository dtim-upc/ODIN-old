package edu.upc.essi.dtim.ODINBackend.services.impl;

import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OMQService {

    @Autowired
    private GraphOperations graphOperations;
    @Autowired
    private DataSourcesRepository dsRepository;

    public void hello(){
        System.out.println("Hello there!");
    }

}
