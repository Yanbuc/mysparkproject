package test;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import utils.SparkUtils;

public class TestSparkUtils {
    public static void main(String[] args) {
        SparkConf conf = SparkUtils.produceSparkConf("test");
        SparkSession ssc =SparkUtils.produceSparkSession(conf);
    }
}
