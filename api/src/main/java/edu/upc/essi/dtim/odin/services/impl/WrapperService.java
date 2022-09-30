package edu.upc.essi.dtim.odin.services.impl;

import edu.upc.essi.dtim.Graph;
import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.config.vocabulary.SourceGraph;
import edu.upc.essi.dtim.odin.models.mongo.helpers.Attribute;
import edu.upc.essi.dtim.odin.bootstrapping.DataSource;
import edu.upc.essi.dtim.odin.models.mongo.Wrapper;
import edu.upc.essi.dtim.odin.repository.DataSourcesRepository;
import edu.upc.essi.dtim.odin.repository.WrapperRepository;
import edu.upc.essi.dtim.odin.services.omq.WrapperI;
//import edu.upc.essi.dtim.odin.utils.jena.GraphOperations;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class WrapperService {


//    @Autowired
//    private GraphOperations graphOperations;
    @Autowired
    private JenaConnection graph;
    @Autowired
    private DataSourcesRepository dataSourcesRepository;
    @Autowired
    private WrapperRepository wrapperRepository;

    private Map<DataSourceTypes, WrapperI> wrappers;

    @Autowired
    public WrapperService(List<WrapperI> wrappers) {
        this.wrappers =  wrappers.stream().collect(Collectors.toMap(WrapperI::getType, Function.identity()));
    }


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
    public Graph create(String wname, Attribute[] attrNames, String dataSourceId) {
        Optional<DataSource> optionalDataSources = dataSourcesRepository.findById(dataSourceId);
        if (optionalDataSources.isPresent()) {
            DataSource ds = optionalDataSources.get();
            String wIRI = createWrapperIri(wname);
            graph.persistent().addTriple(ds.getIri(), wIRI, RDF.getURI() + "type", SourceGraph.WRAPPER.val());
            graph.persistent().addTriple(ds.getIri(), ds.getIri(), SourceGraph.HAS_WRAPPER.val(), wIRI);
            for (Attribute attribute: attrNames) {
                String attName = attribute.getName();
                String attIRI = ds.getIri() + '/' + attName;
                graph.persistent().addTriple(ds.getIri(), attIRI, RDF.getURI() + "type", SourceGraph.ATTRIBUTE.val());
                graph.persistent().addTriple(ds.getIri(), wIRI, SourceGraph.HAS_ATTRIBUTE.val(), attIRI);
            }

            return graph.persistent().getGraph(ds.getIri());
        }
        return null;
    }

    public Wrapper createWrapperBootstrapped(Graph bootstrap, String dsName, String dsID) {

        // TODO: probably wrapper name should be given by user or generated incrementally for x wrappers (future)
        String wIRI = createWrapperIri(dsName +"Wrapper");

        graph.persistent().addModel(wIRI, bootstrap);
        graph.persistent().addTriple(wIRI,wIRI, RDF.type.getURI(), SourceGraph.WRAPPER.val());

        Wrapper w = new Wrapper();
        w.setName(dsName+"Wrapper");
        w.setIri(wIRI);
        w.setDataSourcesId(dsID);

        return wrapperRepository.save(w);
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
            graph.persistent().deleteGraph(w.getIri());
            //Delete all lavMappings with lavMappingId
//            lavMappingRepository.deleteById(w.getLavMappingId());
        }
        //Delete from Mongo
        wrapperRepository.deleteById(id);
    }

    public void delete(Wrapper w, DataSource ds) {
        //Delete from Jena
        //DsIri = http://www.essi.upc.edu/~snadal/BDIOntology/Source/DataSource/asd
        //WIri = http://www.essi.upc.edu/~snadal/BDIOntology/Source/Wrapper/w1
        graph.persistent().deleteTriplesWithSubject(ds.getIri(), createWrapperIri(w.getName()));
        graph.persistent().deleteTriplesWithObject(ds.getIri(), createWrapperIri(w.getName()));
        //Eliminación de attributos
        for (Attribute a : w.getAttributes()) {
            String attName = a.getName();
            String attIRI = ds.getIri() + '/' + attName;
            graph.persistent().deleteTriplesWithSubject(ds.getIri(), attIRI);
            graph.persistent().deleteTriplesWithObject(ds.getIri(), attIRI);
        }


        //Delete from Mongo
        wrapperRepository.deleteById(w.getId());
    }


    public List<String> inferSchema(DataSource ds) throws Exception {

        WrapperI w = wrappers.get( ds.getType() );
        return w.inferSchema( ds );


    }

}
