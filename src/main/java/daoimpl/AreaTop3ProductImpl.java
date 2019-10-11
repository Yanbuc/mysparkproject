package daoimpl;

import dao.IAreaTop3Product;
import domain.AreaTop3Product;
import jdbc.JDBCHelper;

public class AreaTop3ProductImpl implements IAreaTop3Product {
    @Override
    public void insert(AreaTop3Product data) {
        String sql="insert into area_top3_product values(?,?,?,?,?,?,?,?)";
        Object[] params={data.getTaskId(),data.getArea(),data.getAreaLevel(),data.getProduct_id(),
        data.getCityNames(),data.getClickCount(),data.getProductName(),data.getProductStatus()};
        JDBCHelper con = JDBCHelper.getInstance();
        con.executeUpdate(sql,params);
    }
}
