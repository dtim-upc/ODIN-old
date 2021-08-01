package edu.upc.essi.dtim.ODINBackend.services.impl;

import edu.upc.essi.dtim.ODINBackend.config.Namespaces;
import edu.upc.essi.dtim.ODINBackend.config.SourceGraph;
import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import edu.upc.essi.dtim.ODINBackend.models.Wrapper;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.repository.WrapperRepository;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataSourceService {
    @Autowired
    private GraphOperations graphOperations;
    @Autowired
    private DataSourcesRepository repository;
    @Autowired
    private WrapperRepository wrapperRepository;
    @Autowired
    private LAVMappingService lavMappingService;
    
    public void delete(String id) {
        Optional<DataSource> optionalDataSource = repository.findById(id);

        //First Step: Find wrappers with _dataSourceId == id and delete them
        Iterable<Wrapper> wrapperIterable = wrapperRepository.findAllByDataSourcesId(id);
        for (Wrapper w: wrapperIterable) {
            graphOperations.removeGraph(w.getIri());
            if (!(w.getLavMappingId().equals("") || w.getLavMappingId() == null)) {
                lavMappingService.removeLavMappingFromMongo(w.getLavMappingId());
            }
            wrapperRepository.deleteById(w.getId());
        }

        //Second Step: Delete the data source with _id = id
        optionalDataSource.ifPresent(dataSource ->
                graphOperations.removeGraph(dataSource.getIri())
        );
        repository.deleteById(id);
    }

}
