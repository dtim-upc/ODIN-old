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
import org.springframework.stereotype.Service;

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

//    @Override
//    public String preview(List<String> attributes) throws Exception {
//        JSONArray data = new JSONArray();
//        Dataset<Row> ds = spark.read()
//                .option("header",String.valueOf(this.headerInFirstRow))
//                .option("delimiter",this.columnDelimiter)
//                .csv(this.path);
//        String tableName = UUID.randomUUID().toString().replace("-","");
//        ds.createTempView(tableName);
//
//        spark.sql("select "+String.join(",",
//                attributes.stream().filter(a->!a.isEmpty()).map(a -> "`"+a+"`")
//                        .collect(Collectors.toList()))+" from "+tableName+" limit 10")
//                .toJavaRDD()
//                .collect()
//                .forEach(r -> {
//                    JSONArray arr = new JSONArray();
//                    attributes.stream().filter(a->!a.isEmpty()).forEach(a -> {
//                        JSONObject datum = new JSONObject();
//                        datum.put("attribute",a);
//                        datum.put("value",String.valueOf(r.get(r.fieldIndex(a))));
//                        arr.add(datum);
//                    });
//                    data.add(arr);
//                });
//        spark.close();
//        JSONObject res = new JSONObject(); res.put("data",data);
//        return res.toJSONString();
//    }



}
