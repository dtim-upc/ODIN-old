package edu.upc.essi.dtim.odin.auth.user;

import edu.upc.essi.dtim.Graph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import edu.upc.essi.dtim.odin.utils.jena.GraphOperations;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

    @Value("${dataStorage.namedGraphs.users}")
    private String userGraph;
    private JenaConnection graph;


    @Autowired
    private GraphOperations gp;

    // it does not need @autowired cause it's default constructor. More info here https://stackoverflow.com/questions/41092751/spring-injects-dependencies-in-constructor-without-autowired-annotation
    public UserRepository(JenaConnection graph) {
        this.graph = graph;
    }

    public Boolean save(User user){
        gp.addTriple(userGraph, user.getIri(), RDF.type.getURI(), Namespaces.USER.val() );
        gp.addTripleLiteral(userGraph, user.getIri(), UserVocabulary.HAS_USERNAME.val(), user.getUsername());
        graph.persistent().addTriple(userGraph, user.getIri(), RDF.type.getURI(), Namespaces.USER.val() );
        graph.persistent().addTripleLiteral(userGraph, user.getIri(), UserVocabulary.HAS_USERNAME.val(), user.getUsername());
        graph.persistent().addTripleLiteral(userGraph, user.getIri(), UserVocabulary.HAS_FIRSTNAME.val(), user.getFirstName());
        graph.persistent().addTripleLiteral(userGraph, user.getIri(), UserVocabulary.HAS_LASTNAME.val(), user.getLastName());
        graph.persistent().addTripleLiteral(userGraph, user.getIri(), UserVocabulary.HAS_PASSWORD.val(), user.getPassword());
        graph.persistent().addTripleLiteral(userGraph, user.getIri(), UserVocabulary.HAS_ROLES.val(), String.join(" ",user.getRoles() ));

        if(existUserName(user)){
            return true;
        }

        return false;
    }

    public void Delete(User user){

//        create method to delete all triples related to subject in a given graph

    }

    public Boolean existUserName(User user) {

        Graph g = graph.persistent().getGraph(userGraph);

        if(g!=null) {
            return g.contains(user.getIri(), RDF.type.getURI(), Namespaces.USER.val());
        }
        // user graph have not been initialized. No users
        return false;


    }


    public User findByUsername(String username) {

        User u = new User();
        u.setUsername(username);
        String subject = u.getIri();
        String query = commonPrefixes() + " SELECT * WHERE { " +
                " <"+subject+"> rdf:type <"+Namespaces.USER.val()+">; " +
                " <"+UserVocabulary.HAS_USERNAME.val()+"> ?username; " +
                " <"+UserVocabulary.HAS_PASSWORD.val()+"> ?password; " +
                " <"+UserVocabulary.HAS_FIRSTNAME.val()+"> ?firstname;" +
                " <"+UserVocabulary.HAS_LASTNAME.val()+"> ?lastname;" +
                " <"+UserVocabulary.HAS_ROLES.val()+"> ?roles." +
                " }";

        ResultSet res = graph.persistent().runAQuery(query);
        if(res.hasNext()){
            QuerySolution r = res.next();
            // password is already encoded
            u.setPassword( r.get("password").toString() );
            u.setFirstName( r.get("firstname").toString() );
            u.setLastName( r.get("lastname").toString() );
            u.setRoles( r.get("roles").toString().split(" ") );
            return u;
        }
        return null;

//        if(username.equalsIgnoreCase("admin")) {
//            return new User(username, "admin123", "ADMIN");
//        }
//        return null;
    }


    private String commonPrefixes(){

        return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX nextiaDS: <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource/> ";

    }







}
