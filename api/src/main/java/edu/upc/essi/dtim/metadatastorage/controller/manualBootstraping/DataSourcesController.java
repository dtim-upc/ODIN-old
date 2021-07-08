package edu.upc.essi.dtim.metadatastorage.controller.manualBootstraping;

import edu.upc.essi.dtim.metadatastorage.controller.AdminController;
import edu.upc.essi.dtim.metadatastorage.models.DataSources;
import edu.upc.essi.dtim.metadatastorage.models.GlobalGraph;
import edu.upc.essi.dtim.metadatastorage.models.Wrapper;
import edu.upc.essi.dtim.metadatastorage.repository.DataSourcesRepository;
import edu.upc.essi.dtim.metadatastorage.repository.WrapperRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/dataSources")
public class DataSourcesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private static final String EMPTY_INPUTS = "{}";

    @Autowired
    private DataSourcesRepository repository;
    @Autowired
    private WrapperRepository wrapperRepository;

    @PostMapping
    public ResponseEntity<DataSources> createDataSources(@RequestBody DataSources dataSources) {
        try {
            DataSources _dataSources = new DataSources(dataSources.getName(), dataSources.getType());
            repository.save(_dataSources);
            LOGGER.info(LOG_MSG, "createDataSources", dataSources.toString(), _dataSources.toString() );
            return new ResponseEntity<>(_dataSources, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<DataSources>> getAllDataSources() {

        try {
            List<DataSources> dataSources = new ArrayList<DataSources>();

            repository.findAll().forEach(dataSources::add);

            if (dataSources.isEmpty()) {
                ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                LOGGER.info(LOG_MSG, "getAllDataSources", "", response);
                return response;
            }
            LOGGER.info(LOG_MSG, "getAllDataSources", EMPTY_INPUTS, "" );
            return new ResponseEntity<>(dataSources, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/view/{id}")
    public ResponseEntity<DataSources> getAllDataSources(@PathVariable("id") String id) {

        try {
            Optional<DataSources> optionalDataSources = repository.findById(id);
            if (optionalDataSources.isPresent()) {
                LOGGER.info(LOG_MSG, "getDataSources", id, "" );
                return new ResponseEntity<>(optionalDataSources.get(), HttpStatus.OK);
            }
            LOGGER.info(LOG_MSG, "getDataSources", id, "" );
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<HttpStatus> editDataSources(@PathVariable("id") String id, @RequestBody DataSources dataSources) {
        try {
            Optional<DataSources> optionalDataSources = repository.findById(id);
            if (optionalDataSources.isPresent()) {
                DataSources ds = optionalDataSources.get();
                ds.setName(dataSources.getName());
                ds.setType(dataSources.getType());
                repository.save(ds);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDataSources(@PathVariable("id") String id) {
        //First Step: Find wrappers with _dataSourceId == id and delete them
        //Second Step: Delete the data source with _id = id
        try {
            Iterable<Wrapper> wrapperIterable = wrapperRepository.findAllByDataSourcesId(id);
            for (Wrapper w:
                 wrapperIterable)
            wrapperRepository.deleteById(w.getId());
            repository.deleteById(id);
            LOGGER.info(LOG_MSG, "deleteDataSources", id, HttpStatus.NO_CONTENT.toString() );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteDataSources() {
        try {
            repository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
