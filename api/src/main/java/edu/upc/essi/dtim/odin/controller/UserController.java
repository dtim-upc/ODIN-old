package edu.upc.essi.dtim.odin.controller;

import edu.upc.essi.dtim.NextiaDI;
import edu.upc.essi.dtim.nextiadi.config.Namespaces;
import edu.upc.essi.dtim.odin.models.rest.Triple;
import edu.upc.essi.dtim.odin.models.rest.TripleStore;
import edu.upc.essi.dtim.odin.utils.jena.GraphOperations;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private GraphOperations graphOperations;

    @GetMapping("/triplestore")
    public ResponseEntity<List<Triple>> GET_graph() {
        LOGGER.info("flagGET");
        String out = "";

        List<Triple> triples = new ArrayList<>();
        try{
            ResultSet rs = graphOperations.runAQuery("SELECT * WHERE {GRAPH ?g {?s ?p ?o.}}");

            while (rs.hasNext()){

                QuerySolution r = rs.next();

                Triple t = new Triple();
                t.setG( r.get("g").toString() );
                t.setS( r.get("s").toString() );
                t.setO( r.get("o").toString() );
                t.setP( r.get("p").toString() );

                triples.add(t);
            }


            /*out = ResultSetFormatter.asText(rs);
            LOGGER.info("RESULT:");
            LOGGER.info(out);*/
            return new ResponseEntity<>(triples,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(triples, HttpStatus.CONFLICT);
        }


    }

    @GetMapping("/triples")
    public ResponseEntity<Collection<TripleStore>> GET_graphT() {
        LOGGER.info("triples");
        String out = "";

//        List<TripleStore> trip = new ArrayList<>();
        HashMap<String, TripleStore> trip = new HashMap<>();
//        List<Triple> triples = new ArrayList<>();
        try{
            ResultSet rs = graphOperations.runAQuery("SELECT * WHERE {GRAPH ?g {?s ?p ?o.}}");

            while (rs.hasNext()){

                QuerySolution r = rs.next();

                Triple t = new Triple();
                t.setG( setPrefix(r.get("g").toString() ));
                t.setS( setPrefix(r.get("s").toString() ));
                t.setO( setPrefix(r.get("o").toString() ));
                t.setP( setPrefix(r.get("p").toString() ));

                TripleStore ts = new TripleStore();
                if(trip.containsKey(setPrefix(r.get("g").toString()))){
                    ts = trip.get(setPrefix(r.get("g").toString()));
                } else {
                    ts.setG(setPrefix(r.get("g").toString()));
                }
                ts.getTriples().add(t);
                trip.put(setPrefix(r.get("g").toString()), ts);
            }
            return new ResponseEntity<>(trip.values(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(trip.values(), HttpStatus.CONFLICT);
        }


    }

    public String setPrefix(String uri){

        String xsd = XSD.getURI();
        String rdf = RDF.getURI();
        String rdfs = RDFS.getURI();
        String nextia = Namespaces.NextiaDI.val();
        String nextiaSchema = Namespaces.NextiaDI.val()+"DataSource/Schema/";


        if(uri.contains(xsd)){
            return uri.replace(xsd, "xsd:");
        } else if (uri.contains(rdf)){
            return uri.replace(rdf, "rdf:");
        } else if (uri.contains(rdfs)){
            return uri.replace(rdfs, "rdfs:");
        } else if (uri.contains(nextiaSchema)){
            return uri.replace(nextiaSchema, "nextia:");
        } else if (uri.contains(nextia)){
            return uri.replace(nextia, "nextia:");
        }


        return uri;
    }

}
