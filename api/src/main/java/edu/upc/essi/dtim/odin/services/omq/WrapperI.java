package edu.upc.essi.dtim.odin.services.omq;

import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.models.mongo.DataSource;

import java.util.List;

public interface WrapperI<T extends DataSource> {



    public DataSourceTypes getType();

    public List<String> inferSchema(T datasource);
}
