package edu.upc.essi.dtim.metadatastorage.services.impl;

import edu.upc.essi.dtim.metadatastorage.config.Namespaces;
import edu.upc.essi.dtim.metadatastorage.config.SourceGraph;
import edu.upc.essi.dtim.metadatastorage.models.Attribute;
import edu.upc.essi.dtim.metadatastorage.models.DataSource;
import edu.upc.essi.dtim.metadatastorage.models.Wrapper;
import edu.upc.essi.dtim.metadatastorage.repository.DataSourcesRepository;
import edu.upc.essi.dtim.metadatastorage.repository.WrapperRepository;
import edu.upc.essi.dtim.metadatastorage.utils.jena.GraphOperations;
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
    //Pre:
    //  INPUT: Wrapper name, dataSourceId, Array of strings of the names of the attributes.
    //Post:
    //  Generated the triples of Jena inserted into the dataSource graph.
    //Example:
    /*
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
        //Delete from Jena
        Optional<Wrapper> optionalWrapper = wrapperRepository.findById(id);
        if (optionalWrapper.isPresent()) {
            Wrapper w = optionalWrapper.get();
            DataSource ds = new DataSource();
            Optional<DataSource> optionalDataSources = dataSourcesRepository.findById(w.getDataSourcesId());
            if (optionalDataSources.isPresent()) {
                ds = optionalDataSources.get();
            }
            delete(w, ds);

        }
    }

    public void delete(Wrapper w, DataSource ds) {
        //Delete from Jena
        graphOperations.deleteTriplesWithSubject(ds.getIri(), createWrapperIri(w.getName()));
        graphOperations.deleteTriplesWithObject(ds.getIri(), createWrapperIri(w.getName()));

        //Delete from Mongo
        wrapperRepository.deleteById(w.getId());
    }
}
