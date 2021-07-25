package edu.upc.essi.dtim.metadatastorage.services.omq;

import edu.upc.essi.dtim.metadatastorage.services.omq.wrapper_implementations.JSON_Wrapper;
import edu.upc.essi.dtim.metadatastorage.utils.jena.GraphOperations;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Getter @Setter
public class Wrapper extends RelationalOperator {
    @Autowired
    GraphOperations graphOperations;

    private String wrapper;

    public Wrapper(String w) {
        this.wrapper = w;
    }
    public String inferSchema() throws Exception {
        throw new Exception("Can't infer the schema of a generic wrapper, need to call an implementation subclass");
    }
}
