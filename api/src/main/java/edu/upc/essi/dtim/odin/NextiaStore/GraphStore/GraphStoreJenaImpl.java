package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;


import edu.upc.essi.dtim.NextiaCore.graph.*;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphStoreJenaImpl implements GraphStoreInterface {
    private final Dataset dataset;


    public GraphStoreJenaImpl(@Autowired AppConfig appConfig) {
        String directory = appConfig.getJenaPath();

        //Open TDB Dataset
        dataset = TDBFactory.createDataset(directory);
    }

    /**
     * Saves the given graph.
     *
     * @param graph the graph to save
     */
    @Override
    public void saveGraph(Graph graph) {
        /*Model modelToSave = graph.;
        dataset.begin(ReadWrite.WRITE);
        try {
            String modelName = graph.getGraph().getName().getURI();
            dataset.addNamedModel(modelName, modelToSave);
            dataset.commit();
        } catch (final Exception ex) {
            dataset.abort();
            throw ex;
        }*/
    }

    /**
     * Retrieves the graph with the given name.
     *
     * @param name the URI of the graph to retrieve
     * @return the retrieved graph
     */
    @Override
    public Graph getGraph(URI name) {
        dataset.begin(ReadWrite.READ);
        try {
            //Retrieve Named Graph from Dataset, or use Default Graph.
            String modelName = name.getURI();
            if (dataset.containsNamedModel(modelName)) {
                Model model = dataset.getNamedModel(modelName);
                //TODO
                return CoreGraphFactory.createGraphInstance("normal");
            } else {
                throw getIllegalArgumentException(name);
            }
        } catch (final Exception ex) {
            dataset.abort();
            throw ex;
        } finally {
            dataset.end();
        }
    }

    private static IllegalArgumentException getIllegalArgumentException(URI name) {
        return new IllegalArgumentException("Graph " + name.getURI() + " not found");
    }

    /**
     * Deletes the graph with the given name.
     *
     * @param name the URI of the graph to delete
     */
    @Override
    public void deleteGraph(URI name) {
        dataset.begin(ReadWrite.WRITE);
        try {
            String modelName = name.getURI();
            if (dataset.containsNamedModel(modelName)) {
                dataset.removeNamedModel(modelName);
            } else {
                throw getIllegalArgumentException(name);
            }
            dataset.commit();
        } catch (final Exception ex) {
            dataset.abort();
            throw ex;
        }
    }
}

