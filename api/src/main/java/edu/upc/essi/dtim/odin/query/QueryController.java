package edu.upc.essi.dtim.odin.query;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.Sets;
import edu.upc.essi.dtim.NextiaQR_RDFS;
import edu.upc.essi.dtim.nextiaqr.models.querying.RDFSResult;
import edu.upc.essi.dtim.odin.query.pojos.QueryDataSelection;
import edu.upc.essi.dtim.odin.projects.Project;
import edu.upc.essi.dtim.odin.projects.ProjectRepository;
import edu.upc.essi.dtim.odin.query.pojos.ODINQuery;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import scala.Tuple3;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/query/{id}")
public class QueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    private QueryService Iservice;

    // maybe this can be done with @preauthorize isresourceowner
    // https://stackoverflow.com/questions/70096839/how-to-check-security-access-before-validation-valid-in-controller
    public Project validateAccess(String projectID, Authentication authentication){
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Map<String, Object> attributes = token.getTokenAttributes();
        Project project = projectRepo.findByID(projectID);
        // validate access
        if(!project.getCreatedBy().equals(attributes.get("username").toString())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not have accessed to this project");
        }
        return project;
    }

//    fromGraphicalToSPARQL
    @PostMapping(value="graphical")
    public ResponseEntity<RDFSResult> POST_query_fromGraphicalToSPARQL(Authentication authentication, @PathVariable("id") String id, @RequestBody QueryDataSelection body) {

        LOGGER.info("[POST /OMQ/fromGraphicalToSPARQL/]");
        Project project = validateAccess(id, authentication);
        System.out.println("saving integration persistent");
        ODINQuery constructs = Iservice.retrieveConstructs(project, body);

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

        NextiaQR_RDFS n = new NextiaQR_RDFS();
        System.out.println("QUERY: ");
        System.out.println(query.toString());

        List<Integer> cont = new ArrayList<>();
        cont.add(1);
        constructs.getSourceGraphs().forEach( (g, m) -> {
            try {
                cont.set(0, cont.get(0)+1);
                RDFDataMgr.write(new FileOutputStream("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/source"+cont.get(0)+".ttl"), m, Lang.TURTLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        cont.add(1);
        constructs.getSubGraphs().forEach( (g, m) -> {
            try {
                cont.set(1, cont.get(1)+1);
                RDFDataMgr.write(new FileOutputStream("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/subgraph"+cont.get(1)+".ttl"), m, Lang.TURTLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        try {
            RDFDataMgr.write(new FileOutputStream("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/minimal2.ttl"), constructs.getMinimal(), Lang.TURTLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        RDFSResult res = n.rewriteToUnionOfConjunctiveQueries(constructs.getSourceGraphs(), constructs.getMinimal(),
                constructs.getSubGraphs(), query.toString());

        return new ResponseEntity<>(res, HttpStatus.OK );
    }


}
