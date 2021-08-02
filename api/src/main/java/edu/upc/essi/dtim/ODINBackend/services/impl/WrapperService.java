package edu.upc.essi.dtim.ODINBackend.services.impl;

import edu.upc.essi.dtim.ODINBackend.config.Namespaces;
import edu.upc.essi.dtim.ODINBackend.config.SourceGraph;
import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import edu.upc.essi.dtim.ODINBackend.models.Wrapper;
import edu.upc.essi.dtim.ODINBackend.repository.LavMappingRepository;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import edu.upc.essi.dtim.ODINBackend.models.Attribute;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.repository.WrapperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WrapperService {
    @Autowired
    private GraphOperations graphOperations;
    @Autowired
    private DataSourcesRepository dataSourcesRepository;
    @Autowired
    private WrapperRepository wrapperRepository;

    @Autowired
    private LavMappingRepository lavMappingRepository;
    /**
    @pre:
      INPUT: Wrapper name, dataSourceId, Array of strings of the names of the attributes.
    @post:
      Generated the triples of Jena inserted into the dataSource graph.
    Example:
    * INPUT: "w1", "1fd3d01369b947f2a18a70b596006029",["DateOfBirth", "FullName", "teamId"]
    * POST:
    (triple <http://www.essi.upc.edu/~snadal/BDIOntology/Source/Wrapper/w1T> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.essi.upc.edu/~snadal/BDIOntology/Source/Wrapper>)
    (triple <http://www.essi.upc.edu/~snadal/BDIOntology/Source/Wrapper/w1T> <http://www.essi.upc.edu/~snadal/BDIOntology/Source/hasAttribute> <http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/Teams/teamName>)
    (triple <http://www.essi.upc.edu/~snadal/BDIOntology/Source/Wrapper/w1T> <http://www.essi.upc.edu/~snadal/BDIOntology/Source/hasAttribute> <http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/Teams/id>)
    (triple <http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/Teams/teamName> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.essi.upc.edu/~snadal/BDIOntology/Source/Attribute>)
    (triple <http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/Teams/id> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.essi.upc.edu/~snadal/BDIOntology/Source/Attribute>)
    * */
    public void create(String wname, Attribute[] attrNames, String dataSourceId) {
        Optional<DataSource> optionalDataSources = dataSourcesRepository.findById(dataSourceId);
        if (optionalDataSources.isPresent()) {
            DataSource ds = optionalDataSources.get();
            String wIRI = createWrapperIri(wname);
            graphOperations.addTriple(ds.getIri(), wIRI, Namespaces.rdf.val() + "type", SourceGraph.WRAPPER.val());
            graphOperations.addTriple(ds.getIri(), ds.getIri(), SourceGraph.HAS_WRAPPER.val(), wIRI);
            for (Attribute attribute: attrNames) {
                String attName = attribute.getName();
                String attIRI = ds.getIri() + '/' + attName;
                graphOperations.addTriple(ds.getIri(), attIRI, Namespaces.rdf.val() + "type", SourceGraph.ATTRIBUTE.val());
                graphOperations.addTriple(ds.getIri(), wIRI, SourceGraph.HAS_ATTRIBUTE.val(), attIRI);
            }
        }
    }
    private String createWrapperIri(String name) {
        return SourceGraph.WRAPPER.val() + '/' + name;
    }

    public void delete(String id) {
        Optional<Wrapper> optionalWrapper = wrapperRepository.findById(id);
/*
        if (optionalWrapper.isPresent()) {
            Wrapper w = optionalWrapper.get();
            DataSource ds = new DataSource();
            Optional<DataSource> optionalDataSources = dataSourcesRepository.findById(w.getDataSourcesId());
            if (optionalDataSources.isPresent()) {
                ds = optionalDataSources.get();
            }
            delete(w, ds);

        }
*/


        if (optionalWrapper.isPresent()) {
            Wrapper w = optionalWrapper.get();
            //Delete from Jena
            graphOperations.removeGraph(w.getIri());
            //Delete all lavMappings with lavMappingId
            lavMappingRepository.deleteById(w.getLavMappingId());
        }
        //Delete from Mongo
        wrapperRepository.deleteById(id);
    }

    public void delete(Wrapper w, DataSource ds) {
        //Delete from Jena
        //DsIri = http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/asd
        //WIri = http://www.essi.upc.edu/~snadal/BDIOntology/Source/Wrapper/w1
        graphOperations.deleteTriplesWithSubject(ds.getIri(), createWrapperIri(w.getName()));
        graphOperations.deleteTriplesWithObject(ds.getIri(), createWrapperIri(w.getName()));
        //Eliminación de attributos
        for (Attribute a : w.getAttributes()) {
            String attName = a.getName();
            String attIRI = ds.getIri() + '/' + attName;
            graphOperations.deleteTriplesWithSubject(ds.getIri(), attIRI);
            graphOperations.deleteTriplesWithObject(ds.getIri(), attIRI);
        }


        //Delete from Mongo
        wrapperRepository.deleteById(w.getId());
    }
}
