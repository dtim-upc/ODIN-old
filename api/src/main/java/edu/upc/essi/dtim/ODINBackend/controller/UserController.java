package edu.upc.essi.dtim.ODINBackend.controller;

import edu.upc.essi.dtim.ODINBackend.models.rest.Triple;
import edu.upc.essi.dtim.ODINBackend.utils.jena.GraphOperations;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

}
