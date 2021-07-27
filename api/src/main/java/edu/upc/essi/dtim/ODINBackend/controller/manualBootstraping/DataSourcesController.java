package edu.upc.essi.dtim.ODINBackend.controller.manualBootstraping;

import edu.upc.essi.dtim.ODINBackend.config.Namespaces;
import edu.upc.essi.dtim.ODINBackend.config.SourceGraph;
import edu.upc.essi.dtim.ODINBackend.controller.AdminController;
import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.repository.WrapperRepository;
import edu.upc.essi.dtim.ODINBackend.services.filestorage.StorageService;
import edu.upc.essi.dtim.ODINBackend.services.impl.DataSourceService;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/dataSource")
public class DataSourcesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private static final String EMPTY_INPUTS = "{}";
    private String latest_generated_random_string = "";
    @Value("${db.files.upload.dir}")
    private String upload_dir;

    @Autowired
    private DataSourcesRepository repository;
    @Autowired
    private WrapperRepository wrapperRepository;
    @Autowired
    private StorageService storageService;
    @Autowired
    private GraphOperations graphOperations;
    @Autowired
    private DataSourceService dataSourceService;

    @PostMapping
    public ResponseEntity<DataSource> createDataSources(@RequestBody DataSource dataSource) {
        try {
            DataSource _dataSource = new DataSource(dataSource.getName(), dataSource.getType());
            this.latest_generated_random_string = RandomStringUtils.randomAlphanumeric(16);
            System.out.println(upload_dir);
            _dataSource.setJson_path(upload_dir + '/' + this.latest_generated_random_string + ".json");

            repository.save(_dataSource);
            storageService.setRandomSeed(latest_generated_random_string);
            graphOperations.addTriple(_dataSource.getIri(),
                    _dataSource.getIri(),
                    Namespaces.rdf.val() + "type",
                    SourceGraph.DATA_SOURCE.val());
            String input = dataSource.toString().replaceAll("[\n\r\t]", "_");
            String returnval = _dataSource.toString().replaceAll("[\n\r\t]", "_");
            LOGGER.info(LOG_MSG, "createDataSources", input, returnval);
            return new ResponseEntity<>(_dataSource, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<HttpStatus> uploadMultipartFile(@RequestParam MultipartFile file) throws IOException {
        storageService.store(file);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DataSource>> getAllDataSources() {

        try {
            List<DataSource> dataSources = new ArrayList<DataSource>();

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
    public ResponseEntity<DataSource> getAllDataSources(@PathVariable("id") String id) {

        try {
            Optional<DataSource> optionalDataSources = repository.findById(id);
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

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> editDataSources(@PathVariable("id") String id, @RequestBody DataSource dataSource) {
        try {
            Optional<DataSource> optionalDataSources = repository.findById(id);
            if (optionalDataSources.isPresent()) {
                DataSource ds = optionalDataSources.get();
                ds.setName(dataSource.getName());
                ds.setType(dataSource.getType());
                repository.save(ds);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDataSources(@PathVariable("id") String id) {
        dataSourceService.delete(id);
        LOGGER.info(LOG_MSG, "deleteDataSources", id, HttpStatus.NO_CONTENT.toString() );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
