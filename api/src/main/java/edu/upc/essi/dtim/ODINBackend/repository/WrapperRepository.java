package edu.upc.essi.dtim.ODINBackend.repository;

import edu.upc.essi.dtim.ODINBackend.models.Wrapper;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WrapperRepository extends MongoRepository<Wrapper, String> {

    Wrapper findByName(String name);
    Iterable<Wrapper> findAllByDataSourcesId(String id);
}