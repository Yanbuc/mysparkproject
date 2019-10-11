package myfunc;

import com.alibaba.fastjson.JSONObject;
import org.apache.spark.sql.api.java.UDF2;

public class GetJson implements UDF2<String,String,String> {
    @Override
    public String call(String s, String key) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(s);
        return jsonObject.getString(key);
    }
}
