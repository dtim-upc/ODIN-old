package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import org.apache.jena.rdf.model.Model;

/**
 * Just auxiliar structure
 */
public class GraphModelPair {
    private final Graph graph;
    private final Model model;

    /**
     * Constructs a new GraphModelPair.
     *
     * @param graph The graph.
     * @param model The model.
     */
    public GraphModelPair(Graph graph, Model model) {
        this.graph = graph;
        this.model = model;
    }

    /**
     * Retrieves the graph.
     *
     * @return The graph.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Retrieves the model.
     *
     * @return The model.
     */
    public Model getModel() {
        return model;
    }
}
