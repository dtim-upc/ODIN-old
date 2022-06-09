package edu.upc.essi.dtim.odin.utils.jena.parsers.models;

import lombok.Getter;
import lombok.Setter;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.XSD;

@Getter @Setter
public class Subject {

    String iri;
    String type;
    String domain;
    String range;

    public String getPropertyType() {

        if(range != null) {
            if(range.contains(XSD.getURI()) ){
                return OWL.DatatypeProperty.getURI();
            }
            return OWL.ObjectProperty.getURI();
        }
        return null;

    }


}
