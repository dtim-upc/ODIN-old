package edu.upc.essi.dtim.ODINBackend.services.omq;

import edu.upc.essi.dtim.ODINBackend.config.DataSourceTypes;
import edu.upc.essi.dtim.ODINBackend.models.mongo.DataSource;

import java.util.List;

public interface WrapperI<T extends DataSource> {



    public DataSourceTypes getType();

    public List<String> inferSchema(T datasource);
}
