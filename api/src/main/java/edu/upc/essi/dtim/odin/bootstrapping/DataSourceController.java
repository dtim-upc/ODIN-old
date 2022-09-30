//package edu.upc.essi.dtim.odin.bootstrapping;
//
//import edu.upc.essi.dtim.odin.repository.DataSourcesRepository;
//import edu.upc.essi.dtim.odin.storage.JenaConnection;
//import edu.upc.essi.dtim.odin.storage.filestorage.StorageService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/dataSource")
//public class DataSourceController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceController.class);
//    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
//    private static final String EMPTY_INPUTS = "{}";
//
//    @Autowired
//    private DataSourcesRepository repository;
//    @Autowired
//    private JenaConnection graph;
//
//    @Autowired
//    private StorageService storageService;
//
//    @Autowired
//    private DataSourceService dataSourceService;
//
//    @PostMapping("persist")
//    public ResponseEntity<DataSource> persistDataSource(@RequestBody DataSource dataSource) {
//        try {
//            System.out.println(dataSource.toString());
//
//            System.out.println("persist data source");
//            dataSourceService.persist(dataSource);
//            return new ResponseEntity<>(dataSource, HttpStatus.CREATED);
//        } catch (Exception e) {
//            LOGGER.error("something went wrong persistent", e);
//            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping( consumes = {"multipart/form-data"})
//    public ResponseEntity<DataSource> createDataSource(@RequestPart DataSource dataSource, @RequestPart MultipartFile file, @RequestPart Boolean bootstrappingType) {
//        try {
//            DataSource _dataSource = dataSourceService.create(dataSource, bootstrappingType, file);
//
//            String input = dataSource.toString().replaceAll("[\n\r\t]", "_");
//            String returnval = _dataSource.toString().replaceAll("[\n\r\t]", "_");
//
//            LOGGER.info(LOG_MSG, "createDataSource", input, returnval);
//            return new ResponseEntity<>(_dataSource, HttpStatus.CREATED);
//        } catch (Exception e) {
//            LOGGER.error(e.toString(), e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//    @GetMapping
//    public ResponseEntity<List<DataSource>> getAllDataSources() {
//        System.out.println("getAllDataSources....");
//        try {
//            List<DataSource> dataSources = new ArrayList<>();
//
//            repository.findAll().forEach(dataSources::add);
////            dataSources = graph.persistent().findAllDataSources();
//            if (dataSources.isEmpty()) {
//                ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
//                LOGGER.info(LOG_MSG, "getAllDataSources", "", response);
//                return response;
//            }
//            LOGGER.info(LOG_MSG, "getAllDataSources", EMPTY_INPUTS, "" );
//            return new ResponseEntity<>(dataSources, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @GetMapping("/{id}")
//    public ResponseEntity<DataSource> getDataSourceByID(@PathVariable("id") String id) {
//
//        try {
//            Optional<DataSource> optionalDataSources = repository.findById(id);
//            if (optionalDataSources.isPresent()) {
//                LOGGER.info(LOG_MSG, "getDataSources", id, "" );
//                return new ResponseEntity<>(optionalDataSources.get(), HttpStatus.OK);
//            }
////            kkk
//            DataSource ds = graph.persistent().findDSById(id);
//            if( !ds.getName().equals("")) {
//                LOGGER.info(LOG_MSG, "getDataSources", id, "" );
//                return new ResponseEntity<>(ds, HttpStatus.OK);
//            }
//            LOGGER.info(LOG_MSG, "getDataSources", id, "" );
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            LOGGER.error("something went wrong", e);
//            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//    @GetMapping("/temporal/{id}")
//    public ResponseEntity<DataSource> getTemporalDataSourceByID(@PathVariable("id") String id) {
//
//        try {
//            DataSource ds = graph.temporal().findDSById(id);
//
//            if( ds.getName() != null) {
//                LOGGER.info(LOG_MSG, "getDataSources", id, "" );
//                return new ResponseEntity<>(ds, HttpStatus.OK);
//            }
//            LOGGER.info(LOG_MSG, "getDataSources", id, "" );
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            LOGGER.error("something went wrong", e);
//            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<HttpStatus> editDataSources(@PathVariable("id") String id, @RequestBody DataSource dataSource) {
//        try {
//            Optional<DataSource> optionalDataSources = repository.findById(id);
//            if (optionalDataSources.isPresent()) {
//                DataSource ds = optionalDataSources.get();
//                ds.setName(dataSource.getName());
//                ds.setType(dataSource.getType());
//                repository.save(ds);
//            }
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<HttpStatus> deleteDataSources(@PathVariable("id") String id) throws IOException {
//        //Delete the file
//        Optional<DataSource> optionalDataSource = repository.findById(id);
//        if (optionalDataSource.isPresent()) {
//            DataSource ds = optionalDataSource.get();
//            storageService.delete(ds.getPath());
//        }
//        //Delete from MongoDB
//        dataSourceService.delete(id);
//        LOGGER.info(LOG_MSG, "deleteDataSources", id, HttpStatus.NO_CONTENT.toString() );
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
////    @DeleteMapping
////    public ResponseEntity<HttpStatus> deleteDataSources() {
////        try {
////            repository.deleteAll();
////            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
////        } catch (Exception e) {
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////    }
//
//}
