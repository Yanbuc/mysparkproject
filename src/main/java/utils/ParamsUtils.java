package utils;

import com.alibaba.fastjson.JSONObject;
import dao.ITask;
import domain.Task;
import factory.Daofactory;

public class ParamsUtils {

    public static JSONObject strToJsonObject(String str){
        JSONObject jsonObject = JSONObject.parseObject(str);
        return jsonObject;
    }
    // 获得任务的过滤参数 转化为key val的形式
    public static JSONObject getTaskParmas(int taskId){
        ITask iTask= Daofactory.getTaskImpl();
        Task taskById = iTask.getTaskById(taskId);
        JSONObject params= ParamsUtils.strToJsonObject(taskById.getTaskParam());
        return params;
    }
}
