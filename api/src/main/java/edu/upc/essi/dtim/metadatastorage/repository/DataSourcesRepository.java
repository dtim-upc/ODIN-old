package edu.upc.essi.dtim.metadatastorage.repository;

import edu.upc.essi.dtim.metadatastorage.models.DataSource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataSourcesRepository extends MongoRepository<DataSource, String> {

    DataSource findByName(String name);

}
