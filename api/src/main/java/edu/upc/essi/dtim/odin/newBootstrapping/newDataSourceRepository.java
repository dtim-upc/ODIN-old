package edu.upc.essi.dtim.odin.newBootstrapping;

import edu.upc.essi.dtim.Graph;
import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import edu.upc.essi.dtim.odin.storage.graph.GraphStore;
import edu.upc.essi.dtim.odin.utils.jena.GraphOperations;
import edu.upc.essi.dtim.odin.utils.jena.parsers.graphy.Nodes;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class newDataSourceRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JenaConnection graph;

    @Autowired
    private GraphOperations gp;

//        common methods: save, findOne, findAll, count, delete, exists

    public Boolean existsNotEmpty(newDataSource ds) {
//        TDB.sync(graph.temporal() ) ;
        Graph g = graph.temporal().getGraph(ds.getIri());
        if( g.size()>1)
            return true;
        return false;

    }

    public Boolean exists(newDataSource ds) {
        boolean g = graph.temporal().containsGraph(ds.getIri());
        if( g)
            return true;
        return false;

    }

    public newDataSource save(newDataSource dataSource, String oldPath ){
        // we just move the graph into the persistent and update the path triple with the persistent file path
        Graph g = graph.temporal().getGraph(dataSource.getIri());
        graph.persistent().addModel(dataSource.getIri(), g);
//        graph.persistent().getGraph(dataSource.getIri()).write("/Users/javierflores/Documents/upc/projects/newODIN/api/jena2/newprueba2.ttl");

        graph.persistent().updateLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_PATH.val(), oldPath, dataSource.getPath()  );
        deleteTemporal(dataSource);
//            graph.temporal().deleteGraph(dataSource.getIri());
//        graph.persistent().getGraph(dataSource.getIri()).write("/Users/javierflores/Documents/upc/projects/newODIN/api/jena2/newprueba2.ttl");
        return dataSource;
    }

    public newDataSource saveTemporal(newDataSource dataSource, Model m ){
        // model includes name, format, id, path, wrapper

            graph.temporal().addModel(dataSource.getIri(), m);
            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_DESCRIPTION.val(), dataSource.getDescription());
            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_FILESIZE.val(), dataSource.getFilesize());
            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.GRAPHICAL.val(), dataSource.getGraphicalSchema());
            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_FILENAME.val(), dataSource.getFilename());

            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_PROJECTID.val(), dataSource.getProjectID());

            gp.addModel(dataSource.getIri(), m);



        return dataSource;
    }

    public void delete(newDataSource dataSource) {
//        TODO: we need to delete triples from integration too. IMPORTANT!!!!
        graph.persistent().deleteGraph(dataSource.getIri());

    }


    public Boolean deleteTemporal(newDataSource dataSource){
        Boolean flag = exists(dataSource);
        if(!flag){
            System.out.println("something is wrong. Datasource should exist but it's not in jena");
        }
        graph.temporal().deleteGraph(dataSource.getIri());

        flag = exists(dataSource);
        if(!flag){
            System.out.println("data source deleted");
        }
        return !flag;

    }


    public List<newDataSource> findAllTemporal(String projectID){
        return findAllBase(graph.temporal(), projectID);
    }

    public List<newDataSource> findAll(String projectID){
        return findAllBase(graph.persistent(), projectID);
//        String query = commonPrefixes() + "SELECT * WHERE {" +
//                "?datasource rdf:type <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource>; " +
//                "rdfs:label ?name; " +
//                "nextiaDS:format ?format; " +
//                "nextiaDS:graphicalGraph ?graphicalGraph; " +
//                "nextiaDS:id ?id; " +
//                "nextiaDS:path ?path; " +
//                "<"+DataSourceGraph.HAS_PROJECTID.val()+"> '"+projectID+"'; " +
//                "nextiaDS:hasFileName ?fileName. " +
//                "OPTIONAL { ?datasource nextiaDS:graphicalGraph ?graphicalGraph.  }" +
//                "}";
//
//        ResultSet res = graph.persistent().runAQuery(query);
//        List<newDataSource> datasources = new ArrayList<>();
//        while(res.hasNext()) {
//            QuerySolution r = res.next();
//            newDataSource ds = new newDataSource();
//            ds.setName(r.get("name").toString());
//            ds.setType(getDataSourcetype(r.get("format").toString()));
//            ds.getSchema().setGraphicalSchemaStr(StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
////            ds.setGraphicalGraph( StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
//            ds.setId(r.get("id").toString());
//            ds.setProjectID(projectID);
//            ds.setPath(r.get("path").toString());
//            ds.setFilename(r.get("fileName").toString());
//            datasources.add(ds);
//        }
//        return datasources;

    }

    private List<newDataSource> findAllBase(GraphStore graph, String projectID){
        String query = commonPrefixes() + "SELECT * WHERE { " +
                " GRAPH ?graph { " +
                "?datasource rdf:type <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource>; " +
                "rdfs:label ?name; " +
                "nextiaDS:format ?format; " +
                "nextiaDS:graphicalGraph ?graphicalGraph; " +
                "nextiaDS:id ?id; " +
                "nextiaDS:path ?path; " +
                "<"+DataSourceGraph.HAS_PROJECTID.val()+"> '"+projectID+"'; " +
                "nextiaDS:hasFileName ?fileName. " +
                "OPTIONAL { ?datasource nextiaDS:graphicalGraph ?graphicalGraph.  } " +
                " } " +
//                " FILTER( !contains(str(?graph), '"+ Namespaces.GlobalSchema.val()+"') || !contains(str(?graph), '"+ Namespaces.SchemaIntegration.val()+"') ) " +
//                " FILTER NOT EXISTS {?project <"+DataSourceGraph.INTEGRATION_OF.val()+"> ?datasource} " +
                "}";

        ResultSet res = graph.runAQuery(query);
        List<newDataSource> datasources = new ArrayList<>();
        while(res.hasNext()) {
            QuerySolution r = res.next();
            String graphIRI = r.get("graph").toString();
            if(!graphIRI.contains(Namespaces.GlobalSchema.val()) && !graphIRI.contains(Namespaces.SchemaIntegration.val()) ) {
                newDataSource ds = new newDataSource();
                ds.setName(r.get("name").toString());
                ds.setType(getDataSourcetype(r.get("format").toString()));
                ds.setGraphicalSchema(StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
//            ds.setGraphicalGraph( StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
                ds.setId(r.get("id").toString());
                ds.setProjectID(projectID);
                ds.setPath(r.get("path").toString());
                ds.setFilename(r.get("fileName").toString());
                datasources.add(ds);
            } else {
                LOGGER.debug("excluded integrated temporal graphs");
            }

        }
        return datasources;
    }


    // TODO: adapt this for public datasources
    public List<newDataSource> findAll(){

        String query = commonPrefixes() + "SELECT * WHERE {" +
                "?datasource rdf:type <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource>; " +
                "rdfs:label ?name; " +
                "nextiaDS:format ?format; " +
                "nextiaDS:graphicalGraph ?graphicalGraph; " +
                "nextiaDS:id ?id; " +
                "nextiaDS:path ?path; " +
//                "nextiaDS:wrapper ?wrapper; " +
                "nextiaDS:hasFileName ?fileName. " +
                "OPTIONAL { ?datasource nextiaDS:graphicalGraph ?graphicalGraph.  }" +
                "}";

        ResultSet res = graph.persistent().runAQuery(query);
        List<newDataSource> datasources = new ArrayList<>();
        while(res.hasNext()) {
            QuerySolution r = res.next();
            newDataSource ds = new newDataSource();
            ds.setName(r.get("name").toString());
            ds.setType(getDataSourcetype(r.get("format").toString()));
            ds.setGraphicalSchema(StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
//            ds.setGraphicalGraph( StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
            ds.setId(r.get("id").toString());
            ds.setPath(r.get("path").toString());
            ds.setFilename(r.get("fileName").toString());
            datasources.add(ds);
        }
        return datasources;

    }



    public newDataSource findByID(String id){
        return findDSById(id, graph.persistent());
    }

    public newDataSource findByIDTemp(String id){
        return findDSById(id, graph.temporal());
    }


    private newDataSource findDSById(String id, GraphStore g){

        String query = commonPrefixes() + "SELECT * WHERE {" +
                "?datasource rdf:type <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource>; " +
                "rdfs:label ?name; " +
                "nextiaDS:format ?format; " +
                "nextiaDS:graphicalGraph ?graphicalGraph; " +
                "nextiaDS:id \""+id+"\"; " +
                "nextiaDS:path ?path; " +
//                "nextiaDS:wrapper ?wrapper; " +
                "nextiaDS:hasFileName ?fileName. " +
                "OPTIONAL { ?datasource nextiaDS:graphicalGraph ?graphicalGraph.  }" +
                "}";

        ResultSet res = g.runAQuery(query);


        if(res.hasNext()){
            newDataSource ds =  new newDataSource();
            QuerySolution r = res.next();
            ds.setName(r.get("name").toString());
            ds.setType(getDataSourcetype(r.get("format").toString()));
            ds.setGraphicalSchema(StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
//            ds.setGraphicalGraph( StringEscapeUtils.unescapeJava(r.get("graphicalGraph").toString()));
            ds.setId(id);
            ds.setPath(r.get("path").toString());
            ds.setFilename(r.get("fileName").toString());
            return ds;
        }
        return null;
    }


//    private

    private String commonPrefixes(){

        return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX nextiaDS: <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource/> ";

    }

    // I think this method can be in pojo
    private DataSourceTypes getDataSourcetype(String format){

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
