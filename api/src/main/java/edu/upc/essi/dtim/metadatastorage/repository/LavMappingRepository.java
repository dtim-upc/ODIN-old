package edu.upc.essi.dtim.metadatastorage.repository;

import edu.upc.essi.dtim.metadatastorage.models.LavMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LavMappingRepository extends MongoRepository<LavMapping, String> {
}