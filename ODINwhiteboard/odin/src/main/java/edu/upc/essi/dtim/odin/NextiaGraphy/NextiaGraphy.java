package edu.upc.essi.dtim.odin.NextiaGraphy;

import com.google.gson.Gson;
import edu.upc.essi.dtim.odin.NextiaGraphy.graphy.Graphy;
import edu.upc.essi.dtim.odin.NextiaGraphy.graphy.Links;
import edu.upc.essi.dtim.odin.NextiaGraphy.graphy.Nodes;
import edu.upc.essi.dtim.odin.NextiaGraphy.vocabulary.SourceGraph;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static edu.upc.essi.dtim.odin.NextiaGraphy.vocabulary.Namespaces.GLOBALSCHEMA;
import static edu.upc.essi.dtim.odin.NextiaGraphy.vocabulary.Namespaces.SCHEMAINTEGRATION;

public class NextiaGraphy {

    public String generateVisualGraphNew(Model model){

        HashMap<String, String> nodesId = new HashMap<String, String>();
        HashMap<String, String> propertiesId= new HashMap<String, String>();

        //Graph graph = new Graph();
        //graph.setModel(model);

        List<Nodes> nodes = new ArrayList<>();

        int conterMember = 1;
        // create nodes
        int nodeId = 1;
        for( Resource r : model.listSubjects().toList() ) {

            Nodes n = new Nodes();
            if(r.getURI().contains("pop")) {
                System.out.println("here");
            }
            n.setIri(r.getURI());
            n.setId("Class"+nodeId);
            nodeId += 1 ;

            nodesId.put(n.getIri(), n.getId());

            Nodes memberProperty = new Nodes();
            for (Statement statement : r.listProperties().toList()) {

                if (statement.getPredicate().equals(RDF.type)) {
                    n.setIriType(statement.getObject().toString());
//                    n.setType(statement.getObject().toString());
                } else if (statement.getPredicate().equals(RDFS.domain)) {
                    n.setDomain(statement.getObject().toString());
                } else if (statement.getPredicate().equals(RDFS.range)) {
                    n.setRange(statement.getObject().toString());
                } else if (statement.getPredicate().getURI().equals(SourceGraph.HAS_ATTRIBUTE.val()) || statement.getPredicate().getURI().equals(SourceGraph.HAS_WRAPPER.val())) {

                Nodes property = new Nodes();
                property.setIri(statement.getPredicate().getURI());
                property.setType(statement.getPredicate().getURI());
                property.setDomain(n.getIri());
                property.setRange(statement.getObject().toString());
                    property.setLabel(getLastElem(property.getIri()));
                    property.setId("Property"+nodeId);
                    nodesId.put(property.getIri(), property.getId());
                    nodeId += 1 ;
                    property.computeType();
                    nodes.add(property);
            } else if(statement.getPredicate().equals(RDFS.subClassOf)  || statement.getPredicate().equals(RDFS.subPropertyOf)

            ) {

                Nodes property = new Nodes();
                property.setIri(statement.getPredicate().getURI());
                property.setType(statement.getPredicate().getURI());
                property.setDomain(statement.getSubject().toString());
                property.setRange(statement.getObject().toString());
                    property.setLabel(getLastElem(property.getIri()));
                    property.setId("Property"+nodeId);
                    nodesId.put(property.getIri(), property.getId());
                    nodeId += 1 ;
                    property.computeType();
                    nodes.add(property);
            } else if(statement.getPredicate().equals(RDFS.member)){


                    memberProperty.setIri(statement.getPredicate().getURI()+"."+conterMember);
                    memberProperty.setLabel("rdfs:member");
                    memberProperty.setIriType(statement.getPredicate().getURI());
                    memberProperty.setShortType("rdfs:member");
                    memberProperty.setType("object");
                    //todo: not sure if id should be class or property
                    memberProperty.setId("Class"+nodeId);
                    nodeId += 1 ;
                    nodesId.put(memberProperty.getIri(), memberProperty.getId());
                    memberProperty.setDomain(statement.getSubject().toString());
                    memberProperty.setRange(statement.getObject().toString());

                    conterMember = conterMember+1;

                    // do nothing. Probably statement not useful for graphical graph
//                    LOGGER.debug("NOT USEFUL: "+ statement.getSubject() +","+statement.getPredicate()+", "+statement.getObject() );
                } else if( statement.getPredicate().equals(RDFS.label) ){
                    n.setLabel( statement.getObject().toString() );
                }
            }
            if(n.getLabel() == null)
                n.setLabel(getLastElem(n.getIri()));
            n.computeType();
            if(memberProperty.getIriType() != null) {

                if( n.getIriType() != null) {
                    if (n.getIriType().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#Property")) {

                        n.setRange(memberProperty.getRange());
                        n.setType("object");

                    } else {
                        nodes.add(memberProperty);
                    }
                } else {

//                    nodes.add(memberProperty);

                }
            }

            // for J:document
            if(n.getIriType() != null)
                nodes.add(n);
//            if(n.getType() == null) {
//                LOGGER.info("SOMETHING IS WRONG IN THE MODEL. No type definition for resource "+n.getIri());
//            } else {
//
//            }

        }

        List<String> excluded = new ArrayList<>();
        excluded.add("http://www.essi.upc.edu/DTIM/NextiaDI/DataSource");
        excluded.add(SCHEMAINTEGRATION.val());
        excluded.add(GLOBALSCHEMA.val());

        List<String> excludedForProperties = new ArrayList<>();
        excludedForProperties.add("class");
        excludedForProperties.add("integratedClass");


        System.out.println(nodes.size());
        List<Nodes> nodesReady = nodes.stream().filter( s -> !excluded.contains(s.getIriType()) )
                .filter( s -> !s.getIri().contains(GLOBALSCHEMA.val()) && !s.getIri().contains(SCHEMAINTEGRATION.val())
                        && !s.getIri().contains(RDFS.subPropertyOf.toString()) && !s.getRange().equals("http://schema.org/identifier") )
                .collect(Collectors.toList());
//        List<Nodes> nodesProperties = nodes.stream().filter(n -> !n.getType().equals("class")).collect(Collectors.toList());
        List<Nodes> nodesProperties = nodes.stream()
                .filter(n -> !excludedForProperties.contains(n.getType()) && !n.getIri().contains(RDFS.subPropertyOf.toString())  ).collect(Collectors.toList());

        List<Links> links = new ArrayList<>();


        int linkid =1;
        for (Nodes n: nodesProperties) {

            String id = "Link"+linkid;
            linkid +=1;

            Links l = new Links();
            l.setId(id);
            l.setNodeId(n.getId());
            n.setLinkId(id);
            l.setSource(nodesId.get(n.getDomain()));

            if(n.getRange().contains(XSD.getURI() )) {
                // create node for datatype,
                Nodes datatype = new Nodes();
                datatype.setIri(n.getRange());
                String id2 = "Datatype"+nodeId;
                datatype.setId(id2);
                datatype.setLabel(getLastElem(datatype.getIri()));
                datatype.setXSDDatatype();
                nodeId +=1;

                l.setTarget(id2);
                nodesReady.add(datatype);
                nodes.add(datatype);
            } else {
                l.setTarget(nodesId.get(n.getRange()));
            }
            l.setLabel(n.getLabel());
            if( (l.getSource() == null || l.getTarget() == null  ) && !n.getRange().equals("http://schema.org/identifier") ){

                System.out.println("ERROR......");
                System.out.println(l.getLabel()+"-------------------------------------");
            }
            // TODO: support to see subproperty of....it implies to connect to properties visually
            if(!n.getRange().equals("http://schema.org/identifier"))
                links.add(l);
        }

//        System.out.println("NODES:");
//        nodesReady.forEach(System.out::println);
//        links.forEach(System.out::println);
//        System.out.println("---");
//        nodes.forEach(System.out::println);


        Graphy gr = new Graphy();
        gr.setNodes(nodesReady);
        gr.setLinks(links);

        return new Gson().toJson(gr);
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




}
