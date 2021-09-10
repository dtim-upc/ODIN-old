package edu.upc.essi.dtim.ODINBackend.utils.jena.parsers;


import com.google.gson.Gson;
import edu.upc.essi.dtim.ODINBackend.config.SourceGraph;
import edu.upc.essi.dtim.ODINBackend.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.ODINBackend.controller.manualBootstraping.LavMappingsController;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import edu.upc.essi.dtim.ODINBackend.utils.jena.parsers.models.*;
import edu.upc.essi.dtim.nuupdi.config.Vocabulary;
import edu.upc.essi.dtim.nuupdi.jena.Graph;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class OWLToWebVOWL {

    @Autowired
    GraphOperations graphO;

    private static final Logger LOGGER = LoggerFactory.getLogger(OWLToWebVOWL.class);

    String prefix = "G";
    String namespace = "";
    String title = "";
    HashMap<String, String> nodesId;
    HashMap<String, String> propertiesId;


    List<Triple> triples;
    List<Nodes> nodes;
    List<Property> properties;

    List<ClassAttribute> classA;
    List<PropertyAttribute> proA;

    List<Triple> cleanTriples;


    public HashMap<String, String> getNodesId() { return nodesId; }

    public OWLToWebVOWL() {
        setUp();
    }

    public OWLToWebVOWL(String namespace, String title) {

        this.namespace = namespace;
        this.title = title;
        this.prefix = "";
        setUp();
    }

    public void setUp(){
        triples = new ArrayList<>();
        nodes = new ArrayList<>();
        properties = new ArrayList<>();

        classA = new ArrayList<>();
        proA = new ArrayList<>();

        nodesId = new HashMap<String, String>();
        propertiesId= new HashMap<String, String>();
        cleanTriples = new ArrayList<>();

    }


    public Header generateHeader() {
        Header h = new Header();
        List<String> baseIri = new ArrayList<>();
        baseIri.add("http://www.w3.org/2000/01/rdf-schema");
        h.setBaseIris(baseIri);
        h.setDescription(new ElementLangEn("new Ontology description"));
        h.setIri(namespace);
        List<String> lang = new ArrayList<>();
        lang.add("en");
        h.setLanguages(lang);
        h.setTitle(new ElementLangEn(this.title));
        return h;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    private String sparqlQueryPrefixes = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + "PREFIX G: <http://www.essi.upc.edu/~snadal/BDIOntology/Global/> \n "+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";




    public String convertSchema(Model model) {

        setUp();

        Graph graph = new Graph();
        graph.setModel(model);

        List<Subject> listS = new ArrayList<>();

        for( Resource r : model.listSubjects().toList() ) {

            Subject s = new Subject();
            s.setIri(r.getURI());

            for (Statement statement : r.listProperties().toList()) {

                if (statement.getPredicate().equals(RDF.type)) {
                    s.setType(statement.getObject().toString());
                } else if (statement.getPredicate().equals(RDFS.domain)) {
                    s.setDomain(statement.getObject().toString());
                } else if (statement.getPredicate().equals(RDFS.range)) {
                    s.setRange(statement.getObject().toString());
                } else if (statement.getPredicate().getURI().equals(SourceGraph.HAS_ATTRIBUTE.val()) || statement.getPredicate().getURI().equals(SourceGraph.HAS_WRAPPER.val())) {

                    Subject property = new Subject();
                    property.setIri(statement.getPredicate().getURI());
                    property.setType(statement.getPredicate().getURI());
                    property.setDomain(s.getIri());
                    property.setRange(statement.getObject().toString());
                    listS.add(property);
                } else if(statement.getPredicate().equals(RDFS.subClassOf)  || statement.getPredicate().equals(RDFS.subPropertyOf)

                ) {

                    Subject property = new Subject();
                    property.setIri(statement.getPredicate().getURI());
                    property.setType(statement.getPredicate().getURI());
                    property.setDomain(statement.getSubject().toString());
                    property.setRange(statement.getObject().toString());
                    listS.add(property);
            } else{
                    // do nothing. Probably statement not useful for graphical graph
                    LOGGER.info("NOT USEFUL: "+ statement.getSubject() +","+statement.getPredicate()+", "+statement.getObject() );
                }

            }
            if(s.getType() == null) {
                LOGGER.info("SOMETHING IS WRONG IN THE MODEL. No type definition for resource "+s.getIri());
            } else {
                listS.add(s);
            }

        }



        List<String> propertiesTypes = new ArrayList<>();
            propertiesTypes.add(RDF.Property.getURI());
            propertiesTypes.add(OWL.DatatypeProperty.getURI());
            propertiesTypes.add(OWL.ObjectProperty.getURI());
            propertiesTypes.add(RDFS.ContainerMembershipProperty.getURI());

            propertiesTypes.add(RDFS.subPropertyOf.getURI());
            propertiesTypes.add(RDFS.subClassOf.getURI());

        propertiesTypes.add(Vocabulary.IntegrationDProperty.val());
        propertiesTypes.add(Vocabulary.IntegrationOProperty.val());

        propertiesTypes.add(SourceGraph.HAS_WRAPPER.val() );
        propertiesTypes.add(SourceGraph.HAS_ATTRIBUTE.val() );

//        "http://www.w3.org/1999/02/22-rdf-syntax-ns#Property"
        List<Subject> classesInfo = listS.stream()
                .filter(s -> !propertiesTypes.contains(s.getType()) ).collect(Collectors.toList());
        List<Subject> propertiesInfo = listS.stream()
                .filter(s -> propertiesTypes.contains(s.getType()) && !s.getType().equals(RDFS.subPropertyOf.getURI()) ).collect(Collectors.toList());
        List<Subject> subProperties = listS.stream()
                .filter(s -> s.getType().equals(RDFS.subPropertyOf.getURI())).collect(Collectors.toList());

        // create nodes
        int nodeId = 1;
        for (Subject s: classesInfo) {
            String id = createNode(s, nodeId);
            nodesId.put(s.getIri(), id);
            nodeId++;
        }

        // create properties
        int iProperties = 1;
        for (Subject s : propertiesInfo) {

            String id = "property" + iProperties;
            String type = s.getPropertyType();
            propertiesId.put(s.getIri(), id);

            String domainID = nodesId.get(s.getDomain());
            String rangeID = nodesId.get(s.getRange());
            if( type.equals(OWL.DatatypeProperty.getURI())){
                // create node for datatype, we don't save datatypes ids since the iri (key) can be duplicated
                rangeID = createLiteralNode(s, nodeId);
                nodeId++;
            }

            properties.add(new Property(id,typeWithPrefix( s.getType() ) ));
            proA.add( new PropertyAttribute( id, s.getIri(), domainID, rangeID ) )  ;


            iProperties++;
        }


        for (Subject s : subProperties) {

            String id = "property" + iProperties;
            String type = s.getPropertyType();

            String domainID = propertiesId.get(s.getDomain());
            String rangeID = propertiesId.get(s.getRange());

            properties.add(new Property(id,typeWithPrefix( s.getType() ) ));
            proA.add( new PropertyAttribute( id, s.getIri(), domainID, rangeID ) )  ;

            iProperties++;
        }


//        graph.runAQuery(sparqlQueryPrefixes + " SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER NOT EXISTS {?s G:sameAs ?o .}  FILTER NOT EXISTS {  ?s ?p <"+Namespaces.SCHEMA.val() + "identifier>. }} ").forEachRemaining(res -> {
//            triples.add(new Triple(new ResourceImpl(res.get("s").toString()).asNode(),
//                    new PropertyImpl(res.get("p").toString()).asNode(), new ResourceImpl(res.get("o").toString()).asNode()));
//        });

//        int i = 1;
//        //look for types triples and create nodes;
//        for (Triple triple : triples) {
//            if (triple.getPredicate().getURI().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
//                String id = "Class" + i;
//                nodesId.put(triple.getSubject().getURI(), id);
//                //this check is to hide to triples like feature sameAs feature. Such features are not required because they are subsumed by the global iri feature.
////                if(!triplesHashMap.containsKey(triple)){
//                    if(triple.getSubject().getURI().contains(Namespaces.G.val()) && triple.getObject().getURI().equals(GlobalGraph.FEATURE.val())) //URI comes from alignments
//                        nodes.add(new Nodes(id, prefix + ":Feature_ID"));
//                    else
//                        nodes.add(new Nodes(id, triple.getObject().getURI()));
//                    classA.add(new ClassAttribute(id, triple.getSubject().getURI(), getBaseIri(triple.getSubject().getURI()), getLastElem(triple.getSubject().getURI())));
////                }
//                i++;
//            } else {
//                cleanTriples.add(triple);
//            }
//        }

//        int iProperties = 1;
//        //create properties
//        for (Triple triple : cleanTriples) {
//
//            String id = "objectProperty" + iProperties;
//
//            if (triple.getPredicate().getURI().equals("http://www.essi.upc.edu/~snadal/BDIOntology/Global/hasFeature")) {
//                properties.add(new Property(id, triple.getPredicate().getURI()));
//                proA.add(new PropertyAttribute(id, getBaseIri(getBaseIri(triple.getPredicate().getURI())) + id,
//                        getBaseIri(triple.getPredicate().getURI()), "hasFeature"
//                        , nodesId.get(triple.getSubject().getURI()), nodesId.get(triple.getObject().getURI())));
//            } else {
//                properties.add(new Property(id, "G:hasRelation"));
//                proA.add(new PropertyAttribute(id, triple.getPredicate().getURI(),
//                        getBaseIri(triple.getPredicate().getURI()), getLastElem(triple.getPredicate().getURI())
//                        , nodesId.get(triple.getSubject().getURI()), nodesId.get(triple.getObject().getURI())));
//            }
//            iProperties++;
//        }

        JsonWebVowl json = new JsonWebVowl(generateHeader(), new ArrayList<>(), nodes, classA, properties, proA);

        return new Gson().toJson(json).replace("classes", "class");
    }

    public String createNode(Subject s, int nodeId) {

        String id = "Class" + nodeId;
        nodes.add(new Nodes(id, typeWithPrefix(s.getType()) ));
        classA.add(new ClassAttribute(id, s.getIri(), getBaseIri(s.getIri()), getLastElem(s.getIri())));
        return id;

    }

    public String createLiteralNode(Subject s, int nodeId){
        String id = "Class" + nodeId;
        nodes.add(new Nodes(id, "rdfs:Datatype") );
        classA.add(new ClassAttribute(id, s.getRange(),getBaseIri(s.getRange()), getLastElem(s.getRange()) ) );
        return id;
    }

    public String typeWithPrefix(String type){

        if (type.contains(Namespaces.G.val() )){
            return "G:" + type.replace(Namespaces.G.val(), "");
        } else if (type.contains(RDF.getURI() )){
            return "RDF:" + type.replace(RDF.getURI(), "");
        } else if (type.contains(RDFS.getURI() )){
            return "RDFS:" + type.replace(RDFS.getURI(), "");
        } else if (type.contains(OWL.getURI())){
            return "OWL:"+type.replace(OWL.getURI(), "");
        } else if( type.contains(Namespaces.S.val()) ){
            return "S:"+ type.replace(Namespaces.S.val() , "");
        }
        return type;

    }

    public String getLastElem(String iri) {
        String regex = "/";
        if(iri.contains("#")){
            regex = "#";
        }
        String[] bits = iri.split(regex);
        String label = bits[bits.length - 1]; // it could throw an exception when split empty....CHECK!

        if(label.contains(".")){
            String[] bits2 = label.split("\\.");
            label = bits2[bits2.length - 1];
        }

        return label;
    }

    public String getBaseIri(String iri) {
        String regex = "/";
        if(iri.contains("#")){
            regex = "#";
        }
        String[] elem = iri.split(regex);
        String baseIri = "";
        for (int i = 0; i < elem.length - 1; i++) {
            baseIri += elem[i] + "/";
        }
        return baseIri;
    }




}
