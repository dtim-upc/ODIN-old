package edu.upc.essi.dtim.ODINBackend.utils.jena.query;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.Var;

public class SelectQuery {
    private Var VAR_graph = Var.alloc( "g" );
    private Var VAR_subject = Var.alloc( "s" );
    private Var VAR_predicate = Var.alloc( "p" );
    private Var VAR_object = Var.alloc( "o" );

    /**
     *
     * @return PRODUCES: SELECT * WHERE { GRAPH ?g {?s ?p ?o} }
     */
    private SelectBuilder getDefaultSelectAllFromGraph(){
        SelectBuilder query = new SelectBuilder().addGraph( VAR_graph,VAR_subject, VAR_predicate, VAR_object );
        return query;
    }

    /**
     *
     * @param namedGraph
     * @param predicate to set in the where clause. If null, a var ?p is used.
     * @return SELECT ?s ?o WHERE {GRAPH <namedGraph>  { ?s ?p ?o } } in case predicate null
     */
    public Query selectSubjectAndFeatureFromGraph(String namedGraph, String predicate){
        SelectBuilder query = getDefaultSelectAllFromGraph().addVar(VAR_subject).addVar(VAR_object);
        query.setVar( VAR_graph, NodeFactory.createURI( namedGraph ) ) ;
        if(predicate != null)
            query.setVar( VAR_predicate, NodeFactory.createURI( predicate ) ) ;
        return query.build();
    }
}
