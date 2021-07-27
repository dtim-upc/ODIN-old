package edu.upc.essi.dtim.ODINBackend.repository;

import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataSourcesRepository extends MongoRepository<DataSource, String> {

    DataSource findByName(String name);

}
