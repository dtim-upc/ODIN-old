package edu.upc.essi.dtim.odin.newBootstrapping;

import edu.upc.essi.dtim.nextiadi.bootstraping.CSVBootstrap;
import edu.upc.essi.dtim.nextiadi.bootstraping.JSONBootstrap;
import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.storage.filestorage.StorageService;
import edu.upc.essi.dtim.odin.utils.jena.NextiaGraphy;
import org.apache.commons.io.FilenameUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class newDataSourceService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private newDataSourceRepository dataSourceRepo;

    public newDataSource persist(newDataSource ds) {
        String newPath = storageService.storePersistent(ds.getFilename());
        String oldPath = ds.getPath();
        ds.setPath(newPath);
        dataSourceRepo.save(ds, oldPath);
        return ds;
    }

    public newDataSource create(newDataSource dataSource, MultipartFile file) throws IOException {

        // save file to temporal landing
        String filename = storageService.storeTemporal(file);
        String path = storageService.getTemporalDir() + "/" +filename;

        // we create a new datasource since id is not generated in the  frontend
        newDataSource _dataSource = new newDataSource(dataSource.getName(), dataSource.getType());
        _dataSource.setPath(path);
        _dataSource.setFilename(filename);
        _dataSource.setFilesize(file.getSize()+"bytes");
        _dataSource.setDescription("");
        _dataSource.setType(getDataSourcetype(file.getOriginalFilename()));
        _dataSource.setProjectID(dataSource.getProjectID());

        Model bootsrapM = bootstrap_schema(_dataSource);
        dataSourceRepo.saveTemporal(_dataSource, bootsrapM);
        return _dataSource;
    }

    public void delete(String id) throws IOException {

        newDataSource ds = dataSourceRepo.findByID(id);

        dataSourceRepo.delete(ds);
        storageService.delete(ds.getPath());

    }


    public void deleteTemporal(String id) throws IOException {

        newDataSource ds = dataSourceRepo.findByIDTemp(id);

        dataSourceRepo.deleteTemporal(ds);
        storageService.delete(ds.getPath());

    }


    public Model bootstrap_schema(newDataSource dataSource) throws IOException {

        Model bootsrapM = ModelFactory.createDefaultModel();
        switch (FilenameUtils.getExtension(dataSource.getPath())){
            case "csv":
                CSVBootstrap bootstrap = new CSVBootstrap();
                bootsrapM = bootstrap.bootstrapSchema(dataSource.getId(),dataSource.getName(),dataSource.getSchemaIRI(),  dataSource.getPath() );
                break;
            case "json":
                JSONBootstrap jsonBootstrap =  new JSONBootstrap();
                bootsrapM = jsonBootstrap.bootstrapSchema(dataSource.getName(), dataSource.getId(), dataSource.getPath());
                break;
            default:
                break;
        }

        NextiaGraphy ng = new NextiaGraphy();
        String visualG = ng.generateVisualGraph(bootsrapM);
//        Graphy gr = new Gson().fromJson(visualG, Graphy.class);
        dataSource.setGraphicalSchema(visualG);
//        generateVisualGraph

//        OWLToWebVOWL vowl = new OWLToWebVOWL();
//        vowl.setNamespace(dataSource.getIri());
//        vowl.setTitle(dataSource.getName());
//        vowl.setPrefix("");
//        String vowlJson = vowl.convertSchema(bootsrapM);

//        dataSource.setGraphicalGraph(vowlJson);
        return bootsrapM;
    }



    public DataSourceTypes getDataSourcetype (String fileName){

        switch (FilenameUtils.getExtension(fileName).toLowerCase().trim()) {
            case "csv":
                return DataSourceTypes.CSV;
            case "json":
                return DataSourceTypes.JSON;
            default:
                return null;
        }
    }


}
