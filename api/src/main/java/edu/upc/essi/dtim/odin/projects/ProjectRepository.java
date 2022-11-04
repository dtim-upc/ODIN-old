package edu.upc.essi.dtim.odin.projects;

import edu.upc.essi.dtim.Graph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectRepository {

    @Autowired
    private JenaConnection graph;

    public Project save(Project project) {


        Graph g = Graph.createDefaultGraph();
        g.add(project.getIri(), RDF.type.getURI(), Namespaces.PROJECT.val());
        g.addLiteral(project.getIri(), ProjectGraph.HAS_ID.val(), project.getId() );
        g.addLiteral(project.getIri(), ProjectGraph.CREATED_BY.val(), project.getCreatedBy() );
        g.addLiteral(project.getIri(), ProjectGraph.DESCRIPTION.val(), project.getDescription() );
        g.addLiteral(project.getIri(), RDFS.label.getURI(), project.getName() );
        g.addLiteral(project.getIri(), ProjectGraph.PRIVACY.val(), project.getPrivacy() );
        g.addLiteral(project.getIri(), ProjectGraph.COLOR.val(), project.getColor() );
        g.addLiteral(project.getIri(), ProjectGraph.NUMBERDATASOURCES.val(), project.getNumberOfDS() );
        g.addLiteral(project.getIri(), ProjectGraph.GLOBALSCHEMA.val(), project.getGraphicalGlobalSchema() );
        g.addLiteral(project.getIri(), ProjectGraph.INTEGRATEDSCHEMA.val(), project.getGraphicalSchemaIntegration() );
        graph.persistent().addModel(project.getIri(), g);

        graph.persistent().addModel( project.getGlobalSchemaIRI(), Graph.createDefaultGraph()  );
        graph.temporal().addModel( project.getGlobalSchemaIRI(), Graph.createDefaultGraph()  );

        graph.persistent().addModel( project.getSchemaIntegrationIRI(), Graph.createDefaultGraph()  );
        graph.temporal().addModel( project.getSchemaIntegrationIRI(), Graph.createDefaultGraph()  );
        // todo: not sure if graph is updated or we need to add model to repo
        return project;
    }

    public void cloneProject(){

    }

    public Project updateNumberDataSources(Project oldProject, String newNumber){
        System.out.println("updating number data sources to" + newNumber);
        graph.persistent().updateLiteral(
                oldProject.getIri(),
                oldProject.getIri(),
                ProjectGraph.NUMBERDATASOURCES.val(),
                oldProject.getNumberOfDS(),
                newNumber );

        oldProject.setNumberOfDS(newNumber);
        return oldProject;
    }

    public Project updateGraphicalGlobalSchema(Project oldProject, String newGraphical){
        System.out.println("updating graphical schema");
        graph.persistent().updateLiteral(
                oldProject.getIri(),
                oldProject.getIri(),
                ProjectGraph.GLOBALSCHEMA.val(),
                oldProject.getGraphicalGlobalSchema(),
                newGraphical );

        oldProject.setGraphicalGlobalSchema(newGraphical);
        return oldProject;
    }

    public Project updateGraphicalIntegratedSchema(Project oldProject, String newGraphical){
        System.out.println("updating graphical integrated schema");
        graph.persistent().updateLiteral(
                oldProject.getIri(),
                oldProject.getIri(),
                ProjectGraph.INTEGRATEDSCHEMA.val(),
                oldProject.getGraphicalGlobalSchema(),
                newGraphical );

        oldProject.setGraphicalGlobalSchema(newGraphical);
        return oldProject;
    }



    public void delete(Project project) {
//        TODO: delete also data sources
        graph.persistent().deleteGraph(project.getIri());
    }

    // only find all projects related to a user
    public List<Project> findAll(String username) {
        System.out.println("find all projects created by "+username);
        List<Project> projects = new ArrayList<>();

        // resulset is supposed to be unique
        String query = commonPrefixes() + "SELECT * WHERE { " +
                "?project rdf:type <"+ Namespaces.PROJECT.val()+">;" +
                " <"+ProjectGraph.CREATED_BY.val()+"> '"+username+"'  " +
                "OPTIONAL { ?project <"+ProjectGraph.HAS_ID.val()+"> ?id } " +
                "OPTIONAL { ?project <"+ProjectGraph.DESCRIPTION.val()+"> ?description } " +
                "OPTIONAL { ?project <"+RDFS.label.getURI()+"> ?name } " +
                "OPTIONAL { ?project <"+ProjectGraph.PRIVACY.val()+"> ?privacy } " +
                "OPTIONAL { ?project <"+ProjectGraph.COLOR.val()+"> ?color } " +
                "OPTIONAL { ?project <"+ProjectGraph.NUMBERDATASOURCES.val()+"> ?numberOfDS } " +
                "}";

        ResultSet res = graph.persistent().runAQuery(query);

        while(res.hasNext() ) {

            QuerySolution r = res.next();
            Project p = new Project();
            p.setId( r.get("id").toString() );
            p.setCreatedBy( username );
            p.setDescription( r.get("description").toString() );
            p.setName( r.get("name").toString()  );
            p.setPrivacy( r.get("privacy").toString() );
            p.setColor( r.get("color").toString() );
            p.setNumberOfDS( r.get("numberOfDS").toString() );
            projects.add(p);

        }

        return projects;
    }


    public Project findByID(String id) {

        // resulset is supposed to be unique
        String query = commonPrefixes() + "SELECT * WHERE { " +
                "?project rdf:type <"+ Namespaces.PROJECT.val()+">;" +
                " <"+ProjectGraph.CREATED_BY.val()+"> ?username;  " +
                " <"+ProjectGraph.HAS_ID.val()+"> '"+id+"'. " +
                "OPTIONAL { ?project <"+ProjectGraph.DESCRIPTION.val()+"> ?description } " +
                "OPTIONAL { ?project <"+RDFS.label.getURI()+"> ?name } " +
                "OPTIONAL { ?project <"+ProjectGraph.PRIVACY.val()+"> ?privacy } " +
                "OPTIONAL { ?project <"+ProjectGraph.COLOR.val()+"> ?color } " +
                "OPTIONAL { ?project <"+ProjectGraph.NUMBERDATASOURCES.val()+"> ?numberOfDS } " +
                "OPTIONAL { ?project <"+ProjectGraph.GLOBALSCHEMA.val()+"> ?graphicalGlobal } " +
                "OPTIONAL { ?project <"+ProjectGraph.INTEGRATEDSCHEMA.val()+"> ?graphicalIntegrated } " +
                "}";

        //should only return one. If make test, ensure that
        ResultSet res = graph.persistent().runAQuery(query);


        if(res.hasNext() ) {
            Project p = new Project();
            QuerySolution r = res.next();
            p.setId(id );
            p.setCreatedBy( r.get("username").toString() );
            p.setDescription( r.get("description").toString() );
            p.setName( r.get("name").toString()  );
            p.setPrivacy( r.get("privacy").toString() );
            p.setColor( r.get("color").toString() );
            p.setNumberOfDS( r.get("numberOfDS").toString() );
            p.setGraphicalGlobalSchema(StringEscapeUtils.unescapeJava(r.get("graphicalGlobal").toString()));
            p.setGraphicalSchemaIntegration(StringEscapeUtils.unescapeJava(r.get("graphicalIntegrated").toString()));
            return p;
        }
        return null;
    }

    public Graph getTemporalG(String iri){
        return graph.temporal().getGraph(iri);
    }

//    TODO: make it global somehow
    private String commonPrefixes(){

        return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX nextiaDS: <http://www.essi.upc.edu/DTIM/NextiaDI/DataSource/> ";

    }

}
