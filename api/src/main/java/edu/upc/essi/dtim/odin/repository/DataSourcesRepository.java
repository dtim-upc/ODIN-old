package edu.upc.essi.dtim.odin.repository;

import edu.upc.essi.dtim.odin.models.mongo.DataSource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataSourcesRepository extends MongoRepository<DataSource, String> {

    DataSource findByName(String name);

}
