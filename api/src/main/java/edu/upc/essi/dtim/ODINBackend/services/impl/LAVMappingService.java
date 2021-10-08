package edu.upc.essi.dtim.ODINBackend.services.impl;

import edu.upc.essi.dtim.ODINBackend.config.Namespaces;
import edu.upc.essi.dtim.ODINBackend.models.mongo.DataSource;
import edu.upc.essi.dtim.ODINBackend.models.LavMapping;
import edu.upc.essi.dtim.ODINBackend.models.SameAs;
import edu.upc.essi.dtim.ODINBackend.models.mongo.Wrapper;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.repository.LavMappingRepository;
import edu.upc.essi.dtim.ODINBackend.repository.WrapperRepository;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(LAVMappingService.class);

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
            LOGGER.info("Setting id = " + lavMapping.getId());
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

    public void updateLAVMappingMapsTo(SameAs[] sameAsArr, LavMapping lavMapping) {
        Optional<Wrapper> optionalWrapper = wrapperRepository.findById(lavMapping.getWrapperId());
        if (optionalWrapper.isPresent()) {
            Wrapper wrapper = optionalWrapper.get();
            Optional<DataSource> optionalDataSource = dataSourcesRepository.findById(wrapper.getDataSourcesId());
            if (optionalDataSource.isPresent()) {
                DataSource dataSource = optionalDataSource.get();
                updateTriples(sameAsArr, lavMapping.getId(),
                        wrapper.getIri(), dataSource.getIri());
            }
        }
    }
    /**
     * Updates feature iri in datasource, lavmapping and deletes triples from wrapper.
     * @param features array of modified features.
     * @param LAVMappingID id of the LAVMapping to be updated in mongodb.
     * @param wrapperIRI IRI of the wrapper to be deleted in jena.
     * @param datasourceIRI IRI of the datasource to be updated in jena.
     */
    public void updateTriples(SameAs[] features, String LAVMappingID, String wrapperIRI, String datasourceIRI) {

        return;
        /*
        for (Object selectedElement : features) {
            JSONObject objSelectedElement = (JSONObject) selectedElement;
            String oldIRI = objSelectedElement.getAsString("featureOld");
            String newIRI = objSelectedElement.getAsString("featureNew");

            updateLavMappingSameAsFeature(LAVMappingID,oldIRI,newIRI);
            graphOperations.updateResourceNodeIRI(datasourceIRI,oldIRI,newIRI);
        }
        graphOperations.deleteAllTriples(wrapperIRI);
        deleteGraphicalSubgraph(LAVMappingID);
        */
    }
}
