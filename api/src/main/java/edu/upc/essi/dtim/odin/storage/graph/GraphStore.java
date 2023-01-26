package edu.upc.essi.dtim.odin.storage.graph;

import edu.upc.essi.dtim.Graph;
import edu.upc.essi.dtim.TripleStore;
import edu.upc.essi.dtim.nextiadi.config.DataSourceVocabulary;
import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.Transactional;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.util.List;
import java.util.StringJoiner;

public class GraphStore extends TripleStore {

    public static GraphStore createGraphStore(String dir) {
        return new GraphStore( TDBFactory.createDataset(dir).asDatasetGraph()) ;
    }

    protected GraphStore(DatasetGraph dsg) {
        super(dsg);
    }

    protected GraphStore(DatasetGraph dsg, Transactional transactional) {
        super(dsg, transactional);
    }

    public GraphStore(Graph model) {
        super(model);
    }

    public List<Pair<String,String>> getSourceAtts(String uri){

        List<Pair<String,String>> sourceAttributes = Lists.newArrayList();

        String query = "SELECT ?subject ?alias WHERE { GRAPH <"+uri+"> { ?subject <"+ DataSourceVocabulary.ALIAS.val() +"> ?alias. } }";
        ResultSet res =  runAQuery(query);
        while(res.hasNext()){
            QuerySolution solution = res.nextSolution();
            String s = solution.getResource("subject").getURI();
            String alias = solution.getLiteral("alias").getString();

            sourceAttributes.add( Pair.of(s, alias)  );
        }
        return sourceAttributes;
    }

    public Graph getIntegratedResourcesOfSource(String id, String uri){
        // ?integrated can be values
        String q = "PREFIX rdfs: <"+ RDFS.getURI()+">" +
                "CONSTRUCT {" +
                " ?sourceR <"+ OWL.sameAs.getURI()+"> ?integrated. " +
//                " ?integrated ?p ?o." +
                "} WHERE { " +
                "VALUES ?dr { rdfs:subPropertyOf rdfs:subClassOf  }" +
                " ?sourceR ?dr ?integrated. " +
                " ?integrated ?p ?o." +
                "FILTER ( CONTAINS( STR(?sourceR), '"+id+"' )  )" +
                "} ";

        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.create(query, getGraph(uri) );
        Graph results = Graph.wrap( qexec.execConstruct() );
        return results;
    }


    public Model simplify(Model m){

        String q = "DELETE { ?s <"+RDFS.label.getURI()+"> ?label} WHERE { " +
                "?s <"+RDFS.label.getURI()+"> ?label." +
                "?s <"+DataSourceVocabulary.ALIAS.val()+"> ?alias" +
                "}";

        String q2 = "DELETE WHERE { " +
                "?s <"+ DataSourceGraph.GRAPHICAL.val()+"> ?graphical" +
                "}";

        runAnUpdateQuery(q, m);
        return m;
    }

    public Model getPropertiesSameAs( Model graph){
        // ?integrated can be values
        String q = "PREFIX rdfs: <"+ RDFS.getURI()+">" +
                "PREFIX rdf: <"+ RDF.getURI() +">" +
                "PREFIX owl: <"+OWL.getURI()+">" +
                "CONSTRUCT {" +
                " ?property owl:sameAs ?property. " +
//                " ?integrated ?p ?o." +
                "} WHERE { " +
                "?property rdf:type rdf:Property " +
                "FILTER NOT EXISTS { " +
                "?property owl:sameAs ?integrated"  +
                "} }";


        String q2 = "PREFIX rdfs: <"+ RDFS.getURI()+">" +
                "PREFIX rdf: <"+ RDF.getURI() +">" +
                "PREFIX owl: <"+OWL.getURI()+">" +
                "SELECT ?property WHERE { " +
                "?property rdf:type rdf:Property. " +
                "FILTER NOT EXISTS { " +
                "?property owl:sameAs ?int"  +
                "}"+
//                "?property rdf:type rdf:Property." +
//                "MINUS { ?property <"+OWL.sameAs.getURI()+"> ?integrated}"+
//                "?noproperty rdf:type rdf:Property." +
//                "?noproperty <"+OWL.sameAs.getURI()+"> ?integrated."+
//                "FILTER (?property != ?noproperty)" +
                "} ";
        ARQ.setTrue(Syntax.syntaxARQ);
        ResultSet x = runAQuery(q2, graph);

        ResultSet y =runAQuery("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT ?property WHERE {?property rdf:type rdf:Property FILTER NOT EXISTS{?property owl:sameAs ?int}}", graph);




        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.create(query, graph );
        Model results = qexec.execConstruct();
        return results;
    }

    public Model getSubGraph( List<String> resources, String uri){

        StringJoiner b = new StringJoiner(" ");
        resources.forEach( x -> b.add("<"+x+">"));
        String vals =  "VALUES ?val {  "+b.toString()+" }";

        String q = "CONSTRUCT { ?val ?p ?o   } WHERE {  "+vals+"" +
                " ?val ?p ?o  } ";

        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.create(query, getGraph(uri) );
        Model results = qexec.execConstruct();

//        identifiers.forEach(i -> results.add(new ResourceImpl(i), RDFS.subClassOf, new ResourceImpl(Namespaces.SCHEMA.val()+"identifier")) );

        return  results;

    }

    public String commonPrefixes(){

        return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX nextiaDS: <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource/> ";

    }
//
//    public List<DataSource> findAllDataSources(){
//
//        String query = commonPrefixes() + "SELECT * WHERE {" +
//                "?datasource rdf:type <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource>; " +
//                "rdfs:label ?name; " +
//                "nextiaDS:id ?id; " +
//                "OPTIONAL { ?datasource nextiaDS:graphicalGraph ?graphicalGraph. }" +
//                "OPTIONAL { ?datasource nextiaDS:format ?format. }" +
//                "OPTIONAL { ?datasource nextiaDS:path ?path. }" +
//                "OPTIONAL { ?datasource nextiaDS:wrapper ?wrapper. }" +
//                "OPTIONAL { ?datasource nextiaDS:hasFileName ?fileName.} " +
//                "}";
//
//        ResultSet res = runAQuery(query);
//        List<DataSource> datasources = new ArrayList<>();
//        while(res.hasNext()) {
//            QuerySolution r = res.next();
//            DataSource ds = new DataSource();
//            ds.setName(r.get("name").toString());
//            ds.setType(getDataSourcetype(r.get("format").toString()));
//            if(r.get("graphicalGraph") != null)
//            ds.setGraphicalGraph( StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
//            ds.setId(r.get("id").toString());
//            ds.setPath(r.get("path").toString());
//            ds.setFilename(r.get("fileName").toString());
////            ds.setWrappers(r.get("wrapper").toString());
//            datasources.add(ds);
//        }
//        return datasources;
//
//    }
//
//    public DataSource findDSById(String id){
//
//        String query = commonPrefixes() + "SELECT * WHERE {" +
//                "?datasource rdf:type <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource>; " +
//                "rdfs:label ?name; " +
//                "nextiaDS:graphicalGraph ?graphicalGraph; " +
//                "nextiaDS:id \""+id+"\". " +
//                "OPTIONAL { ?datasource nextiaDS:format ?format. }" +
//                "OPTIONAL { ?datasource nextiaDS:path ?path. }" +
//                "OPTIONAL { ?datasource nextiaDS:wrapper ?wrapper. }" +
//                "OPTIONAL { ?datasource nextiaDS:hasFileName ?fileName.} " +
//                "}";
//
//
//        ResultSet res = runAQuery(query);
//        DataSource ds =  new DataSource();
//        if(res.hasNext()){
//            QuerySolution r = res.next();
//            ds.setName(r.get("name").toString());
//            ds.setType(getDataSourcetype(r.get("format").toString()));
//            ds.setGraphicalGraph( StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
//            ds.setId(id);
//            ds.setPath(r.get("path").toString());
//            ds.setFilename(r.get("fileName").toString());
////            ds.setWrappers(r.get("wrapper").toString());
//        }
//        return ds;
//    }


    public DataSourceTypes getDataSourcetype(String format){

        switch (format.toLowerCase().trim()) {
            case "csv":
                return DataSourceTypes.CSV;
            case "json":
                return DataSourceTypes.JSON;
            default:
                return null;
        }
    }

}
