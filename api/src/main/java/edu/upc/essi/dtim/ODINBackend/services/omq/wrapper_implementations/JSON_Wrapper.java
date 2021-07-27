package edu.upc.essi.dtim.ODINBackend.services.omq.wrapper_implementations;

import edu.upc.essi.dtim.ODINBackend.models.DataSource;
import edu.upc.essi.dtim.ODINBackend.services.omq.Wrapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.compress.utils.Sets;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.List;
import java.util.Set;

public class JSON_Wrapper extends Wrapper {
    private String path;
    private List<String> explodeLevels;
    private String arrayOfValues;
    private String attributeForSchema;
    private String valueForAttribute;
    private String copyToParent;

    public JSON_Wrapper(String name) {
        super(name);
    }
    public JSON_Wrapper(DataSource ds, String queryParameters) {
        super("preview");
        this.path = ds.getJson_path();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getExplodeLevels() {
        return explodeLevels;
    }

    public void setExplodeLevels(List<String> explodeLevels) {
        this.explodeLevels = explodeLevels;
    }

    public String getArrayOfValues() {
        return arrayOfValues;
    }

    public void setArrayOfValues(String arrayOfValues) {
        this.arrayOfValues = arrayOfValues;
    }

    public String getAttributeForSchema() {
        return attributeForSchema;
    }

    public void setAttributeForSchema(String attributeForSchema) {
        this.attributeForSchema = attributeForSchema;
    }

    public String getValueForAttribute() {
        return valueForAttribute;
    }

    public void setValueForAttribute(String valueForAttribute) {
        this.valueForAttribute = valueForAttribute;
    }

    public String getCopyToParent() {
        return copyToParent;
    }

    public void setCopyToParent(String copyToParent) {
        this.copyToParent = copyToParent;
    }
    @Override
    public String[] inferSchema() throws Exception {
        SparkSession spark = SparkSession.builder()
                .appName("parquetPreview")
                .master("local[*]")
                .config("spark.driver.bindAddress","localhost")
                .config("spark.driver.memory","471859200")
                .config("spark.testing.memory", "471859200")
                .getOrCreate();
        Dataset<Row> ds = spark.read().json(this.path);
        ds.createOrReplaceTempView("inference");
        Set<String> attributes = Sets.newHashSet();
        extractAttributes(attributes,"",(JSONObject) JSONValue.parse(spark.sql(generateSparkSQLQuery("inference")).schema().json()));

        String[] res = attributes.toArray(new String[0]);

        /*
        JSONObject res = new JSONObject();
        res.put("schema", new Gson().toJson(attributes));
         */
        spark.close();
        //return res.toJSONString();
        //return super.inferSchema();
        return res;
    }

    private String generateSparkSQLQuery(String tableName) {
        return "select * from ("+ tableName + ")";
    }

    public static void extractAttributes(Set<String> attributes, String parent, JSONObject jsonSchema) {
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
