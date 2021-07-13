package edu.upc.essi.dtim.metadatastorage.utils.jena;

import edu.upc.essi.dtim.metadatastorage.config.db.JenaConnection;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.system.Txn;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;

public class GraphOperations {
    @Autowired
    private JenaConnection jenaConnection;
    private Dataset ds = jenaConnection.getTDBDataset();


    public void loadTTL(String namedGraph, String contentTTL) {
        Txn.executeWrite(ds, ()-> {
            Model graph = ds.getNamedModel(namedGraph);
            graph.read(new ByteArrayInputStream(contentTTL.getBytes()), null, "TTL");
        });

    }
}
