package edu.upc.essi.dtim.odin.services.omq.wrapper_implementations;

import edu.upc.essi.dtim.odin.config.DataSourceTypes;
import edu.upc.essi.dtim.odin.bootstrapping.DataSource;
import edu.upc.essi.dtim.odin.services.omq.WrapperI;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.compress.utils.Sets;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@Getter @Setter
public class JSONWrapper implements WrapperI<DataSource> {

    @Autowired
    private SparkSession spark;

    private List<String> explodeLevels;
    private String arrayOfValues;
    private String attributeForSchema;
    private String valueForAttribute;
    private String copyToParent;


    @Override
    public DataSourceTypes getType() {
        return DataSourceTypes.JSON;
    }


    @Override
    public List<String> inferSchema(DataSource dataSource) {

        Dataset<Row> ds = spark.read().json(dataSource.getPath());
        ds.createOrReplaceTempView("inference");
        Set<String> attributes = Sets.newHashSet();
        extractAttributes(attributes,"",(JSONObject) JSONValue.parse(spark.sql(generateSparkSQLQuery("inference")).schema().json()));

        String[] res = attributes.toArray(new String[0]);

        return Arrays.asList( res );
    }

    private String generateSparkSQLQuery(String tableName) {
        return "select * from ("+ tableName + ")";
    }

    public void extractAttributes(Set<String> attributes, String parent, JSONObject jsonSchema) {
        if (jsonSchema.get("type") instanceof String && jsonSchema.get("type").equals("struct") ||
                jsonSchema.get("type") instanceof JSONObject && ((JSONObject) jsonSchema.get("type")).get("type").equals("struct")) {
            if (jsonSchema.containsKey("fields")) {
                ((JSONArray)jsonSchema.get("fields")).forEach(f -> {
                    extractAttributes(attributes,jsonSchema.containsKey("name") ?
                            (!parent.isEmpty() ? parent + /*"." +*/ jsonSchema.getAsString("name") : jsonSchema.getAsString("name"))
                            : parent,((JSONObject)f));
                });
            }
            else if (jsonSchema.containsKey("type")) {
                ((JSONArray)((JSONObject)jsonSchema.get("type")).get("fields")).forEach(f -> {
                    extractAttributes(attributes,jsonSchema.containsKey("name") ?
                            (!parent.isEmpty() ? parent + /*"." +*/ jsonSchema.getAsString("name") : jsonSchema.getAsString("name"))
                            : parent,((JSONObject)f));
                });
            }
        }
        else if (!jsonSchema.get("type").equals("array")) {
            attributes.add(parent + /*"." +*/ jsonSchema.getAsString("name"));
        }

    }
}
