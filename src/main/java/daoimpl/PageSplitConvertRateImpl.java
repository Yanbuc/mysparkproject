package daoimpl;

import dao.IPageSplitConvertRate;
import domain.PageSplitConvertRate;
import jdbc.JDBCHelper;

public class PageSplitConvertRateImpl implements IPageSplitConvertRate {
    @Override
    public void insert(PageSplitConvertRate data) {
        String sql="insert into page_split_convert_rate values(?,?)";
        Object[] params={data.getTaskId(),data.getConvertRate()};
        JDBCHelper con = JDBCHelper.getInstance();
        con.executeUpdate(sql,params);
    }
}
