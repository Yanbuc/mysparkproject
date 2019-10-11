package page;

import com.alibaba.fastjson.JSONObject;
import constant.Constants;
import dao.IPageSplitConvertRate;
import domain.PageSplitConvertRate;
import factory.Daofactory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.sources.In;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;
import scala.tools.nsc.interactive.tests.InteractiveTest;
import session.Category;
import utils.DateUtils;
import utils.NumberUtils;
import utils.ParamsUtils;
import utils.SparkUtils;

import java.lang.invoke.ConstantCallSite;
import java.util.*;


public class PageOneStepConvertRate {
    public static JavaPairRDD<String, Iterable<Row>> getDataBySessionId(JavaRDD<Row> data){
        JavaPairRDD<String, Row> sessionData = data.mapToPair((row) -> {
            String sessionId = row.getString(2);
            return new Tuple2<String, Row>(sessionId, row);
        });
        return sessionData.groupByKey();
    }

    public static  String[] calculatePageSplit(JSONObject params){
        String targetPageFlow=params.getString(Constants.TAEGET_PAGE_FLOW);
        String[] pages=targetPageFlow.split(",");
        String[] pageSplit=new String[pages.length-1];
        for(int i=0;i<pageSplit.length;i++){
            pageSplit[i]=pages[i]+"_"+pages[i+1];
        }
        return pageSplit;
    }
    public static  JavaPairRDD<String, Integer> generateAndMatchPageSplit(JavaSparkContext jsc,JavaPairRDD<String, Iterable<Row>> data,String[] pageSplit){
        Broadcast<String[]> broadcast = jsc.broadcast(pageSplit);
        JavaPairRDD<String, Integer> pageSplitData = data.flatMapToPair((line) -> {
            List<Tuple2<String, Integer>> count = new ArrayList<Tuple2<String, Integer>>();
            Iterator<Row> actions = line._2.iterator();
            List<Row> rows = new ArrayList<Row>();
            while (actions.hasNext()) {
                rows.add(actions.next());
            }
            Collections.sort(rows, new Comparator<Row>() {
                @Override
                public int compare(Row o1, Row o2) {
                    Date one = DateUtils.parseTime(o1.getString(4));
                    Date two = DateUtils.parseTime(o2.getString(4));
                    return one.getTime() > two.getTime() ? 1 : -1;
                }
            });
            String[] sp = broadcast.value();
            long lastPage = -1;
            for (Row r : rows) {
                long pageId = r.getLong(3);
                if (lastPage == -1) {
                    lastPage = pageId;
                    continue;
                }
                String pagesp = lastPage + "_" + pageId;
                for (int i = 0; i < sp.length; i++) {
                    if (sp[i].equals(pagesp)) {
                        count.add(new Tuple2<String, Integer>(pagesp, 1));
                        break;
                    }
                }
                lastPage = pageId;
            }
            return count.iterator();
        });
        return pageSplitData.reduceByKey((Integer a,Integer b)->{
            return a+b;
        });
    }

    public static  long calculateStartPageCount(JavaSparkContext jsc,long startPage,JavaPairRDD<String, Iterable<Row>> data){
        Broadcast<Long> broadcast = jsc.broadcast(startPage);
        JavaPairRDD<Long, Integer> tmp = data.flatMapToPair((line) -> {
            List<Tuple2<Long, Integer>> params = new ArrayList<Tuple2<Long, Integer>>();
            Iterator<Row> rows = line._2.iterator();
            long sp = broadcast.value();
            while (rows.hasNext()) {
                Row t = rows.next();
                Long p = t.getLong(3);
                if (sp == p) {
                    params.add(new Tuple2<Long, Integer>(sp, 1));
                }
            }
            return params.iterator();
        });
        return tmp.count();
    }

    public static long getStartPageId(String[] pageSplit){
        return Long.valueOf(pageSplit[0].split("_")[0]);
    }

    // 计算页面的转化比例
    public static Map<String,Double> calculatePageConvertRate(long startPageCount,String[] pageSplit,JavaPairRDD<String,Integer> data){
        List<Tuple2<String, Integer>> collect = data.collect();
        Map<String,Integer> tmp=new HashMap<String, Integer>();
        for(Tuple2<String,Integer> a:collect){
            tmp.put(a._1,a._2);
        }
        Map<String,Double> pageConvert=new HashMap<String, Double>();
        double convertRate=0.0;
        for(String s:pageSplit){
            convertRate= NumberUtils.formatDouble(((double)tmp.get(s)) /startPageCount,2);
            pageConvert.put(s,convertRate);
            startPageCount=tmp.get(s);
        }
        return pageConvert;
    }

    public static  void insertPageConvertRate(int taskId,Map<String,Double> pageConvertRate){
        StringBuffer sb=new StringBuffer();
        for(Map.Entry<String,Double> s:pageConvertRate.entrySet()){
            sb.append(s.getKey()+"="+s.getValue()+"|");
        }
        String convertRate=sb.substring(0,sb.length()-1);
        PageSplitConvertRate p=new PageSplitConvertRate(taskId,convertRate);
        IPageSplitConvertRate iPageSplitConvertRate= Daofactory.getIPageSplitConvertRate();
        iPageSplitConvertRate.insert(p);
    }


    public static void main(String[] args){
        SparkConf conf= SparkUtils.produceSparkConf("userSessionAnalize");
        //注册序列化对象
        conf.registerKryoClasses(new Class[]{Category.class});
        SparkSession sc=SparkUtils.produceSparkSession(conf);
        JavaSparkContext jsc=SparkUtils.produceJavaSparkContext(sc);

        SQLContext sqlContext=sc.sqlContext();
        // 生成模拟数据
        SparkUtils.mockData(jsc,sqlContext);
        int taskId=2;
        JSONObject taskParmas = ParamsUtils.getTaskParmas(taskId);
        JavaRDD<Row> sessionRangeData= SparkUtils.getSessionRangeData(sc,taskParmas);
        JavaPairRDD<String, Iterable<Row>> dataKeyBySessionId = getDataBySessionId(sessionRangeData);

        dataKeyBySessionId=dataKeyBySessionId.persist(StorageLevel.MEMORY_ONLY());
        String[] pageSplit=calculatePageSplit(taskParmas);
        // 获得用户访问的页面切片
        JavaPairRDD<String, Integer> pageSplitData = generateAndMatchPageSplit(jsc, dataKeyBySessionId, pageSplit);
        long startPageId=getStartPageId(pageSplit);
        long startPageCount = calculateStartPageCount(jsc, startPageId, dataKeyBySessionId);
        Map<String,Double> pageConvertRate=calculatePageConvertRate(startPageCount,pageSplit,pageSplitData);
        insertPageConvertRate(taskId,pageConvertRate);
    }
}
