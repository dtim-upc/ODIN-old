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

    //Create sameAs relations in the datasource graph of a wrapper.
    public void createLAVMappingMapsTo(LavMapping lavMapping){

        Optional<Wrapper> optionalWrapper = wrapperRepository.findById(lavMapping.getWrapperId());
        String dsIRI = "";
        if (optionalWrapper.isPresent()) {
            Wrapper wrapper = optionalWrapper.get();
            Optional<DataSource> optionalDataSource =  dataSourcesRepository.findById(wrapper.getDataSourcesId());
            if (optionalDataSource.isPresent()) {
                DataSource dataSource = optionalDataSource.get();
                dsIRI = dataSource.getIri();
            }
        }
        lavMappingRepository.save(lavMapping);

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
        String finalWIri = dsIRI;

        //updateTriples(lavMapping.getSameAs(), lavMapping.getId(),
        //        finalWIri, finalDsIri);

    }
    /**
     * Updates feature iri in datasource, lavmapping and deletes triples from wrapper.
     * @param features array of modified features.
     * @param LAVMappingID id of the LAVMapping to be updated in mongodb.
     * @param wrapperIRI IRI of the wrapper to be deleted in jena.
     * @param datasourceIRI IRI of the datasource to be updated in jena.
    public void updateTriples(JSONArray features,String LAVMappingID, String wrapperIRI, String datasourceIRI){

        for (Object selectedElement : features) {
            JSONObject objSelectedElement = (JSONObject) selectedElement;
            String oldIRI = objSelectedElement.getAsString("featureOld");
            String newIRI = objSelectedElement.getAsString("featureNew");

            updateLavMappingSameAsFeature(LAVMappingID,oldIRI,newIRI);
            graphOperations.updateResourceNodeIRI(datasourceIRI,oldIRI,newIRI);
        }
//        RDFUtil.deleteTriplesNamedGraph(wrapperIRI);
        graphOperations.deleteAllTriples(wrapperIRI);
        deleteGraphicalSubgraph(LAVMappingID);
    }*/
}
