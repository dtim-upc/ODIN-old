package edu.upc.essi.dtim.metadatastorage.config;

import edu.upc.essi.dtim.metadatastorage.config.db.JenaConnection;
import edu.upc.essi.dtim.metadatastorage.utils.jena.GraphOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class AppConfig {

//    @Bean
//    @DependsOn("Jena")
//    public GraphOperations getGraphOperations() {
//        return new GraphOperations();
//    }

//    @Bean
//    public JenaConnection getJenaConnection() {
//        JenaConnection conn = JenaConnection.getInstance();
//        conn.init();
//        return conn;
//    }



}