package product;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.FADD;
import conf.ConfigurationManager;
import constant.Constants;
import dao.IAreaTop3Product;
import domain.AreaTop3Product;
import factory.Daofactory;
import mockdata.MocksData;
import myfunc.Concat2Str;
import myfunc.GetJson;
import myfunc.Group_distinct_concat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.glassfish.jersey.internal.util.collection.DataStructures;
import scala.Tuple2;
import scala.collection.immutable.Stream;
import session.Category;
import utils.ParamsUtils;
import utils.SparkUtils;
import utils.StringUtils;

import javax.security.auth.login.Configuration;
import java.util.*;

public class AreaProductCalculate {

    public static JavaPairRDD<Long, Long> getTimeRangeData(JSONObject params,SparkSession sc){
        String startTime=params.getString(Constants.START_DATE);
        String endTime=params.getString(Constants.END_DATE);
        String sql="select city_id,click_product_id product_id from user_visit_action " +
                "  where date >= '"+startTime+"'  and date <= '"+endTime+"' "
                + "and click_product_id is not null";
        Dataset<Row> data = sc.sql(sql);
        JavaRDD<Row> tmp = data.javaRDD();
        JavaPairRDD<Long, Long> sessionDataKeyByCityId = tmp.mapToPair((line) -> {
            long cityId = line.getLong(0);
            long productId = line.getLong(1);
            return new Tuple2<Long, Long>(cityId, productId);
        });
        return sessionDataKeyByCityId;
    }

     // 将mysql之中的的数据转化成为一个spark sql的表格
    public static JavaPairRDD<Integer,String> getCityInfo(SparkSession sc){
        Map<String,String> options=new HashMap<String,String>();
        boolean local= ConfigurationManager.getBoolean(Constants.LOCAL);
        String user=local==true ? ConfigurationManager.getProperty(Constants.LOCAL_DB_USER):ConfigurationManager.getProperty(Constants.PRODUCT_DB_USER);
        String password= local==true?ConfigurationManager.getProperty(Constants.LOCAL_DB_PASSWORD):ConfigurationManager.getProperty(Constants.PRODUCT_DB_PASSWORD);
        String url=local==true?ConfigurationManager.getProperty(Constants.LOCAL_DB_URL):ConfigurationManager.getProperty(Constants.PRODUCT_DB_URL);
        String dbtable="city_info";
        options.put("user",user);
        options.put("password",password);
        options.put("url",url );
        options.put("dbtable",dbtable);
        Dataset<Row> cityInfo = sc.read().format("jdbc").options(options).load();
        JavaRDD<Row> tmp = cityInfo.javaRDD();
        return  tmp.mapToPair((row)->{
           int cityId=row.getInt(0);
           String cityName=row.getString(1);
           String area=row.getString(2);
           String line=Constants.CITY_NAME+"="+cityName+"|"+Constants.AREA_NAME+"="+area;
           return new Tuple2<Integer,String>(cityId,line);
        });
    }

    // 获得以 area cityId cityName product_id 四个字段的表格
    public static  void generateFullProductInfoTable(SparkSession sc,JavaSparkContext jsc,
                                                     JavaPairRDD<Long, Long> sessionDateKeyByCityId,
                                                     JavaPairRDD<Integer, String> cityInfo){
        List<Tuple2<Integer, String>> cityData = cityInfo.collect();
        Map<Integer,String> map=new HashMap<Integer, String>();
        for(Tuple2<Integer,String> one:cityData){
            map.put(one._1,one._2);
        }
        Broadcast<Map<Integer, String>> broadcast = jsc.broadcast(map);
        JavaRDD<String> fullProductInfo = sessionDateKeyByCityId.map((line) -> {
            int cityId = (int) ((long) line._1);
            long productId = line._2;
            Map<Integer, String> cityD = broadcast.value();
            if (!cityD.containsKey(cityId)) {
                return "0000";
            }
            String area = StringUtils.getFieldFromConcatString(cityD.get(cityId), "\\|", Constants.AREA_NAME);
            String cityName = StringUtils.getFieldFromConcatString(cityD.get(cityId), "\\|", Constants.CITY_NAME);
            return Constants.AREA_NAME + "=" + area + "|" + Constants.CITY_ID + "=" + cityId + "|"
                    + Constants.CITY_NAME + "=" + cityName + "|" + Constants.PRODUCT_ID + "=" + productId;
        });
        fullProductInfo=fullProductInfo.filter((line)->{
            return line.equals("0000") ? false:true;
        });
        JavaRDD<Row> fullProductInfoRdd = fullProductInfo.map((line) -> {
            String area = StringUtils.getFieldFromConcatString(line, "\\|", Constants.AREA_NAME);
            int cityId = Integer.valueOf(StringUtils.getFieldFromConcatString(line, "\\|", Constants.CITY_ID));
            String cityName = StringUtils.getFieldFromConcatString(line, "\\|", Constants.CITY_NAME);
            long productId = Long.valueOf(StringUtils.getFieldFromConcatString(line, "\\|", Constants.PRODUCT_ID));
            return RowFactory.create(area, cityId, cityName, productId);
        });
        List<StructField> fields=new ArrayList<StructField>();
        fields.add(DataTypes.createStructField("area", DataTypes.StringType, false));
        fields.add(DataTypes.createStructField("city_id", DataTypes.IntegerType, false));
        fields.add(DataTypes.createStructField("city_name", DataTypes.StringType, false));
        fields.add(DataTypes.createStructField("product_id", DataTypes.LongType, false));
        StructType  scheme=DataTypes.createStructType(fields);
        Dataset<Row> dataFrame = sc.createDataFrame(fullProductInfoRdd, scheme);
        dataFrame.registerTempTable("tmp_full_product_info");
    }

    // 获得每个区域的点击量最高的产品
    public  static void getAreaTop3Product(SparkSession sc){

        String sql=" select * from " +
                "( select area,product_id,cnt,line, row_number() over(partition by area order by cnt desc ) as num " +
                "from    " +
                "(select area,product_id,count(*) as cnt , group_distinct(fields_concat(city_id,city_name,':')) as line from tmp_full_product_info" +
                " group by area,product_id ) t) o where o.num <=3";
        Dataset<Row> data = sc.sql(sql);
        data.registerTempTable("tmp_area_city_product");
    }

    public static JavaRDD<Row> getFullProductData(SparkSession sc){

        String sql="select  a.area area, " +
                " case " +
                " when `a`.`area`='China North' or `a`.`area`='China East' then 'A级' " +
                " when `a`.`area`='China South' or `a`.`area`='China Middle' then 'B级' " +
                " when `a`.`area`=' West South' or `a`.`area`=' West North' then 'C级' " +
                " else 'D级' end `area_level` , " +
                " a.product_id product_id ,a.cnt cnt,a.line line," +
                "  if(get_json(b.extend_info,\"product_status\")==\"1\",\"第三方\",\"自营\") as info,b.product_name product_name " +
                " from tmp_area_city_product a join product_info b" +
                "  on a.product_id=b.product_id ";


        Dataset<Row> data = sc.sql(sql);
         return data.javaRDD();
    }

    public static void insertData(JavaRDD<Row> data,final int taskId){
        data.foreachPartition((rows)->{
            while (rows.hasNext()){
                Row tmp=rows.next();
                String area=tmp.getString(0);
                String areaLevel=tmp.getString(1);
                long productId=tmp.getLong(2);
                int clickCount=(int)((long)tmp.getLong(3));
                String cityNames=tmp.getString(4);
                String productStatus=tmp.getString(5);
                String productName=tmp.getString(6);
                IAreaTop3Product iAreaTop3Product= Daofactory.getIAreaTop3Product();
                iAreaTop3Product.insert(new AreaTop3Product(taskId,area,areaLevel,productId,cityNames,clickCount,productName,productStatus));
            }
        });
    }


    public static void main(String[] args) {
        SparkConf conf= SparkUtils.produceSparkConf("areaProduct");
        //注册序列化对象
        conf.registerKryoClasses(new Class[]{Category.class});
        SparkSession sc=SparkUtils.produceSparkSession(conf);
        JavaSparkContext jsc=SparkUtils.produceJavaSparkContext(sc);
        //注册函数：
        sc.udf().register("group_distinct",new Group_distinct_concat());
        sc.udf().register("fields_concat",new Concat2Str(),DataTypes.StringType);
        sc.udf().register("get_json",new GetJson(),DataTypes.StringType);
        SQLContext sqlContext=sc.sqlContext();
        MocksData.mock(jsc,sqlContext);
        int taskId=4;
        JSONObject taskParmas = ParamsUtils.getTaskParmas(taskId);
        JavaPairRDD<Long, Long> sessionDateKeyByCityId = getTimeRangeData(taskParmas, sc);
        JavaPairRDD<Integer, String> cityInfo = getCityInfo(sc);
        generateFullProductInfoTable(sc,jsc,sessionDateKeyByCityId,cityInfo);
        getAreaTop3Product(sc);
        JavaRDD<Row> data=getFullProductData(sc);
        insertData(data,taskId);
    }
}
