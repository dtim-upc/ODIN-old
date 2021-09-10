package edu.upc.essi.dtim.ODINBackend.services.omq.wrapper_implementations;

import edu.upc.essi.dtim.ODINBackend.config.DataSourceTypes;
import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import edu.upc.essi.dtim.ODINBackend.services.omq.WrapperI;
import lombok.Getter;
import lombok.Setter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Getter @Setter
public class CSV_Wrapper implements WrapperI<DataSource> {

    @Autowired
    private SparkSession spark;

//    private String path;
    private String columnDelimiter;
    private String rowDelimiter;
    private boolean headerInFirstRow;


    @Override
    public DataSourceTypes getType() {
        return DataSourceTypes.CSV;
    }

    @Override
    public List<String> inferSchema(DataSource dataSource) {

        Dataset<Row> ds = spark.read()
                .option("header",String.valueOf(true))
                .option("delimiter",",")
                .csv(dataSource.getPath());

        return Arrays.asList( ds.schema().fieldNames() ) ;
    }





}
