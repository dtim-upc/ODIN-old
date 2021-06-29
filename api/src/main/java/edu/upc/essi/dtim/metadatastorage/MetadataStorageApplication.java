package edu.upc.essi.dtim.metadatastorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetadataStorageApplication {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(MetadataStorageApplication.class);

	public static void main(String[] args) {
		System.out.println("MAIN START!");
		SpringApplication.run(MetadataStorageApplication.class, args);
	}



}
