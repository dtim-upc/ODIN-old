package edu.upc.essi.dtim.ODINBackend.repository;

import edu.upc.essi.dtim.ODINBackend.models.GlobalGraph;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalGraphRespository extends MongoRepository<GlobalGraph, String> {

    GlobalGraph findByName(String name);

}
