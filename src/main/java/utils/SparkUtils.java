package utils;

import com.alibaba.fastjson.JSONObject;
import conf.ConfigurationManager;
import constant.Constants;
import mockdata.MockData;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

import javax.security.auth.login.Configuration;

public class SparkUtils {
    // 设置SparkConf
    public static SparkConf produceSparkConf(String appName){
        boolean local = ConfigurationManager.getBoolean(Constants.LOCAL);
        SparkConf conf =new SparkConf().setAppName(appName);
        if(local){
            conf.setMaster("local");
            conf.set("spark.sql.warehouse.dir","D:/a");
        }else{
            conf.setMaster("yarn-cluster");
        }
        return conf;
    }

    // 设置SparkSession
    public static SparkSession produceSparkSession(SparkConf conf){
        boolean local = ConfigurationManager.getBoolean(Constants.LOCAL);
        if(local){
           return  SparkSession.builder().config(conf).getOrCreate();
        }
        return  SparkSession.builder().config(conf).enableHiveSupport().getOrCreate();
    }
    // 产生javaSparkContext
    public static JavaSparkContext produceJavaSparkContext(SparkSession sc){
        return JavaSparkContext.fromSparkContext(sc.sparkContext());
    }
    // 模拟产生数据
    public static void mockData(JavaSparkContext jsc, SQLContext sqlContext){
        MockData.mock(jsc,sqlContext);
    }

     public static JavaRDD<Row> getSessionRangeData(SparkSession sc, JSONObject jsonObject){
          String startTime=jsonObject.getString(Constants.START_DATE);
          String endTime=jsonObject.getString(Constants.END_DATE);
          //String sql="select * from user_visit_action where '"+startTime+"' >= date and date<='"+endTime+"'";
         String sql="select * from user_visit_action where date>= '"+startTime+"' and  date<= '"+endTime+"'";
         System.out.println(sql);
          Dataset<Row> tmp = sc.sql(sql);
          return tmp.javaRDD();
     }
}
