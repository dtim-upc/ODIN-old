package edu.upc.essi.dtim.metadatastorage.repository;

import edu.upc.essi.dtim.metadatastorage.models.GlobalGraph;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalGraphRespository extends MongoRepository<GlobalGraph, String> {

    GlobalGraph findByName(String name);

}
