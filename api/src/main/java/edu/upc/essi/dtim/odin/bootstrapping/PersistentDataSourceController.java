package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.odin.bootstrapping.pojos.DataSource;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.auth.responses.Triple;
import edu.upc.essi.dtim.odin.auth.responses.TripleStore;
import edu.upc.essi.dtim.odin.projects.Project;
import edu.upc.essi.dtim.odin.projects.ProjectRepository;
import edu.upc.essi.dtim.odin.projects.ProjectService;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project/{id}/datasources")
public class PersistentDataSourceController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private final String EMPTY_INPUTS = "{}";

    @Autowired
    private JenaConnection graph;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private DataSourceRepository dataSourceRepo;

    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private ProjectService projectService;

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
    public ResponseEntity<DataSource> persistDataSource(Authentication authentication, @PathVariable("id") String id, @RequestBody DataSource dataSource) {
        System.out.println("Persist data source...");
        Project project = validateAccess(id, authentication);
        // just for security
        dataSource.setProjectID(id);
        DataSource ds = dataSourceService.persist(project, dataSource);


        if(project.getNumberOfDS().equals("0") ) {
            // if this is gonna be the first data source, we assign its source schema to the project schema (global)
            projectService.updateGraphicalSchema(project, dataSource.getGraphicalSchema());
        }
        projectService.increaseNumberDSBy1(project);

        return new ResponseEntity<>(dataSource, HttpStatus.CREATED);
    }


//    @PostMapping( consumes = {"multipart/form-data"})
//    public ResponseEntity<newDataSource> createDataSource(Authentication authentication, @PathVariable("id") String id, @RequestPart newDataSource dataSource, @RequestPart MultipartFile file) {
//        LOGGER.debug("inside of createDataSource method");
//        validateAccess(id, authentication);
//        // just for security
//        dataSource.setProjectID(id);
//        try {
//            newDataSource _dataSource = dataSourceService.create(dataSource, file);
//
//            String input = dataSource.toString().replaceAll("[\n\r\t]", "_");
//            String returnval = _dataSource.toString().replaceAll("[\n\r\t]", "_");
//
//            LOGGER.info(LOG_MSG, "createDataSource", input, returnval);
//            return new ResponseEntity<>(_dataSource, HttpStatus.CREATED);
//        } catch (Exception e) {
//            LOGGER.error(e.toString(), e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @RequestMapping(path = "/download/sourcegraph", method = RequestMethod.GET)
    public ResponseEntity<String> download(Authentication authentication, @PathVariable("id") String id, @RequestParam("dsID") String dsID ) throws IOException {
//        Resource
        Project p = validateAccess(id, authentication);
        DataSource ds = dataSourceRepo.findByID(dsID);
        // TODO: validate access to datasource? or its enough with project access?
        Model m = dataSourceRepo.getG(ds);
//        OutputStream out = new FileOutputStream(ds.getName()+ ".ttl");
//        m.write(out, "TTL");
        StringWriter v = new StringWriter();
        m.write(v, "TTL");

//        File file = new File(ds.getPath());
//        Path path = Paths.get(file.getAbsolutePath());
//        InputStream inputStream = new FileInputStream(file.getAbsolutePath());
//        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentLength(Files.size(Paths.get(filePath)));
//        return new ResponseEntity(inputStreamResource, headers, HttpStatus.OK);
//
//
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ds.getFilename());
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//
//        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
//                .headers(header)
//                .contentLength(file.length())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(v.toString());
    }

    @GetMapping
    public ResponseEntity<List<DataSource>> getAllDataSources(Authentication authentication, @PathVariable("id") String id) {
//        System.out.println("Project is " + p.getCreatedBy());
        validateAccess(id, authentication);

        try {
            List<DataSource> dataSources = dataSourceRepo.findAll(id);
            if (dataSources.isEmpty()) {
                ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                LOGGER.info(LOG_MSG, "getAllDataSources from project", "", response);
                return response;
            }
            LOGGER.info(LOG_MSG, "getAllDataSources from project", EMPTY_INPUTS, "" );
            return new ResponseEntity<>(dataSources, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{dsID}")
    public ResponseEntity<HttpStatus> deleteDataSources(Authentication authentication, @PathVariable("id") String id, @PathVariable("dsID") String dsID) throws IOException {
        System.out.println("delete persistent data source...");
        Project project = validateAccess(id, authentication);
        // TODO: think how to be sure graph is deleted. Maybe after delete it, check if exist?
        // TODO: RESET GLOBAL SCHEMA IF NO DATA SOURCES LEFT
        dataSourceService.delete(dsID);
        projectService.decreaseNumberDSBy1(project);

        LOGGER.info(LOG_MSG, "deleteDataSources", dsID, HttpStatus.NO_CONTENT.toString() );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/triples/{dsID}")
    public ResponseEntity<Collection<TripleStore>> GET_graphT(Authentication authentication, @PathVariable("id") String id, @PathVariable("dsID") String dsID) {
        System.out.println("triples...");
        Project project = validateAccess(id, authentication);
        String out = "";
        HashMap<String, TripleStore> trip = new HashMap<>();
        DataSource dataSource = dataSourceRepo.findByID(dsID);
        try{
            ResultSet rs = graph.persistent().runAQuery("SELECT * WHERE {GRAPH <"+dataSource.getIri()+"> {?s ?p ?o.}}");
            TripleStore ts = new TripleStore();
            ts.setG(setPrefix(dataSource.getIri(), dataSource.getId()));
            while (rs.hasNext()){

                QuerySolution r = rs.next();

                Triple t = new Triple();
                t.setG( setPrefix(dataSource.getIri(), dataSource.getId() ));
                t.setS( setPrefix(r.get("s").toString() , dataSource.getId()));
                t.setO( setPrefix(r.get("o").toString(), dataSource.getId() ));
                t.setP( setPrefix(r.get("p").toString(), dataSource.getId() ));

                if(!r.get("p").toString().equals("http://www.essi.upc.edu/DTIM/NextiaDI/DataSource/graphicalGraph")){
                    ts.getTriples().add(t);
                    trip.put(setPrefix(dataSource.getIri(), dataSource.getId()), ts);
                }

            }
            return new ResponseEntity<>(trip.values(),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(trip.values(), HttpStatus.CONFLICT);
        }
    }

    public String setPrefix(String uri, String id){

        String xsd = XSD.getURI();
        String rdf = RDF.getURI();
        String rdfs = RDFS.getURI();
        String nextia = Namespaces.NEXTIADI.val()+ "DataSource/";
        String nextiaSchema = Namespaces.NEXTIADI.val()+"DataSource/Schema/"+ id+"/";


        if(uri.contains(xsd)){
            return uri.replace(xsd, "xsd:");
        } else if (uri.contains(rdf)){
            return uri.replace(rdf, "rdf:");
        } else if (uri.contains(rdfs)){
            return uri.replace(rdfs, "rdfs:");
        } else if (uri.contains(nextiaSchema)){
            return uri.replace(nextiaSchema, "schema:");
        } else if (uri.contains(nextia)){
            return uri.replace(nextia, "datasource:");
        }


        return uri;
    }

}
