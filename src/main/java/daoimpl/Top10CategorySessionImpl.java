package daoimpl;

import dao.ITop10CategorySession;
import domain.Top10CategorySession;
import jdbc.JDBCHelper;

import java.util.ArrayList;
import java.util.List;

public class Top10CategorySessionImpl implements ITop10CategorySession {
    @Override
    public void insertBatch(List<Top10CategorySession> data) {
        String sql="insert into top10_category_session values(?,?,?,?)";
        List<Object[]> params=new ArrayList<Object[]>();
        for(Top10CategorySession a:data){
            Object[] p={a.getTaskId(),a.getCategoryId(),a.getSessionId(),a.getClickCount()};
            params.add(p);
        }
        JDBCHelper conn = JDBCHelper.getInstance();
        conn.executeBatch(sql,params);
    }

    @Override
    public void insert(Top10CategorySession data) {
        String sql="insert into top10_category_session values(?,?,?,?)";
        Object[] params={data.getTaskId(),data.getCategoryId(),data.getSessionId(),data.getClickCount()};
        JDBCHelper conn= JDBCHelper.getInstance();
        int n=conn.executeUpdate(sql,params);
        System.out.println(n);
    }
}
