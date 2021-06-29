package edu.upc.essi.dtim.metadatastorage.repository;

import edu.upc.essi.dtim.metadatastorage.models.DataSources;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataSourcesRepository extends MongoRepository<DataSources, String> {

    DataSources findByName(String name);

}
