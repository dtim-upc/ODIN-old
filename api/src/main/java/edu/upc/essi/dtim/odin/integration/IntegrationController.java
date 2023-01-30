package edu.upc.essi.dtim.odin.integration;

import edu.upc.essi.dtim.Graph;
import edu.upc.essi.dtim.NextiaDI;
import edu.upc.essi.dtim.nextiadi.models.Alignment;
import edu.upc.essi.dtim.odin.bootstrapping.DataSourceRepository;
import edu.upc.essi.dtim.odin.bootstrapping.pojos.DataSource;
import edu.upc.essi.dtim.odin.config.vocabulary.DataSourceGraph;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.integration.pojos.IntegrationData;
import edu.upc.essi.dtim.odin.integration.pojos.IntegrationTemporalResponse;
import edu.upc.essi.dtim.odin.integration.pojos.JoinAlignment;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project/{id}/integration")
public class IntegrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationController.class);

    @Autowired
    JenaConnection graph;

    @Autowired
    private DataSourceRepository repository;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    private DataSourceRepository dataSourceRepo;

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
//        String visualSchemaIntegration = StringEscapeUtils.unescapeJava(ng.generateVisualGraph(schemaInt));
//        String visualMinimal = StringEscapeUtils.unescapeJava( ng.generateVisualGraph(global) ) ;

        String visualSchemaIntegration = StringEscapeUtils.unescapeJava(ng.generateVisualGraphNew(schemaInt));
        String visualMinimal = StringEscapeUtils.unescapeJava( ng.generateVisualGraphNew(global) ) ;


        System.out.println("***");
        System.out.println(visualMinimal);
//        global.write("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/minimal_pers.ttl", Lang.TTL);
//        schemaInt.write("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/schema_int_pers.ttl", Lang.TTL);
        projectService.updateGraphicalSchema(project, visualMinimal);
        projectService.updateGraphicalSchemaIntegration(project, visualSchemaIntegration);
        return new ResponseEntity(project, HttpStatus.OK);
    }

    @PostMapping("survey")
    public ResponseEntity<List<Alignment>> getSurveyAlignments(Authentication authentication, @PathVariable("id") String id, @RequestBody String idDS){
        Project project = validateAccess(id, authentication);
        DataSource ds = repository.findByIDTemp(idDS);


        List<DataSource> dataSources = dataSourceRepo.findAll(id);
        List<Alignment> alignments  = surveyA.getAlignments(project, dataSources,ds);

        return new ResponseEntity(alignments, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<IntegrationTemporalResponse> integrate(Authentication authentication, @PathVariable("id") String id, @RequestBody IntegrationData iData) {
        System.out.println("INTEGRATING temporal with project..");

        Project project = validateAccess(id, authentication);

        NextiaDI n = new NextiaDI();
        // TODO: all these logic should be in the service class!!!
        String dsA = project.getIri();
//        Model graphA = ModelFactory.createDefaultModel();
        Graph graphA = Graph.createDefaultGraph();
        if( project.getNumberOfDS().equals("1") ) {

            // datasource A is the only data source ingested in project
            List<DataSource> listDS = repository.findAll(id);
            if(listDS.size() == 1) {
                dsA = listDS.get(0).getIri();
                graphA = Graph.wrap(retrieveSourceGraph(dsA, iData.getAlignments(), graph.persistent() )) ;
            } else {
                System.out.println("ERROR!!! CHECK NEW INTEGRATION CONTROLLER");
            }

        } else {
            graphA = Graph.wrap( retrieveSchemaIntegrationFromTemporal(project) );
        }
        String dsB = iData.getDsB().getIri();
        Graph graphB = Graph.wrap(retrieveSourceGraph(dsB, iData.getAlignments(), graph.temporal() ) );

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

        String visualG = StringEscapeUtils.unescapeJava(ng.generateVisualGraphNew(integratedModel));
//        String visualG = StringEscapeUtils.unescapeJava(ng.generateVisualGraph(integratedModel));
        ng = new NextiaGraphy();
        String visualMinimal = StringEscapeUtils.unescapeJava( ng.generateVisualGraphNew(minimal) ) ;
        // UPDATE PROJECT GRAPHICAL GRAPH temporal!!!
        project.setGraphicalSchemaIntegration(visualG);
        project.setGraphicalGlobalSchema(visualMinimal);

        Model simplifyI = n.getOnlyIntegrationResources();
//        String f3 = "/Users/javierflores/Documents/UPC_projects/new/newODIN/api/src/test/resources/case01/Sergi/integrated_simple.ttl";
//        graphOperations.write(f3, simplifyI);

        graph.temporal().deleteGraph(project.getGlobalSchemaIRI());
        graph.temporal().addModel(project.getGlobalSchemaIRI(), minimal);
        graph.temporal().deleteGraph(project.getSchemaIntegrationIRI());
        graph.temporal().addModel(project.getSchemaIntegrationIRI(), simplifyI);

        graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), RDF.type.getURI(), Namespaces.INTEGRATION.val());

//        TODO: persist unusedAlignments to reused them incrementally
        List<Alignment> unusedAlignments = n.getUnused();
//        Gson gson = new Gson();
//        String unusedAlignmentsSTR = gson.toJson(unusedAlignments);
//        graph.temporal().addTripleLiteral(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.UNUSED_ALIGNMENTS.val(), unusedAlignmentsSTR);

        List<String> integrationOfs = new ArrayList<>();
        ResultSet result = graph.persistent().runAQuery("SELECT ?datasource WHERE { GRAPH <" + project.getSchemaIntegrationIRI() + "> { " +
                " <" + project.getSchemaIntegrationIRI() + "> <" + DataSourceGraph.INTEGRATION_OF.val() + ">  ?datasource." +
                "  } }");

        while(result.hasNext()) {
            integrationOfs.add(result.next().get("datasource").toString());
        }

        graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.INTEGRATION_OF.val(), dsB  );
        if(integrationOfs.size() != 0) {
            for(String dsIRI : integrationOfs) {
                graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.INTEGRATION_OF.val(), dsIRI  );
            }
        }
            // we usually have project - datasource then we can only add B, but the first is datasource - datasource integration, so this should add A and B
            if( project.getNumberOfDS().equals("1") )
                graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.INTEGRATION_OF.val(), dsA  );




        graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.MINIMAL.val(), project.getGlobalSchemaIRI()  );
        graph.temporal().addTriple(project.getGlobalSchemaIRI(), project.getGlobalSchemaIRI(), DataSourceGraph.IS_MINIMAL_OF.val(), project.getSchemaIntegrationIRI()  );

//        String f3 = "/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/minimal_tmp.ttl";
//        graph.write(f3, simplifyI);
//        Graph m1 = graph.temporal().getGraph(project.getGlobalSchemaIRI());
//        m1.write("/Users/javierflores/Documents/upc/projects/newODIN/api/source_schemas/minimal_tmp.ttl", Lang.TTL);

        // check if there are potential join properties

        Set<String> alignmentsAB = iData.getAlignments().stream()
                .filter(a -> a.getType().equals("datatype"))
                .map( al -> al.getIriA() + al.getIriB() )
                .collect(Collectors.toSet());

        List<Alignment> potentialJoinAlignments =
                unusedAlignments.stream()
                        .filter(e -> alignmentsAB.contains(e.getIriA()+e.getIriB()))
                        .collect(Collectors.toList());

        List<JoinAlignment> joinProperties = new ArrayList<>();
        for( Alignment a : potentialJoinAlignments) {

            JoinAlignment j = new JoinAlignment();
            j.wrap(a);

            String domainA = graphA.getDomainOfProperty( a.getIriA());
            String domainB = graphB.getDomainOfProperty( a.getIriB());
            String domainLA = graphA.getRDFSLabel(domainA);
            String domainLB = graphB.getRDFSLabel( domainB);

            j.setDomainA(domainA);
            j.setDomainB(domainB);
            j.setDomainLabelA(domainLA);
            j.setDomainLabelB(domainLB);

            joinProperties.add(j);

        }

        return new ResponseEntity(new IntegrationTemporalResponse(project, joinProperties), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<Project> integrateJoins(Authentication authentication, @PathVariable("id") String id, @RequestBody List<JoinAlignment> joinA){

        System.out.println("INTEGRATING joins in temporal integration...");

        Project project = validateAccess(id, authentication);
        Model schemaIntegration = retrieveSchemaIntegrationFromTemporal(project);
        NextiaDI n = new NextiaDI();

        for(JoinAlignment a : joinA) {

            if(a.getRightArrow())
                schemaIntegration = n.JoinIntegration(schemaIntegration, a.getIriA(), a.getIriB(), a.getL(), a.getRelationship(), a.getDomainA(), a.getDomainB());
            else
                schemaIntegration = n.JoinIntegration(schemaIntegration, a.getIriA(), a.getIriB(), a.getL(), a.getRelationship(), a.getDomainB(), a.getDomainA());

        }

        Model minimal = n.getMinimalGraph2();

        // generate again visual graph
        NextiaGraphy ng = new NextiaGraphy();
        String visualG = StringEscapeUtils.unescapeJava(ng.generateVisualGraphNew(schemaIntegration));

        ng = new NextiaGraphy();
        String visualMinimal = StringEscapeUtils.unescapeJava( ng.generateVisualGraphNew(minimal) ) ;
        project.setGraphicalSchemaIntegration(visualG);
        project.setGraphicalGlobalSchema(visualMinimal);


        List<String> integrationOfs = new ArrayList<>();
        ResultSet result = graph.temporal().runAQuery("SELECT ?datasource WHERE { GRAPH <" + project.getSchemaIntegrationIRI() + "> { " +
                " <" + project.getSchemaIntegrationIRI() + "> <" + DataSourceGraph.INTEGRATION_OF.val() + ">  ?datasource." +
                "  } }");

        while(result.hasNext()) {
            integrationOfs.add(result.next().get("datasource").toString());
        }

//

//        String integrationOf = graph.temporal().runAQuery("SELECT ?datasource WHERE { GRAPH <"+project.getSchemaIntegrationIRI()+"> { " +
//                " <"+project.getSchemaIntegrationIRI()+"> <"+DataSourceGraph.INTEGRATION_OF.val()+">  ?datasource." +
//                "  } }").next().get("datasource").toString();

        graph.temporal().deleteGraph(project.getGlobalSchemaIRI());
        graph.temporal().addModel(project.getGlobalSchemaIRI(), minimal);
        graph.temporal().deleteGraph(project.getSchemaIntegrationIRI());
        graph.temporal().addModel(project.getSchemaIntegrationIRI(), n.getOnlyIntegrationResources());

        Graph m1 = Graph.wrap( n.getOnlyIntegrationResources());
        m1.write("/Users/javierflores/Documents/upc/projects/newODIN/api/landing_zone_1/onlyIResources.ttl", Lang.TTL);

        graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), RDF.type.getURI(), Namespaces.INTEGRATION.val());

        for(String dsIRI : integrationOfs) {
            graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.INTEGRATION_OF.val(), dsIRI  );
        }
//        graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.INTEGRATION_OF.val(), integrationOf  );
        graph.temporal().addTriple(project.getSchemaIntegrationIRI(), project.getSchemaIntegrationIRI(), DataSourceGraph.MINIMAL.val(), project.getGlobalSchemaIRI()  );
        graph.temporal().addTriple(project.getGlobalSchemaIRI(), project.getGlobalSchemaIRI(), DataSourceGraph.IS_MINIMAL_OF.val(), project.getSchemaIntegrationIRI()  );

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

    public Model retrieveSchemaIntegrationFromTemporal(Project p){
        //Possible bug between temporal and persistent
        Graph schemaInt = graph.temporal().getGraph(p.getSchemaIntegrationIRI());
        String querySTR = "SELECT ?graph WHERE {  <"+p.getSchemaIntegrationIRI()+"> <"+DataSourceGraph.INTEGRATION_OF.val()+"> ?graph. }";

        ResultSet results  = graph.temporal().runAQuery(querySTR);
//        TODO: maybe some validation is needed. Compare the retrieve number of results against the project number of data sources
        while(results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            String gURI = solution.getResource("graph").getURI();
            if(graph.persistent().getGraph(gURI) != null)
                schemaInt = schemaInt.unionG( graph.persistent().getGraph(gURI) );
            else
                schemaInt = schemaInt.unionG( graph.temporal().getGraph(gURI) );

        }
        return schemaInt;


    }


    @RequestMapping(path = "/download/sourcegraph", method = RequestMethod.GET)
    public ResponseEntity<String> download(Authentication authentication, @PathVariable("id") String id, @RequestParam("dsID") String dsID ) throws IOException {
//        Resource
        System.out.println("download temporal source graph...");
        Project p = validateAccess(id, authentication);
        DataSource ds = dataSourceRepo.findByIDTemp(dsID);
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
            List<DataSource> listDS = repository.findAll(id);
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