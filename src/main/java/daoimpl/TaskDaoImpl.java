package daoimpl;

import dao.ITask;
import domain.Task;
import jdbc.JDBCHelper;

import java.sql.ResultSet;

public class TaskDaoImpl implements ITask {
    @Override
    public Task getTaskById(int taskId) {
        JDBCHelper con = JDBCHelper.getInstance();
        String sql="select * from task where task_id=?";
        Object[] params={taskId};
        Task task=new Task();
        con.executeQuery(sql, params, new JDBCHelper.QueryCallBack() {
            @Override
            public void process(ResultSet resultSet)  {
                try {
                    if(resultSet.next()) {
                        task.setTaskId(resultSet.getInt(1));
                        task.setTaskName(resultSet.getString(2));
                        task.setCreateTime(resultSet.getString(3));
                        task.setStartTime(resultSet.getString(4));
                        task.setFinishTime(resultSet.getString(5));
                        task.setTaskType(resultSet.getString(6));
                        task.setTaskStatus(resultSet.getString(7));
                        task.setTaskParam(resultSet.getString(8));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        return task;
    }
}
