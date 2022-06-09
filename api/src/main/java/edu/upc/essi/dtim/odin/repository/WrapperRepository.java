package edu.upc.essi.dtim.odin.repository;

import edu.upc.essi.dtim.odin.models.mongo.Wrapper;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WrapperRepository extends MongoRepository<Wrapper, String> {

    Wrapper findByName(String name);
    Iterable<Wrapper> findAllByDataSourcesId(String id);
}