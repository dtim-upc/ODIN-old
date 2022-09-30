package edu.upc.essi.dtim.odin.utils.jena.parsers.graphy;

import edu.upc.essi.dtim.nextiadi.config.Vocabulary;
import lombok.Data;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;


@Data
public class Nodes {

    String id;
    String iri;
    String iriType;
    String shortType;
    String type;
    String label;
    String domain;
    String range;

    // optional
    String linkId;

//    public Nodes(){
//        this.domain = "";
//        this.range = "";
//    }

    public void setXSDDatatype(){
        this.type = "xsdType";
        this.shortType = "xsd:String";
    }

    public void computeShortType(){
//        this if it's because rdfs:subclass does not have a type...
        if(iriType != null) {
            if(iriType.contains(RDFS.getURI())){
                this.shortType = "rdfs:"+ iriType.replace(RDFS.getURI(),"");
            } else if (iriType.contains(RDF.getURI())) {
                this.shortType = "rdf:"+ iriType.replace(RDF.getURI(),"");
            } else if (iriType.contains("http://www.essi.upc.edu/DTIM/NextiaDI/")) {
                this.shortType = "nextia:" + iriType.replace("http://www.essi.upc.edu/DTIM/NextiaDI/","");
            } {
                this.shortType = iriType;
            }
        }

//        this.shortType = ""
    }

    public void computeType() {

        // think better way to do this

//        if(iriType.equals(RDFS.Class.getURI())){
//            type = "class";
//        }
        if(range != null)     { // everything that contains range is property
            if( range.contains(XSD.getURI())) {
                type = "datatypeProperty";
//            } else if ( iriType.equals(RDF.Property.getURI())  ) {
//                type = "objectProperty";
            } else {
                type = "objectProperty"; // missing subclassof and subpropertyof

            }
        } else {
            if(iriType != null) {
                if (iriType.equals(Vocabulary.IntegrationClass.val())) {
                    type = "integratedClass";
                } else if (iriType.equals(Vocabulary.IntegrationDProperty.val())) {
                    type = "integratedDatatypeProperty";
                } else if (iriType.equals(Vocabulary.IntegrationOProperty.val())) {
                    type = "integratedObjectProperty";
                } else {
                    type = "class";
                }
            } else {
                type = "class";
            }

        }
        computeShortType();


    }

}
