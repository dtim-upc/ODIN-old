package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.CsvDataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.JsonDataset;
import edu.upc.essi.dtim.NextiaCore.graph.CoreGraphFactory;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.nextiabs2.CSVBootstrap_with_DataFrame_MM_without_Jena;
import edu.upc.essi.dtim.nextiabs2.JSONBootstrap_with_DataFrame_MM_without_Jena;

import java.io.IOException;

public class bsModuleImpl implements bsModuleInterface{

    @Override
    public Graph convertDatasetToGraph(Dataset dataset) {
        Graph bootstrapG = CoreGraphFactory.createGraphInstance("normal");
        if (dataset.getClass().equals(CsvDataset.class)) {
            CSVBootstrap_with_DataFrame_MM_without_Jena bootstrap = new CSVBootstrap_with_DataFrame_MM_without_Jena(dataset.getDatasetId(), dataset.getDatasetName(), ((CsvDataset) dataset).getPath());
            try {
                bootstrapG = bootstrap.bootstrapSchema();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (dataset.getClass().equals(JsonDataset.class)) {
            JSONBootstrap_with_DataFrame_MM_without_Jena j = new JSONBootstrap_with_DataFrame_MM_without_Jena(dataset.getDatasetId(), dataset.getDatasetName(), ((JsonDataset) dataset).getPath());
            try {
                bootstrapG = j.bootstrapSchema();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return bootstrapG;
    }

}
