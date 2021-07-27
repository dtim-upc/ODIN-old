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
import net.minidev.json.JSONArray;
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

    public void updateLAVMappingMapsTo(LavMapping lavMapping){

        Optional<Wrapper> optionalWrapper = wrapperRepository.findById(lavMapping.getWrapperId());
        String dsIRI = "";
        String wIRI = "";
        if (optionalWrapper.isPresent()) {
            Wrapper wrapper = optionalWrapper.get();
            wIRI = wrapper.getIri();
            Optional<DataSource> optionalDataSource =  dataSourcesRepository.findById(wrapper.getDataSourcesId());
            if (optionalDataSource.isPresent()) {
                DataSource dataSource = optionalDataSource.get();
                dsIRI = dataSource.getIri();
            }
        }

        String finalDsIri = dsIRI;
        String finalWIri = wIRI;

        //updateTriples(lavMapping.getSameAs(), lavMapping.getId(), finalWIri, finalDsIri);

    }
    /**
     * Updates feature iri in datasource, lavmapping and deletes triples from wrapper.
     * @param features array of modified features.
     * @param LAVMappingID id of the LAVMapping to be updated in mongodb.
     * @param wrapperIRI IRI of the wrapper to be deleted in jena.
     * @param datasourceIRI IRI of the datasource to be updated in jena.
     **/
    public void updateTriples(JSONArray features, String LAVMappingID, String wrapperIRI, String datasourceIRI){

        for (Object selectedElement : features) {
            /*JSONObject objSelectedElement = (JSONObject) selectedElement;
            String oldIRI = objSelectedElement.getAsString("featureOld");
            String newIRI = objSelectedElement.getAsString("featureNew");

            updateLavMappingSameAsFeature(LAVMappingID,oldIRI,newIRI);
            graphOperations.updateResourceNodeIRI(datasourceIRI,oldIRI,newIRI);*/
        }

        graphOperations.deleteAllTriples(wrapperIRI);
        //deleteGraphicalSubgraph(LAVMappingID);
    }

    /**
     * Updates the feature IRI from sameAs array of a LavMapping collection in MongoDB
     * @param LAVMappingID lavmapping id to be updated.
     * @param oldIRI actual iri.
     * @param newIRI new iri.
     */
    public void updateLavMappingSameAsFeature(String LAVMappingID, String oldIRI, String newIRI){
        //lavMappingRepository.update(LAVMappingMongo.FIELD_sameAsFeature.val(),oldIRI,LAVMappingMongo.FIELD_sameAsFeatureUpdate.val(), newIRI);
    }
}
