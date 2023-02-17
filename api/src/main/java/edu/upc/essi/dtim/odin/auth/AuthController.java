package edu.upc.essi.dtim.odin.auth;

import edu.upc.essi.dtim.odin.auth.responses.LoginResult;
import edu.upc.essi.dtim.odin.auth.responses.Triple;
import edu.upc.essi.dtim.odin.auth.responses.TripleStore;
import edu.upc.essi.dtim.odin.auth.user.User;
import edu.upc.essi.dtim.odin.auth.user.UserRepository;
import edu.upc.essi.dtim.odin.auth.user.UserService;
import edu.upc.essi.dtim.odin.config.auth.JwtHelper;
import edu.upc.essi.dtim.odin.config.auth.WebSecurityConfig;
import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import edu.upc.essi.dtim.odin.projects.Project;
import edu.upc.essi.dtim.odin.projects.ProjectService;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import edu.upc.essi.dtim.odin.storage.filestorage.StorageProperties;
import edu.upc.essi.dtim.odin.utils.jena.NextiaGraphy;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private UserService userService;
    private UserRepository userRepo;

    @Autowired
    ProjectService projectService;

    //just temporal. delete later
    @Autowired
    StorageProperties properties;


    @Autowired
    private JenaConnection graph;

    public AuthController(JwtHelper jwtHelper, UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder, UserService userService, UserRepository userRepo) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @PostMapping(path = "login")
    public LoginResult login(@RequestBody User user) {

//        UserDetails userDetails;
//        try {
//            // checks if there’s a user is present.
//            userDetails = userDetailsService.loadUserByUsername(username);
//        } catch (UsernameNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
//        }
//
//        if (passwordEncoder.matches(password, userDetails.getPassword())) {
//            Map<String, String> claims = new HashMap<>();
//            claims.put("username", username);
//
//            String authorities = userDetails.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.joining(","));
//            claims.put("authorities", authorities);
//            claims.put("userId", String.valueOf(1));
//
//            String jwt = jwtHelper.createJwtForClaims(username, claims);
//            return new LoginResult(jwt);
//        }
//
//        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
//        User user = userRepository.findByUsername(_user.getUsername());

        UserDetails userDetails;
        try {
            // checks if user exists
            userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        System.out.println("password is: "+userDetails.getPassword());
        if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            Map<String, String> claims = new HashMap<>();
            claims.put("username", user.getUsername());

            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
            claims.put(WebSecurityConfig.AUTHORITIES_CLAIM_NAME, authorities);
            claims.put("userId", String.valueOf(1));

            String jwt = jwtHelper.createJwtForClaims(user.getUsername(), claims);
            // to get complete data
            User u = userRepo.findByUsername(user.getUsername());
            return new LoginResult(jwt, u);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    @PostMapping("prueba")
    public ResponseEntity<String> pru(@RequestBody String path){
        System.out.println("Generating visual graph for file: "+path);
        String visualSchemaIntegration = "";
        if(path != null) {
            Model model = RDFDataMgr.loadModel(path) ;
            NextiaGraphy ng = new NextiaGraphy();
//        String visualSchemaIntegration = ng.generateVisualGraph(model);
            visualSchemaIntegration = ng.generateVisualGraphNew(model);
        }

        return new ResponseEntity<>(visualSchemaIntegration, HttpStatus.OK);
    }

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody User user){
        System.out.println("SIGNUP().....");
//        User existingUser = userRepo.findByUsername(user.getUsername());
//                userDetailsService.loadUserByUsername(user.getUsername());
//        findUserByEmail(userDto.getEmail());

        if( userRepo.existUserName(user) ) {
            //                    "There is already an account registered with the same email");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }


        Boolean flag = userService.saveUser(user);
        try(FileWriter fw = new FileWriter(properties.getDir()+"/users.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            out.println("Created user: "+user.getUsername() +",Name: "+ user.getFirstName() +" "+user.getLastName() +" , Date: " + formatter.format(date) );
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }


        if(flag) {
            // SURVEY. remove after survey finished. Otherwise, it will create a project called "survey_NextiaDI" for every user.
            Project project = new Project();
            project.setName("Survey_NextiaDI");
            project.setCreatedBy(user.getUsername());
            project.setColor("#dbe2e7");
            projectService.create(project);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @GetMapping("/triples")
    public ResponseEntity<Collection<TripleStore>> GET_graphT() {
//        LOGGER.info("triples");
        String out = "";

//        graph.temporal().findAllDataSources();

//        List<TripleStore> trip = new ArrayList<>();
        HashMap<String, TripleStore> trip = new HashMap<>();
//        List<Triple> triples = new ArrayList<>();
        try{
            ResultSet rs = graph.temporal().runAQuery("SELECT * WHERE {GRAPH ?g {?s ?p ?o.}}");

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
//            LOGGER.info("something hppend", e);
            return new ResponseEntity<>(trip.values(), HttpStatus.CONFLICT);
        }


    }

    public String setPrefix(String uri){

        String xsd = XSD.getURI();
        String rdf = RDF.getURI();
        String rdfs = RDFS.getURI();
        String nextia = Namespaces.NEXTIADI.val();
        String nextiaSchema = Namespaces.NEXTIADI.val()+"DataSource/Schema/";


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