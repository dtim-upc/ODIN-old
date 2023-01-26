package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.Graph;
import edu.upc.essi.dtim.odin.bootstrapping.pojos.DataSource;
import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.projects.Project;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import edu.upc.essi.dtim.odin.storage.graph.GraphStore;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSourceRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JenaConnection graph;

//    @Autowired
//    private GraphOperations gp;

//        common methods: save, findOne, findAll, count, delete, exists

    public Boolean existsNotEmpty(DataSource ds) {
//        TDB.sync(graph.temporal() ) ;
        Graph g = graph.temporal().getGraph(ds.getIri());
        if( g.size()>1)
            return true;
        return false;

    }

    public Model getG(DataSource ds){
        return graph.persistent().getGraph(ds.getIri());
    }
    public Model getTempG(DataSource ds){
        return graph.temporal().getGraph(ds.getIri());
    }

    public Boolean exists(DataSource ds) {
        boolean g = graph.temporal().containsGraph(ds.getIri());
        if( g)
            return true;
        return false;

    }

    public DataSource save(DataSource dataSource, String oldPath, Project p ){
        // we just move the graph into the persistent and update the path triple with the persistent file path

        Graph g = graph.temporal().getGraph(dataSource.getIri());
        graph.persistent().addModel(dataSource.getIri(), g);
//        graph.persistent().getGraph(dataSource.getIri()).write("/Users/javierflores/Documents/upc/projects/newODIN/api/jena2/newprueba2.ttl");

        graph.persistent().updateLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_PATH.val(), oldPath, dataSource.getPath()  );
        //update path if there is integration in temporal...
//        this should be moved into a service class...
        if(graph.temporal().containsGraph(p.getGlobalSchemaIRI())){
            graph.temporal().updateLiteral(p.getGlobalSchemaIRI(),dataSource.getIri(), DataSourceGraph.HAS_PATH.val(), oldPath, dataSource.getPath()   );
//            graph.temporal().getGraph(p.getGlobalSchemaIRI()).write("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas"+dataSource.getName()+".ttl", Lang.TTL);
        }
//        TODO: should be a better way to update paths of minimal. Actually, minimal should not contain :has_path...Fixing this requires modifying query or updating minimal
        if(graph.persistent().containsGraph(p.getGlobalSchemaIRI())) {
            graph.persistent().updateLiteral(p.getGlobalSchemaIRI(),dataSource.getIri(), DataSourceGraph.HAS_PATH.val(), oldPath, dataSource.getPath()   );
        }


        deleteTemporal(dataSource);
//            graph.temporal().deleteGraph(dataSource.getIri());
//        graph.persistent().getGraph(dataSource.getIri()).write("/Users/javierflores/Documents/upc/projects/newODIN/api/jena2/newprueba2.ttl");
        return dataSource;
    }

    public DataSource saveTemporal(DataSource dataSource, Model m ){
        // model includes name, format, id, path, wrapper

            graph.temporal().addModel(dataSource.getIri(), m);
            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_DESCRIPTION.val(), dataSource.getDescription());
            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_FILESIZE.val(), dataSource.getFilesize());
            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.GRAPHICAL.val(), dataSource.getGraphicalSchema());
            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_FILENAME.val(), dataSource.getFilename());

            graph.temporal().addTripleLiteral(dataSource.getIri(), dataSource.getIri(), DataSourceGraph.HAS_PROJECTID.val(), dataSource.getProjectID());

            graph.temporal().getGraph(dataSource.getIri()).write("/Users/javierflores/Documents/upc/projects/NextiaQR/src/test/resources/qr_rdfs/caseJoin/"+dataSource.getName()+".ttl", Lang.TTL);

//            gp.addModel(dataSource.getIri(), m);



        return dataSource;
    }

    public void delete(DataSource dataSource) {
//        TODO: we need to delete triples from integration too. IMPORTANT!!!!
        graph.persistent().deleteGraph(dataSource.getIri());

    }


    public Boolean deleteTemporal(DataSource dataSource){
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


    public List<DataSource> findAllTemporal(String projectID){
        return findAllBase(graph.temporal(), projectID);
    }

    public List<DataSource> findAll(String projectID){
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

    private List<DataSource> findAllBase(GraphStore graph, String projectID){
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
        List<DataSource> datasources = new ArrayList<>();
        while(res.hasNext()) {
            QuerySolution r = res.next();
            String graphIRI = r.get("graph").toString();
            if(!graphIRI.contains(Namespaces.GLOBALSCHEMA.val()) && !graphIRI.contains(Namespaces.SCHEMAINTEGRATION.val()) ) {
                DataSource ds = new DataSource();
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
    public List<DataSource> findAll(){

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
        List<DataSource> datasources = new ArrayList<>();
        while(res.hasNext()) {
            QuerySolution r = res.next();
            DataSource ds = new DataSource();
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



    public DataSource findByID(String id){
        return findDSById(id, graph.persistent());
    }

    public DataSource findByIDTemp(String id){
        return findDSById(id, graph.temporal());
    }


    private DataSource findDSById(String id, GraphStore g){

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
            DataSource ds =  new DataSource();
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
