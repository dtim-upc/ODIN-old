package edu.upc.essi.dtim.ODINBackend.controller;

import edu.upc.essi.dtim.ODINBackend.models.rest.QueryDataSelection;
import edu.upc.essi.dtim.ODINBackend.repository.DataSourcesRepository;
import edu.upc.essi.dtim.ODINBackend.repository.GlobalGraphRespository;
import edu.upc.essi.dtim.ODINBackend.services.impl.GlobalGraphService;
import edu.upc.essi.dtim.ODINBackend.services.impl.LAVMappingService;
import edu.upc.essi.dtim.ODINBackend.services.impl.OMQService;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;


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



    @PostMapping(value="fromGraphicalToSPARQL")
    public ResponseEntity<String> POST_omq_fromGraphicalToSPARQL(@RequestBody QueryDataSelection body) {
        LOGGER.info("[POST /OMQ/fromGraphicalToSPARQL/]");

        service.hello();
        return new ResponseEntity<>("bye", HttpStatus.OK );
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
