package edu.upc.essi.dtim.odin.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class AppConfig {

    @Value("${spark.name}")
    private String sparkAppName;

    @Value("${spark.bindAddress}")
    private String sparkBindAddress;

    @Value("${spark.master}")
    private String masterURI;

    @Value("${spark.driver.memory}")
    private String sparkDriverMemory;

    @Value("${spark.testing.memory}")
    private String sparkTestingMemory;

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName(sparkAppName)
                .setMaster(masterURI)
                .set("spark.driver.bindAddress", sparkBindAddress)
                .set("spark.driver.memory", sparkDriverMemory  )
                .set("spark.testing.memory", sparkTestingMemory  );

    }

    @Bean
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession
                .builder()
                .sparkContext(javaSparkContext().sc())
                .appName(sparkAppName)
                .getOrCreate();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


}