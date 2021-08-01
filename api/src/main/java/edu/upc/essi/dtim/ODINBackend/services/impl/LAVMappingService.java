package edu.upc.essi.dtim.ODINBackend.services.impl;

import edu.upc.essi.dtim.ODINBackend.config.Namespaces;
import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import edu.upc.essi.dtim.ODINBackend.models.LavMapping;
import edu.upc.essi.dtim.ODINBackend.models.SameAs;
import edu.upc.essi.dtim.ODINBackend.models.Wrapper;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.repository.LavMappingRepository;
import edu.upc.essi.dtim.ODINBackend.repository.WrapperRepository;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class LAVMappingService {

    @Autowired
    private WrapperRepository wrapperRepository;
    @Autowired
    private DataSourcesRepository dataSourcesRepository;
    @Autowired
    private LavMappingRepository lavMappingRepository;
    @Autowired
    private GraphOperations graphOperations;

    public void deleteLavMappingByGlobalGraphId(String globalGraphId) {
        Iterable<LavMapping> lavMappingIterable = lavMappingRepository.findAllByGlobalGraphId(globalGraphId);

        for (LavMapping l: lavMappingIterable) {
            lavMappingRepository.deleteById(l.getId());
            //Serà
            //delete(l.getId());
        }
    }

    //Saves LavMapping to repository
    //Create sameAs relations in the datasource graph of a wrapper.
    //Adds id to LavMappingId
    public void createLAVMappingMapsTo(LavMapping lavMapping){

        Optional<Wrapper> optionalWrapper = wrapperRepository.findById(lavMapping.getWrapperId());
        String dsIRI = "";
        Wrapper wrapper = null;
        if (optionalWrapper.isPresent()) {
            wrapper = optionalWrapper.get();
            Optional<DataSource> optionalDataSource =  dataSourcesRepository.findById(wrapper.getDataSourcesId());
            if (optionalDataSource.isPresent()) {
                DataSource dataSource = optionalDataSource.get();
                dsIRI = dataSource.getIri();
            }
        }
        lavMappingRepository.save(lavMapping);
        if (optionalWrapper.isPresent()) {
            System.out.println("Setting id = " + lavMapping.getId());
            wrapper.setLavMappingId(lavMapping.getId());
            wrapperRepository.save(wrapper);
        }

        String finalDsIRI = dsIRI;
        for (SameAs sa : lavMapping.getSameAs()) {
            graphOperations.addTriple(finalDsIRI, sa.getAttribute(), Namespaces.owl.val() + "sameAs", sa.getFeature());
        }
    }

    public void removeLavMappingFromMongo(String lavMappingId) {
        lavMappingRepository.deleteById(lavMappingId);
    }

    public void delete(String LAVMappingID){
        Optional<LavMapping> optionalLavMapping = lavMappingRepository.findById(LAVMappingID);
        if (optionalLavMapping.isPresent()) {
            LavMapping lavMapping = optionalLavMapping.get();
            Optional<Wrapper> optionalWrapper = wrapperRepository.findById(LAVMappingID);
            Optional<DataSource> optionalDataSource = dataSourcesRepository.findById(LAVMappingID);
            if (optionalWrapper.isPresent() && optionalDataSource.isPresent()) {
                delete(lavMapping, optionalWrapper.get(), optionalDataSource.get());
            }
        }

    }

    public void delete(LavMapping lavMapping, Wrapper wrapper, DataSource dataSource){
        // Remove the sameAs edges
        for (SameAs el : lavMapping.getSameAs()) {
            String feature = el.getFeature();
            String attribute = el.getAttribute();
            graphOperations.deleteTriples(dataSource.getIri(),
                    attribute, Namespaces.owl.val() + "sameAs",feature);
        }

        //Remove the named graph of that mapping
        graphOperations.removeGraph(wrapper.getIri());

        //Remove the associated metadata from MongoDB
        removeLavMappingFromMongo(lavMapping.getId());
    }
}
