package edu.upc.essi.dtim.odin.controller;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.Sets;
//import edu.upc.essi.dtim.Tmp;
import edu.upc.essi.dtim.NextiaQR_RDFS;
import edu.upc.essi.dtim.nextiaqr.models.querying.RDFSResult;
import edu.upc.essi.dtim.odin.models.query.ODINQuery;
import edu.upc.essi.dtim.odin.models.rest.QueryDataSelection;
import edu.upc.essi.dtim.odin.repository.DataSourcesRepository;
import edu.upc.essi.dtim.odin.services.impl.IntegrationService;
import edu.upc.essi.dtim.odin.services.impl.OMQService;
import edu.upc.essi.dtim.odin.utils.jena.GraphOperations;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.Tuple3;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/OMQ")
public class OMQController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OMQController.class);
    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private static final String EMPTY_INPUTS = "{}";
    @Autowired
    private GraphOperations graphOperations;
    @Autowired
    private DataSourcesRepository dsRepository;
    @Autowired
    private OMQService service;

    @Autowired
    private IntegrationService Iservice;

    @PostMapping(value="fromGraphicalToSPARQL")
    public ResponseEntity<RDFSResult> POST_omq_fromGraphicalToSPARQL(@RequestBody QueryDataSelection body) {
        LOGGER.info("[POST /OMQ/fromGraphicalToSPARQL/]");
        ODINQuery constructs = Iservice.retrieveConstructs(body);

        List<String> SELECT = Lists.newArrayList();
        Set<Tuple3<String,String,String>> WHERE = Sets.newHashSet();
        SELECT.addAll(
                body.getProperties().stream().map(c -> c.getIri()).collect(Collectors.toList())
        );
        WHERE.addAll(
                body.getClasses().stream().map(c -> new Tuple3<>(c.getIri(), RDF.type.getURI(), c.getType()))
                        .collect(Collectors.toSet())
        );
        WHERE.addAll(
                body.getProperties().stream().flatMap(c -> {
                    List<Tuple3<String,String,String>> out = Lists.newArrayList();
                    out.add(new Tuple3<>(c.getIri(), RDFS.domain.getURI(), c.getDomain()));
                    out.add(new Tuple3<>(c.getIri(),RDF.type.getURI(),c.getType()));
                    return out.stream();
                }).collect(Collectors.toSet())
        );

        StringBuilder query = new StringBuilder();
        query.append("SELECT");
        for (int i = 0; i < SELECT.size(); ++i) {
            query.append(" ?v"+(i+1));
        }
        query.append(" WHERE { VALUES (");
        for (int i = 0; i < SELECT.size(); ++i) {
            query.append(" ?v"+(i+1));
        }
        query.append(") { (");
        SELECT.forEach(s -> query.append(" <"+s+">"));
        query.append(" ) } ");
        WHERE.forEach(w -> query.append(" <"+w._1()+"> <"+w._2()+"> <"+w._3()+"> . "));
        query.append("}");

//        Tmp t = new Tmp();
//        System.out.println(t.hello());
        NextiaQR_RDFS n = new NextiaQR_RDFS();
//        n.prueba();
        System.out.println("QUERY: ");
        System.out.println(query.toString());
        RDFSResult res = n.rewriteToUnionOfConjunctiveQueries(constructs.getSourceGraphs(), constructs.getMinimal(),
                constructs.getSubGraphs(), query.toString());

        /**
        System.out.println("-- Source Graphs --");
        constructs.getSourceGraphs().forEach((k,v) -> {
            System.out.println(k);
            try {
                java.nio.file.Path p = Files.createTempFile("source-graph",".g");
                graphOperations.write(p.toString(),v);
                System.out.println(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("################");
        });
        System.out.println("");

        System.out.println("-- Minimal --");
        try {
            java.nio.file.Path p = Files.createTempFile("minimal-graph",".g");
            graphOperations.write(p.toString(),constructs.getMinimal());
            System.out.println(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("");

        System.out.println("-- Subgraphs --");
        constructs.getSubGraphs().forEach((k,v) -> {
            System.out.println(k);
            try {
                java.nio.file.Path p = Files.createTempFile("subgraphs",".g");
                graphOperations.write(p.toString(),v);
                System.out.println(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("################");
        });
        System.out.println("");
         **/

        service.hello();
        return new ResponseEntity<RDFSResult>(res, HttpStatus.OK );
//        JSONObject objBody = (JSONObject) JSONValue.parse(body);
//        String select = "SELECT ";
//        String values = "VALUES (";
//        String constants = "{(";
//
//        JSONArray projectedFeatures =(JSONArray)objBody.get("projectedFeatures");
//        for (int i = 0; i < projectedFeatures.size(); ++i) {
//            select += "?v"+(i+1)+" ";
//            values += "?v"+(i+1)+" ";
//            constants += "<"+projectedFeatures.get(i)+"> ";
//        }
//
//        values = values.substring(0,values.length()-1)+")";
//        constants = constants.substring(0,constants.length()-1)+")}";
//
//        String pattern = "";
//        for (Object selectionElement : ((JSONArray)objBody.get("selection"))) {
//            JSONObject selectedElement = (JSONObject)selectionElement;
//            if (selectedElement.containsKey("source")) {
//                JSONObject source = (JSONObject)selectedElement.get("source");
//                JSONObject target = (JSONObject)selectedElement.get("target");
//                pattern += "<"+source.getAsString("iri") + "> <" + selectedElement.getAsString("iri") + "> <" +
//                        target.getAsString("iri") + "> .\n";
//            }
//        }
//        pattern = pattern.substring(0,pattern.length()-2)+"\n";
//
//        JSONObject out = new JSONObject();
//        out.put("sparql",select+"\nWHERE {\n"+values+" "+constants+"\n"+pattern+"}");
//        return new ResponseEntity<>(out.toJSONString(), HttpStatus.OK );
    }


//
//    @PostMapping(value ="fromSPARQLToRA", consumes = "application/json")
//    public ResponseEntity<String> POST_omq_fromSPARQLToRA(@RequestBody String body) throws Exception{
////        LOGGER.info("[POST /omq/fromSPARQLToRA/] body = "+body);
////
//
////        LOGGER.info("[POST /omq/fromSPARQLToRA/] body = "+body);
////        JSONObject objBody = (JSONObject) JSONValue.parse(body);
////
////        String SPARQL = objBody.getAsString("sparql");
////        List<String> listOfFeatures = (((JSONArray)JSONValue.parse(objBody.getAsString("features"))).stream().map(o ->String.valueOf(o)).collect(Collectors.toList()));
////
////        //String namedGraph = objBody.getAsString("namedGraph");
////        //QueryRewriting qr = new QueryRewriting_DAG(SPARQL.replace("\n"," "));
////
////        Dataset T = graphOperations.getDataset();
////        T.begin(ReadWrite.READ);
////        Set<ConjunctiveQuery> UCQ = null;
////        try {
////            UCQ = QueryRewriting_EdgeBased.rewriteToUnionOfConjunctiveQueries(
////                    QueryRewriting_EdgeBased.parseSPARQL(SPARQL.replace("\n", " "), T), T)._2;
////        } catch (Exception e) {
////            e.printStackTrace();
////            throw new Exception(e);
////        }
////
////
////        JSONObject out = new JSONObject();
////        out.put("ra", graphO.nn(UCQ.stream().map(cq -> cq.toString()).collect(Collectors.joining("\nU\n"))));
////
////        T.abort();
//////        T.close();
////
////
////        HashMap<String,String> wrapperIriToID = Maps.newHashMap(); //used to map wrapper IRIs to IDs
////        //Populate data here
////        JSONArray wrappers = new JSONArray();
////        UCQ.forEach(q -> {
////            q.getWrappers().forEach(op -> {
//////                String wrapperID = MongoCollections.getWrappersCollection(Utils.getMongoDBClient()).find(
//////                           new Document("iri",((Wrapper)op).getWrapper())
//////                ).first().getString("wrapperID");
////
////                String wrapperID = wrapperR.findByField(WrapperMongo.FIELD_IRI.val(),((Wrapper)op).getWrapper()).getWrapperID();
////
////                wrapperIriToID.putIfAbsent(((Wrapper)op).getWrapper(),wrapperID);
////                wrappers.add(wrapperID);
////            });
////        });
////
////        //Convert to SQL
////        StringBuilder SQL = new StringBuilder();
////        UCQ.forEach(q -> {
////            StringBuilder select = new StringBuilder("SELECT ");
////            StringBuilder from = new StringBuilder(" FROM ");
////            StringBuilder where = new StringBuilder(" WHERE ");
////            //Sort the projections as they are indicated in the interface
////            //First remove duplicates based on the features
////            List<String> seenFeatures = Lists.newArrayList();
////            List<String> withoutDuplicates = Lists.newArrayList();
////            q.getProjections().forEach(proj -> {
////                if (!seenFeatures.contains(QueryRewriting_EdgeBased.featuresPerAttribute.get(proj))) {
////                    withoutDuplicates.add(proj);
////                    seenFeatures.add(QueryRewriting_EdgeBased.featuresPerAttribute.get(proj));
////                }
////            });
////            //Now do the sorting
////            List<String> projections = Lists.newArrayList(withoutDuplicates);//Lists.newArrayList(q.getProjections());
////            projections.sort(Comparator.comparingInt(s -> listOfFeatures.indexOf(QueryRewriting_EdgeBased.featuresPerAttribute.get(s))));
////            projections.forEach(proj -> select.append("\""+graphO.nn(proj).split("/")[graphO.nn(proj).split("/").length-1]+"\""+","));
////            q.getWrappers().forEach(w -> from.append(wrapperIriToID.get(w.getWrapper())+","));
////            q.getJoinConditions().forEach(j -> where.append(
////                    "\""+graphO.nn(j.getLeft_attribute()).split("/")[graphO.nn(j.getLeft_attribute()).split("/").length-1]+"\""+
////                            " = "+
////                            "\""+graphO.nn(j.getRight_attribute()).split("/")[graphO.nn(j.getRight_attribute()).split("/").length-1]+"\""+
////                            " AND "));
////            SQL.append(select.substring(0,select.length()-1));
////            SQL.append(from.substring(0,from.length()-1));
////            if (!where.toString().equals(" WHERE ")) {
////                SQL.append(where.substring(0, where.length() - " AND ".length()));
////            }
////            SQL.append(" UNION ");
////        });
////        String SQLstr = SQL.substring(0,SQL.length()-" UNION ".length())+";";
////
////        LOGGER.info(SQLstr);
////
////        out.put("wrappers",wrappers);
////        out.put("sql",SQLstr);
////
////        return Response.ok(out.toJSONString()).build();
//
//        return new ResponseEntity<>("", HttpStatus.OK );
//    }
//
//    @PostMapping(value ="fromSQLToData", consumes = "application/json")
//    public ResponseEntity<String> POST_omq_fromSQLToData(@RequestBody String body) {
//        LOGGER.info("[POST /omq/fromSQLToData/] body = "+body);
//
//        return new ResponseEntity<>("", HttpStatus.OK );
//    }


}
