package daoimpl;

import dao.ISessionRandomExtract;
import domain.SessionRandomExtract;
import jdbc.JDBCHelper;

import java.util.ArrayList;
import java.util.List;

public class SessionRandomExtractImpl implements ISessionRandomExtract {
    @Override
    public void insert(List<SessionRandomExtract> data) {
        String sql="insert into session_random_extract values(?,?,?,?,?)";
        JDBCHelper con = JDBCHelper.getInstance();
        List<Object[]> params=new ArrayList<Object[]>();
        for(SessionRandomExtract tmp:data){
            Object[] p={tmp.getTaskId(),tmp.getSessionId(),tmp.getStartTime(),tmp.getSearchKeyWords(),tmp.getClickCatgeoryIds()};
            params.add(p);
        }
        con.executeBatch(sql,params);
    }
}
