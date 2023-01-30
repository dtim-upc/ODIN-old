package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.nextiadi.bootstraping.CSVBootstrap;
import edu.upc.essi.dtim.nextiadi.bootstraping.JSONBootstrapSWJ;
import edu.upc.essi.dtim.odin.bootstrapping.pojos.DataSource;
import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.projects.Project;
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
public class DataSourceService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private DataSourceRepository dataSourceRepo;

    public DataSource persist(Project p, DataSource ds) {
        String newPath = storageService.storePersistent(ds.getFilename());
        String oldPath = ds.getPath();
        ds.setPath(newPath);
        dataSourceRepo.save(ds, oldPath, p);
        return ds;
    }

    public DataSource create(DataSource dataSource, MultipartFile file) throws IOException {

        // save file to temporal landing
        String filename = storageService.storeTemporal(file);
        String path = storageService.getTemporalDir() + "/" +filename;

        // we create a new datasource since id is not generated in the  frontend
        DataSource _dataSource = new DataSource(dataSource.getName(), dataSource.getType());
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

        DataSource ds = dataSourceRepo.findByID(id);

        dataSourceRepo.delete(ds);
        storageService.delete(ds.getPath());

    }


    public void deleteTemporal(String id) throws IOException {

        DataSource ds = dataSourceRepo.findByIDTemp(id);

        dataSourceRepo.deleteTemporal(ds);
        storageService.delete(ds.getPath());

    }


    public Model bootstrap_schema(DataSource dataSource) throws IOException {

        Model bootsrapM = ModelFactory.createDefaultModel();
        switch (FilenameUtils.getExtension(dataSource.getPath())){
            case "csv":
                CSVBootstrap bootstrap = new CSVBootstrap();
//                bootsrapM = bootstrap.bootstrapSchema(dataSource.getId(),dataSource.getName(),dataSource.getSchemaIRI(),  dataSource.getPath() );
                bootsrapM = bootstrap.bootstrapSchema(dataSource.getId(),dataSource.getName(),  dataSource.getPath() );
                break;
            case "json":
//                JSONBootstrap jsonBootstrap =  new JSONBootstrap();
//                bootsrapM = jsonBootstrap.bootstrapSchema(dataSource.getName(), dataSource.getId(), dataSource.getPath());

                JSONBootstrapSWJ j = new JSONBootstrapSWJ();
                bootsrapM = j.bootstrapSchema(dataSource.getName(), dataSource.getId(), dataSource.getPath());


                break;
            default:
                break;
        }

        NextiaGraphy ng = new NextiaGraphy();
//        String visualG = ng.generateVisualGraph(bootsrapM);
        String visualG = ng.generateVisualGraphNew(bootsrapM);

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
