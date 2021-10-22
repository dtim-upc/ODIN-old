package edu.upc.essi.dtim.ODINBackend.services.impl;

import edu.upc.essi.dtim.ODINBackend.config.DataSourceTypes;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.SourceGraph;
import edu.upc.essi.dtim.ODINBackend.models.mongo.DataSource;
import edu.upc.essi.dtim.ODINBackend.models.mongo.Wrapper;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.repository.WrapperRepository;
import edu.upc.essi.dtim.ODINBackend.services.filestorage.StorageProperties;
import edu.upc.essi.dtim.ODINBackend.services.filestorage.StorageService;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import edu.upc.essi.dtim.ODINBackend.utils.jena.parsers.OWLToWebVOWL;
import edu.upc.essi.dtim.nextiadi.bootstraping.CSVBootstrap;
import edu.upc.essi.dtim.nextiadi.bootstraping.JSONBootstrap_new;
import org.apache.commons.io.FilenameUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataSourceService {

    @Autowired
    StorageProperties properties;
    @Autowired
    private GraphOperations graphOperations;
    @Autowired
    private DataSourcesRepository repository;
    @Autowired
    private WrapperRepository wrapperRepository;
    @Autowired
    private WrapperService wService;

    @Autowired
    private StorageService storageService;



    public DataSource create(DataSource dataSource, Boolean bootstrappingType, MultipartFile file) throws IOException {
        String path = storageService.store(file);

        DataSource _dataSource = new DataSource(dataSource.getName(), dataSource.getType());
        _dataSource.setPath(path);
        _dataSource.setType(getDataSourcetype(file.getOriginalFilename()));

        if(  Boolean.TRUE.equals(bootstrappingType)) {
            _dataSource = bootstrap_schema(_dataSource);
        } else {
            graphOperations.addTriple(_dataSource.getIri(),
                    _dataSource.getIri(),
                    RDF.getURI() + "type",
                    SourceGraph.DATA_SOURCE.val());
        }
        repository.save(_dataSource);
        return _dataSource;

    }



    public void delete(String id) {
        Optional<DataSource> optionalDataSource = repository.findById(id);

        //First Step: Find wrappers with _dataSourceId == id and delete them
        Iterable<Wrapper> wrapperIterable = wrapperRepository.findAllByDataSourcesId(id);
        for (Wrapper w: wrapperIterable) {
            graphOperations.removeGraph(w.getIri());
//            if (!(w.getLavMappingId().equals("") || w.getLavMappingId() == null)) {
//                lavMappingService.removeLavMappingFromMongo(w.getLavMappingId());
//            }
            wrapperRepository.deleteById(w.getId());

        }

        //Second Step: Delete the data source with _id = id
        optionalDataSource.ifPresent(dataSource ->
                graphOperations.removeGraph(dataSource.getIri())
        );
        repository.deleteById(id);
    }



    public void verifyDir(String path){
        File theDir = new File(path);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    public DataSourceTypes getDataSourcetype(String fileName){

        switch (FilenameUtils.getExtension(fileName).toLowerCase().trim()) {
            case "csv":
                return DataSourceTypes.CSV;
            case "json":
                return DataSourceTypes.JSON;
            default:
                return null;
        }

    }

    public DataSource bootstrap_schema(DataSource dataSource) throws IOException {

        String filename = FilenameUtils.getName(dataSource.getPath());
        String dir = properties.getLocation()+"/bootstrapping/";
        verifyDir(dir);
        Model bootsrapM = ModelFactory.createDefaultModel();
        switch (FilenameUtils.getExtension(dataSource.getPath())){
            case "csv":

                CSVBootstrap bootstrap = new CSVBootstrap();
                bootsrapM = bootstrap.bootstrapSchema(dataSource.getIri(),  dataSource.getPath() );

                break;
            case "json":
                JSONBootstrap_new jsonBootstrap =  new JSONBootstrap_new();
                bootsrapM = jsonBootstrap.bootstrapSchema(dataSource.getIri(),  dataSource.getPath());

                break;
            default:
                break;
        }

        OWLToWebVOWL vowl = new OWLToWebVOWL();
        vowl.setNamespace(dataSource.getIri());
        vowl.setTitle(dataSource.getName());
        vowl.setPrefix("");
        String vowlJson = vowl.convertSchema(bootsrapM);
        dataSource.setGraphicalGraph(vowlJson);

//        graphOperations.addModel(dataSource.getIri(), bootsrapM);
        Wrapper w = wService.createWrapperBootstrapped(bootsrapM, dataSource.getName(), dataSource.getId());

        graphOperations.addTriple(dataSource.getIri(),
                dataSource.getIri(),
                RDF.getURI() + "type",
                SourceGraph.DATA_SOURCE.val());

        graphOperations.addTriple(dataSource.getIri(),
                dataSource.getIri(),
                Namespaces.S.val()+"hasWrapper",
                w.getId());

        graphOperations.addTriple(dataSource.getIri(),
                dataSource.getIri(),
                RDFS.label.getURI(),
                dataSource.getName());

        List<Wrapper> ls = dataSource.getWrappers();
        if(ls == null){
            ls = new ArrayList<>();
        }
        ls.add(w);
        dataSource.setWrappers(ls);

        return dataSource;
    }

}
