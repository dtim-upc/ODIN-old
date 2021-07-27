package edu.upc.essi.dtim.ODINBackend.repository;

import edu.upc.essi.dtim.ODINBackend.models.LavMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LavMappingRepository extends MongoRepository<LavMapping, String> {
}