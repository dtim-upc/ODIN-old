
/**
 * Created by snadal on 21/12/16.
 */

const odinApi = "http://localhost:8082/odin"

const Namespaces = {
    S: "http://www.essi.upc.edu/~snadal/BDIOntology/Source/",
    G: "http://www.essi.upc.edu/~snadal/BDIOntology/Global/",
  I: "http://www.essi.upc.edu/dtim/ontology/Global/",
    owl: "http://www.w3.org/2002/07/owl#",
    RDF:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",
    RDFS:"http://www.w3.org/2000/01/rdf-schema#",
    dct: "http://purl.org/dc/terms/",
    dcat: "http://www.w3.org/ns/dcat#",
    sc: "http://schema.org/"
};

const Wrapper = {

  Attribute: {

    iri: Namespaces.S+"Attribute",
    name: "S:Attribute",
    gui_name: "S:Attribute",
    color: "#33CCCC",
    isID: false
  },

  DataSource: {

    iri: Namespaces.S+"DataSource",
    name: "S:DataSource",
    gui_name: "S:DataSource",
    color: "#4256d7",
    isID: false
  },

  Wrapper: {

    iri: Namespaces.S+"Wrapper",
    name: "S:Wrapper",
    gui_name: "S:Wrapper",
    color: "#1a92b7",
    isID: false
  },

  hasWrapper:{

    iri: Namespaces.S+"hasWrapper",
    name: "S:hasWrapper",
    gui_name: "S:hasWrapper",
    color: "#33CCCC",
    isID: false
  },
  hasAttribute:{

    iri: Namespaces.S+"hasAttribute",
    name: "S:hasAttribute",
    gui_name: "S:hasAttribute",
    color: "#33CCCC",
    isID: false
  }

}


const SourceGraph_JSON = {
  ContainerMembershipProperty: {
    iri: Namespaces.RDFS+"ContainerMembershipProperty",
    name: "RDFS:ContainerMembershipProperty",
    gui_name: "RDFS:ContainerMembershipProperty",
    color: "#33CCCC",
    isID: false
  },
  Seq: {
    iri: Namespaces.RDF+"Seq",
    name: "RDF:Seq",
    gui_name: "RDF:Seq",
    color: "#33CCCC",
    isID: false
  },
  Property:{
      iri: Namespaces.RDF+"Property",
      name: "RDF:Property",
      gui_name: "RDF:Property",
      color: "#33CCCC",
      isID: false
  },
  RDF_Class:{
      iri: Namespaces.RDFS+"Class",
      name: "RDFS:Class",
      gui_name: "RDFS:Class",
      color: "#33CCCC",
      isID: false
  }

}

const Integration = {

  IntegrationClass:{
    iri: Namespaces.I+"IntegrationClass",
    name: Namespaces.I+"IntegrationClass",
    gui_name: "IntegrationClass",
    color: "#dfffd6",
    isID: false
  },
  IntegrationDProperty:{
    iri: Namespaces.I+"IntegrationDProperty",
    name: Namespaces.I+"IntegrationDProperty",
    gui_name: "IntegrationDProperty",
    color: "#dfffd6",
    isID: false
  },
  IntegrationOProperty:{
    iri: Namespaces.I+"IntegrationOProperty",
    name: "IntegrationOProperty",
    gui_name: "IntegrationOProperty",
    color: "#dfffd6",
    isID: false
  }


}

const Global = {
    CONCEPT: {
        iri: Namespaces.G+"Concept",
        name: "G:Concept",
        gui_name: "G:Concept",
        color: "#33CCCC",
        isID: false
    },
    /*MEMBERCONCEPT: {
        iri: Namespaces.G+"MemberConcept",
        name: "MemberConcept",
        color: "#1dcc72",
        isID: false
    },*/
    HAS_RELATION: {
        iri: Namespaces.G+"hasRelation",
        name: "G:hasRelation",
        gui_name: "G:hasRelation",
        color: "#33CCCC",
        isID: false
    },
    FEATURE: {
        iri: Namespaces.G+"Feature",
        name: "G:Feature",
        gui_name: "G:Feature",
        color: "#D7DF01",
        isID: false
    },
    FEATURE_ID: {
        iri: Namespaces.G+"Feature",
        name: "G:Feature_ID",
        gui_name: "G:Feature (ID subclass)",
        color: "#FF6600",
        isID: true
    },
    HAS_FEATURE: {
        iri: Namespaces.G+"hasFeature",
        name: "G:hasFeature",
        gui_name: "G:hasFeature",
        color: "#D7DF01",
        isID: false
    },
    PART_OF: {
        iri: Namespaces.G+"partOf",
        name: "G:partOf",
        gui_name: "G:partOf",
        color: "#D7DF01",
        isID: false
    }/*,
    AGGREGATIONFUNCTION: {
        iri: Namespaces.G+"AggregationFunction",
        name: "AggregationFunction",
        color: "#aa4adf",
        isID: false
    },
    HAS_AGGREGATIONFUNCTION: {
        iri: Namespaces.G+"hasAggregationFunction",
        name: "hasAggregationFunction",
        color: "#aa4adf",
        isID: false
    }*/
    /*
    HAS_DATATYPE: {
        iri: Namespaces.G+"hasDatatype",
        name: "hasDatatype",
        color: "#FF6600"
    }
    */
};




const Source = {
    EVENT: {
        iri: Namespaces.S+"Event",
        name: "Event",
        color: "#FF3300"
    },
    SCHEMA_VERSION: {
        iri: Namespaces.S+"SchemaVersion",
        name: "SchemaVersion",
        color: "#FECB98"
    },
    ATTRIBUTE: {
        iri: Namespaces.RDFS+"Attribute",
        name: "Attribute",
        color: "#00CCFF"
    }
}


function getGlobalEdge(namespaceOrigin, namespaceDest) {
    if (namespaceOrigin == Global.CONCEPT.iri && namespaceDest == Global.FEATURE.iri) return Global.HAS_FEATURE.iri;
    if (namespaceOrigin == Global.FEATURE.iri && namespaceDest == Global.INTEGRITY_CONSTRAINT.iri) return Global.HAS_INTEGRITY_CONSTRAINT.iri;
    if (namespaceOrigin == Global.FEATURE.iri && namespaceDest == Global.DATATYPE.iri) return Global.HAS_DATATYPE.iri;
    if (namespaceOrigin == Global.CONCEPT.iri && namespaceDest == Global.CONCEPT.iri) return Global.HAS_RELATION.iri;
    /*    if (namespaceOrigin == Global.MEMBERCONCEPT.iri && namespaceDest == Global.MEMBERCONCEPT.iri) return Global.PART_OF.iri;
        if (namespaceOrigin == Global.MEMBERCONCEPT.iri && namespaceDest == Global.CONCEPT.iri) return Global.HAS_RELATION.iri;
        if (namespaceOrigin == Global.MEMBERCONCEPT.iri && namespaceDest == Global.FEATURE_ID.iri) return Global.HAS_FEATURE.iri;
        if (namespaceOrigin == Global.MEMBERCONCEPT.iri && namespaceDest == Global.FEATURE.iri) return Global.HAS_FEATURE.iri;*/
    return null;
}
