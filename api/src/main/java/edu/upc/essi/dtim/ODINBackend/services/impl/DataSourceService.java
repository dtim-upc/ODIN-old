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
    
    public void delete(String id) {
        //First Step: Find wrappers with _dataSourceId == id and delete them
        //Second Step: Delete the data source with _id = id
        Optional<DataSource> optionalDataSources = repository.findById(id);

        Iterable<Wrapper> wrapperIterable = wrapperRepository.findAllByDataSourcesId(id);
        for (Wrapper w: wrapperIterable)
            wrapperRepository.deleteById(w.getId());
        repository.deleteById(id);
        optionalDataSources.ifPresent(dataSources ->
                graphOperations.deleteTriples(dataSources.getIri(),
                                                dataSources.getIri(),
                                        Namespaces.rdf.val() + "type",
                                                SourceGraph.DATA_SOURCE.val())
        );

    }

}
