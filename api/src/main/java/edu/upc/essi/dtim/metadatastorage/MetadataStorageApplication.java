package edu.upc.essi.dtim.metadatastorage;

import edu.upc.essi.dtim.metadatastorage.config.db.JenaConnection;
import edu.upc.essi.dtim.metadatastorage.utils.jena.GraphOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MetadataStorageApplication {




	private static final Logger LOGGER =
			LoggerFactory.getLogger(MetadataStorageApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MetadataStorageApplication.class, args);
	}



}
