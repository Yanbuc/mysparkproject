package daoimpl;

import dao.ITop10Category;
import domain.Top10Category;
import jdbc.JDBCHelper;

import java.util.ArrayList;
import java.util.List;

public class Top10CategoryImpl implements ITop10Category {
    @Override
    public void insert(List<Top10Category> data) {
        String sql="insert into top10_category values(?,?,?,?,?)";
        List<Object[]> params=new ArrayList<Object[]>();
        for(Top10Category tmp:data){
            Object[] p={tmp.getTaskId(),tmp.getCategoryId(),tmp.getClickCategoryCount(),
            tmp.getOrderCategoryCount(),tmp.getPayCategoryCount()};
            params.add(p);
        }
        JDBCHelper con = JDBCHelper.getInstance();
        con.executeBatch(sql,params);
    }
}
