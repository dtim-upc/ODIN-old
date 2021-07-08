package edu.upc.essi.dtim.metadatastorage.repository;

import edu.upc.essi.dtim.metadatastorage.models.Wrapper;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WrapperRepository extends MongoRepository<Wrapper, String> {

    Wrapper findByName(String name);
    Iterable<Wrapper> findAllByDataSourcesId(String id);
}