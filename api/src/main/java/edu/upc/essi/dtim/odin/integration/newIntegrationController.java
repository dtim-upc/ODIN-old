package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.Graph;
import edu.upc.essi.dtim.NextiaDI;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
//import edu.upc.essi.dtim.odin.bootstrapping.DataSource;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.controller.SurveyAlignments;
import edu.upc.essi.dtim.odin.models.rest.IntegrationData;
import edu.upc.essi.dtim.odin.newBootstrapping.newDataSource;
import edu.upc.essi.dtim.odin.newBootstrapping.newDataSourceRepository;
import edu.upc.essi.dtim.odin.projects.Project;
import edu.upc.essi.dtim.odin.projects.ProjectRepository;
import edu.upc.essi.dtim.odin.projects.ProjectService;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import edu.upc.essi.dtim.odin.storage.graph.GraphStore;
import edu.upc.essi.dtim.odin.utils.jena.NextiaGraphy;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
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

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project/{id}/integration")
public class newIntegrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(newIntegrationController.class);

    @Autowired
    JenaConnection graph;

    @Autowired
    private newDataSourceRepository repository;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    private newDataSourceRepository dataSourceRepo;

    @Autowired
    private ProjectService projectService;

    @Autowired
    SurveyAlignments surveyA;

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


    @PostMapping("persist")
    public ResponseEntity<Project> acceptIntegration(Authentication authentication, @PathVariable("id") String id) {
        System.out.println("entering saving integration persistent");

        Project project = validateAccess(id, authentication);
        System.out.println("saving integration persistent");

        // minimal
        Graph global = graph.temporal().getGraph(project.getGlobalSchemaIRI());
        // complete
        Graph schemaInt = graph.temporal().getGraph(project.getSchemaIntegrationIRI());

        graph.persistent().deleteGraph(project.getGlobalSchemaIRI());
        graph.persistent().deleteGraph(project.getSchemaIntegrationIRI());

        graph.persistent().addModel(project.getGlobalSchemaIRI(), global);
        graph.persistent().addModel(project.getSchemaIntegrationIRI(), schemaInt );

        NextiaGraphy ng = new NextiaGraphy();
        String visualSchemaIntegration = StringEscapeUtils.unescapeJava(ng.generateVisualGraph(schemaInt));
        String visualMinimal = StringEscapeUtils.unescapeJava( ng.generateVisualGraph(global) ) ;
        System.out.println("***");
        System.out.println(visualMinimal);
        global.write("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/minimal_pers.ttl", Lang.TTL);
        schemaInt.write("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/schema_int_pers.ttl", Lang.TTL);
        projectService.updateGraphicalSchema(project, visualMinimal);
        projectService.updateGraphicalSchemaIntegration(project, visualSchemaIntegration);
        return new ResponseEntity(project, HttpStatus.OK);
    }

    @PostMapping("survey")
    public ResponseEntity<List<Alignment>> getSurveyAlignments(Authentication authentication, @PathVariable("id") String id, @RequestBody String idDS){
        Project project = validateAccess(id, authentication);
        newDataSource ds = repository.findByIDTemp(idDS);


        List<newDataSource> dataSources = dataSourceRepo.findAll(id);
        List<Alignment> alignments  = surveyA.getAlignments(project, dataSources,ds);

        return new ResponseEntity(alignments, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Project> integrate(Authentication authentication, @PathVariable("id") String id, @RequestBody IntegrationData iData) {
        System.out.println("INTEGRATING temporal with project..");

        Project project = validateAccess(id, authentication);

        NextiaDI n = new NextiaDI();
        // TODO: all these logic should be in the service class!!!
        String dsA = project.getIri();
        Model graphA = ModelFactory.createDefaultModel();;
        if( project.getNumberOfDS().equals("1") ) {

            // datasource A is the only data source ingested in project
            List<newDataSource> listDS = repository.findAll(id);
            if(listDS.size() == 1) {
                dsA = listDS.get(0).getIri();
                graphA = retrieveSourceGraph(dsA, iData.getAlignments(), graph.persistent() );
            } else {
                System.out.println("ERROR!!! CHECK NEW INTEGRATION CONTROLLER");
            }

        } else {
            graphA = retrieveSchemaIntegration(project);
        }
        String dsB = iData.getDsB().getIri();
        Model graphB = retrieveSourceGraph(dsB, iData.getAlignments(), graph.temporal() );

//        TODO: handle exceptions for integrations such as edu.upc.essi.dtim.nextiadi.exceptions.NoDomainForPropertyException
        Model integratedModel = n.Integrate(graphA, graphB, iData.getAlignments());
//        try {
//            RDFDataMgr.write(new FileOutputStream("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/integratedModel.ttl"), integratedModel, Lang.TURTLE);
//            System.out.println("file written temporal");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        Model minimal = n.getMinimalGraph2();
//        Model minimal = n.getOldMinimalGraph();
//        try {
//            RDFDataMgr.write(new FileOutputStream("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/minimal.ttl"), minimal, Lang.TURTLE);
//            System.out.println("file written temporal");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        LOGGER.info("unused are: {}",n.getUnused());
//
        NextiaGraphy ng = new NextiaGraphy();
//       I think in this case is not necessary StringEscapeUtils.unescapeJava
        String visualG = StringEscapeUtils.unescapeJava(ng.generateVisualGraph(integratedModel));
        ng = new NextiaGraphy();
        System.out.println("INTEGRATED....");
        System.out.println(visualG);
        String visualMinimal = StringEscapeUtils.unescapeJava( ng.generateVisualGraph(minimal) ) ;
        System.out.println("MINIMALx....");
        System.out.println(visualMinimal);
//                visualMinimal=     StringEscapeUtils.unescapeJava(visualMinimal );
//        System.out.println("MINIMAL ESCAPE....");
//        System.out.println(visualMinimal);
        // UPDATE PROJECT GRAPHICAL GRAPH temporal!!!
        project.setGraphicalSchemaIntegration(visualG);
        project.setGraphicalGlobalSchema(visualMinimal);


        Model simplifyI = n.getOnlyIntegrationResources();

//        String f3 = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/integrated_simple.ttl";
//        graphOperations.write(f3, simplifyI);

        graph.temporal().deleteGraph(project.getGlobalSchemaIRI());
        graph.temporal().addModel(project.getGlobalSchemaIRI(), minimal);
//        System.out.println("save temporal integration in: "+project.getGlobalSchemaIRI() );
//        System.out.println(project.getSchemaIntegrationIRI());
        graph.temporal().deleteGraph(project.getSchemaIntegrationIRI());
        graph.temporal().addModel(project.getSchemaIntegrationIRI(), simplifyI);

        graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), RDF.type.getURI(), Namespaces.Integration.val());
//        graph.temporal().addTripleLiteral(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.HAS_ID.val(), project.getGlobalSchemaID() );

        // we usually have project - datasource, but the first is datasource - datasource integration
        if( project.getNumberOfDS().equals("1") )
            graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.INTEGRATION_OF.val(), dsA  );
        graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.INTEGRATION_OF.val(), dsB  );
        graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.MINIMAL.val(), project.getGlobalSchemaIRI()  );
        graph.temporal().addTriple(project.getGlobalSchemaIRI(), project.getGlobalSchemaIRI(), DataSourceGraph.IS_MINIMAL_OF.val(), project.getSchemaIntegrationIRI()  );


//        String f3 = "/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/minimal_tmp.ttl";
//        graph.write(f3, simplifyI);
        Graph m1 = graph.temporal().getGraph(project.getGlobalSchemaIRI());
        m1.write("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/minimal_tmp.ttl", Lang.TTL);

        return new ResponseEntity(project, HttpStatus.OK);
    }



    public Model retrieveSourceGraph(String uri, List<Alignment> alignments, GraphStore gr) {
        // Todo think in a better way to do this. Maybe identifiers should be declared when loading data
        List<Alignment> aligId= alignments.stream().filter(x -> x.getType().contains("datatype")  ).collect(Collectors.toList());;

        Model sourceG = gr.getGraph(uri);

        for ( Alignment a : aligId) {
            Resource rA = sourceG.createResource(a.getIriA());
            Resource rB = sourceG.createResource(a.getIriB());

            if (sourceG.containsResource(rA) ) {
                gr.addTriple(uri, rA.getURI(), RDFS.subClassOf.getURI(),Namespaces.SCHEMA.val()+"identifier");
            } else {
                gr.addTriple(uri, rB.getURI(), RDFS.subClassOf.getURI(),Namespaces.SCHEMA.val()+"identifier");
            }
        }

        sourceG = gr.getGraph(uri);
        return  sourceG;
    }

    public Model retrieveSchemaIntegration(Project p){

        Graph schemaInt = graph.persistent().getGraph(p.getSchemaIntegrationIRI());
        String querySTR = "SELECT ?graph WHERE {  <"+p.getSchemaIntegrationIRI()+"> <"+DataSourceGraph.INTEGRATION_OF.val()+"> ?graph. }";

        ResultSet results  = graph.persistent().runAQuery(querySTR);
//        TODO: maybe some validation is needed. Compare the retrieve number of results against the project number of data sources
        while(results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            String gURI = solution.getResource("graph").getURI();
            schemaInt = schemaInt.unionG( graph.persistent().getGraph(gURI) );

        }
        return schemaInt;


    }


    @RequestMapping(path = "/download/sourcegraph", method = RequestMethod.GET)
    public ResponseEntity<String> download(Authentication authentication, @PathVariable("id") String id, @RequestParam("dsID") String dsID ) throws IOException {
//        Resource
        System.out.println("download temporal source graph...");
        Project p = validateAccess(id, authentication);
        newDataSource ds = dataSourceRepo.findByIDTemp(dsID);
        // TODO: validate access to datasource? or its enough with project access?
        Model m = dataSourceRepo.getTempG(ds);

        StringWriter v = new StringWriter();
        m.write(v, "TTL");

        return ResponseEntity.ok().body(v.toString());
    }

    @RequestMapping(path = "/download/projectschema", method = RequestMethod.GET)
    public ResponseEntity<String> downloadP(Authentication authentication, @PathVariable("id") String id ) throws IOException {
//        Resource
        System.out.println("download temporal source graph...");
        Project p = validateAccess(id, authentication);

        Model graphA = ModelFactory.createDefaultModel();;
        if( p.getNumberOfDS().equals("1") ) {

            // datasource A is the only data source ingested in project
            List<newDataSource> listDS = repository.findAll(id);
            if(listDS.size() == 1) {
                graphA = dataSourceRepo.getG(listDS.get(0));
            } else {
                System.out.println("ERROR!!! CHECK NEW INTEGRATION CONTROLLER");
            }

        } else {
            graphA =  graph.persistent().getGraph(p.getSchemaIntegrationIRI());
        }

        StringWriter v = new StringWriter();
        graphA.write(v, "TTL");

        return ResponseEntity.ok().body(v.toString());
    }


//    public Model retrieveGraph(String uri, DataSource data, List<Alignment> alignments){
//
////        if (  data.getType().equals(DataSourceTypes.INTEGRATED)  ) {
//            // generate view of all sources
//
//            Model integratedGraph = graph.persistent().getGraph(uri);
//
//            // get all graphs
//            String querySTR = "SELECT ?graph WHERE {  <"+uri+"> <"+DataSourceGraph.INTEGRATION_OF.val()+"> ?graph. }";
//
//            ResultSet results  = graph.persistent().runAQuery(querySTR);
//
//
//            while(results.hasNext()) {
//                QuerySolution solution = results.nextSolution();
//                String gURI = solution.getResource("graph").getURI();
//                integratedGraph = integratedGraph.union( graph.persistent().getGraph(gURI) );
//            }
//            return integratedGraph;
////        }

//    }


}