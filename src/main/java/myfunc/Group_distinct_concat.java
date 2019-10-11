package myfunc;


import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.MutableAggregationBuffer;
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;


import javax.xml.crypto.Data;
import java.util.Arrays;

public class Group_distinct_concat extends UserDefinedAggregateFunction {

    private StructType inputSchema= DataTypes.createStructType(Arrays.asList(
            DataTypes.createStructField("city_info",DataTypes.StringType,false)
    ));

    private StructType bufferSchema=DataTypes.createStructType(Arrays.asList(
            DataTypes.createStructField("city_tmp",DataTypes.StringType,false)
    ));

    private DataType dataType=  DataTypes.StringType;

    private boolean deterministic=true;
    public Group_distinct_concat() {
        super();
    }

    @Override
    public StructType inputSchema() {
        return inputSchema;
    }

    @Override
    public StructType bufferSchema() {
        return bufferSchema;
    }

    @Override
    public DataType dataType() {
        return dataType;
    }

    @Override
    public boolean deterministic() {
        return deterministic;
    }

    @Override
    public void initialize(MutableAggregationBuffer buffer) {
        buffer.update(0,"");
    }

    @Override
    public void update(MutableAggregationBuffer buffer, Row input) {
          String cityInfo=buffer.getString(0);
          String val=input.getString(0);
          if(cityInfo.equals("")){
              cityInfo=val;
          }else{
              if(!cityInfo.contains(val)){
                  cityInfo=cityInfo+","+val;
              }
          }
          buffer.update(0,cityInfo);
    }

    @Override
    public void merge(MutableAggregationBuffer buffer1, Row buffer2) {
            String cityInfo=buffer1.getString(0);
            String city=buffer2.getString(0);
            String[] fields=city.split(",");
            if(fields.length>0){
                if(cityInfo.equals("")){
                    cityInfo=city;
                }else{
                    System.out.println(cityInfo);
                    for(String s:fields){
                        if(!cityInfo.contains(s)){
                            cityInfo=cityInfo+","+s;
                        }
                    }
                }

            }
            buffer1.update(0,cityInfo);
    }

    @Override
    public Object evaluate(Row buffer) {
        return buffer.get(0);
    }





}
