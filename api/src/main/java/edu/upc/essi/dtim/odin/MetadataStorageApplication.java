package edu.upc.essi.dtim.odin;

import edu.upc.essi.dtim.odin.storage.filestorage.StorageProperties;
import edu.upc.essi.dtim.odin.storage.filestorage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MetadataStorageApplication {

//	Now this is done in JenaConnection class
//	@Bean
//	CommandLineRunner init(StorageService storageService) {
//		return args -> storageService.init();
//	}

	public static void main(String[] args) {
		SpringApplication.run(MetadataStorageApplication.class, args);
	}

}
