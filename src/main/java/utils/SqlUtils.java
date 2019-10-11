package utils;

import com.alibaba.fastjson.JSONObject;
import constant.Constants;
import scala.Int;

public class SqlUtils {
    public static String produceSql(JSONObject params){
        StringBuffer sb=new StringBuffer();
        String startDate=params.getString(Constants.START_DATE);
        if(startDate!=""|| startDate!=null){
            sb.append("  ");
            sb.append("date>="+"'"+startDate+"'");
            sb.append("  and");
        }
        String endDate=params.getString(Constants.END_DATE);
        if(endDate!=null || endDate!=""){
            sb.append("  ");
            sb.append("date<= '"+endDate+"'");
            sb.append("  and");
        }
        /*
        Integer startAge=params.getInteger(Constants.START_AGE);
        if(startAge !=null ){
            sb.append("  ");
            sb.append("= "+(int)startAge+"");
            sb.append("  and");

        }
        Integer endAge=params.getInteger(Constants.END_AGE);
        if(endAge !=null ){
            sb.append("  ");
            sb.append(Constants.END_AGE+"= "+(int)endAge+"");
            sb.append("  and");

        }
        */
        String tmp=sb.toString();
        return tmp.substring(0,tmp.length()-3);
    }
}
