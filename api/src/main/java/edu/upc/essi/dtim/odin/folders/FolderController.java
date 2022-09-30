package edu.upc.essi.dtim.odin.folders;

import edu.upc.essi.dtim.odin.bootstrapping.DataSource;
import edu.upc.essi.dtim.odin.bootstrapping.DataSourceController;
import edu.upc.essi.dtim.odin.storage.JenaConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/folder")
public class FolderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceController.class);
    private static final String LOG_MSG = "{} request finished with inputs: {} and return value: {}";
    private static final String EMPTY_INPUTS = "{}";

    @Autowired
    private JenaConnection graph;

   @GetMapping
   public ResponseEntity<List<Folder>> getAllFolders(){

       List<Folder> folders = new ArrayList<>();
       return new ResponseEntity<>(folders, HttpStatus.OK);
   }


//   @PostMapping
//   public ResponseEntity<List<Folder>> createFolder(@RequestBody Folder folder) {
//
//
//       return new ResponseEntity<>(_dataSource, HttpStatus.CREATED);
//   }





   }
