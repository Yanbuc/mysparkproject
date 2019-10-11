package myfunc;

import org.apache.spark.sql.api.java.UDF3;

public class Concat2Str implements UDF3<Integer,String,String,String> {
    @Override
    public String call(Integer s, String s2, String s3) throws Exception {
        return s+s3+s2;
    }
}
