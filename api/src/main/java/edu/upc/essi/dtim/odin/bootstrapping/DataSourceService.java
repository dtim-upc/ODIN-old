package edu.upc.essi.dtim.odin.services.impl;

import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.SourceGraph;
import edu.upc.essi.dtim.odin.models.mongo.DataSource;
import edu.upc.essi.dtim.odin.models.mongo.Wrapper;
import edu.upc.essi.dtim.odin.repository.DataSourcesRepository;
import edu.upc.essi.dtim.odin.repository.WrapperRepository;
import edu.upc.essi.dtim.odin.storage.filestorage.StorageProperties;
import edu.upc.essi.dtim.odin.storage.filestorage.StorageService;
import edu.upc.essi.dtim.odin.utils.jena.GraphOperations;
import edu.upc.essi.dtim.odin.utils.jena.parsers.OWLToWebVOWL;
import edu.upc.essi.dtim.nextiadi.bootstraping.CSVBootstrap;
import edu.upc.essi.dtim.nextiadi.bootstraping.JSONBootstrap;
import org.apache.commons.io.FilenameUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDF;
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
//          TODO: Delete this code since ODIN v2 should bootstrap automatically all sources and then allow users to make views for privacy purposes (wrappers)
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

//        String filename = FilenameUtils.getName(dataSource.getPath());
//        String dir = properties.getLocation()+"/bootstrapping/";
//        verifyDir(dir);
        Model bootsrapM = ModelFactory.createDefaultModel();
        switch (FilenameUtils.getExtension(dataSource.getPath())){
            case "csv":

                CSVBootstrap bootstrap = new CSVBootstrap();
                bootsrapM = bootstrap.bootstrapSchema(dataSource.getSchemaIRI(),  dataSource.getPath() );

                break;
            case "json":
                JSONBootstrap jsonBootstrap =  new JSONBootstrap();
                bootsrapM = jsonBootstrap.bootstrapSchema(dataSource.getName(), dataSource.getId(), dataSource.getPath());

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

        graphOperations.addModel(dataSource.getIri(), bootsrapM);

        graphOperations.addTripleLiteral(dataSource.getIri(),
                dataSource.getIri(),
                DataSourceGraph.GRAPHICAL.val(),
                vowlJson
                );
//        graphOperations.addTriple(dataSource.getIri(),
//                dataSource.getIri(),
//                RDF.getURI() + "type",
//                Namespaces.DataSource.val());

//        graphOperations.addTripleLiteral(dataSource.getIri(),
//                dataSource.getIri(),
//                RDFS.label.getURI(),
//                dataSource.getName());

//        graphOperations.addTripleLiteral(dataSource.getIri(),
//                dataSource.getIri(),
//                DataSourceGraph.HAS_PATH.val(),
//                dataSource.getPath());

//        graphOperations.addTripleLiteral(dataSource.getIri(),
//                dataSource.getIri(),
//                DataSourceGraph.HAS_FORMAT.val(),
//                dataSource.getType().toString());

//        graphOperations.addTripleLiteral(dataSource.getIri(),
//                dataSource.getIri(),
//                DataSourceGraph.HAS_ID.val(),
//                dataSource.getId() );

//        if(dataSource.getType().equals(DataSourceTypes.CSV)){
//            graphOperations.addTripleLiteral(dataSource.getIri(),
//                    dataSource.getIri(),
//                    DataSourceGraph.HAS_SEPARATOR.val(),
//                    ",");
//        }

//        String f = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/"+dataSource.getName()+"_sourceGraph.ttl";
//        graphOperations.write(f, graphOperations.getGraph(dataSource.getIri()), dataSource.getId());

        List<Wrapper> ls = dataSource.getWrappers();
        if(ls == null){
            ls = new ArrayList<>();
        }
//        ls.add(w);
        dataSource.setWrappers(ls);

        return dataSource;
    }

}
