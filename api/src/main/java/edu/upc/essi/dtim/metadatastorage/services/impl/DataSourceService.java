package edu.upc.essi.dtim.metadatastorage.services.impl;

import edu.upc.essi.dtim.metadatastorage.config.Namespaces;
import edu.upc.essi.dtim.metadatastorage.config.SourceGraph;
import edu.upc.essi.dtim.metadatastorage.models.DataSource;
import edu.upc.essi.dtim.metadatastorage.models.Wrapper;
import edu.upc.essi.dtim.metadatastorage.repository.DataSourcesRepository;
import edu.upc.essi.dtim.metadatastorage.repository.WrapperRepository;
import edu.upc.essi.dtim.metadatastorage.utils.jena.GraphOperations;
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
