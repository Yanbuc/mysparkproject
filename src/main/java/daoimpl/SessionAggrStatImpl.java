package daoimpl;

import dao.ISessionAggrStat;
import domain.SessionAggrStat;
import jdbc.JDBCHelper;

public class SessionAggrStatImpl implements ISessionAggrStat {
    @Override
    public void insert(SessionAggrStat data) {
        String sql="insert into session_aggr_stat values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params={
           data.getTaskId(),data.getSessionCount(),data.getSession1_3(),data.getSession4_6(),
           data.getSession7_9(),data.getSession10_30(),data.getSession30_60(),data.getSession1m_3m(),
           data.getSession3m_10m(),data.getSession10m_30m(),data.getSession30m(),
           data.getStep1_3(),data.getStep4_6(),data.getStep7_9(),data.getStep10_30(),
           data.getStep30_60(),data.getStep60()
        };
        JDBCHelper con = JDBCHelper.getInstance();
        con.executeUpdate(sql,params);
    }
}
