package test;

import jdbc.JDBCHelper;

import java.sql.ResultSet;

public class TestJdbc {
    public static  void main(String[] args){

        JDBCHelper helper = JDBCHelper.getInstance();
        /*
        String sql="insert into test(name,age) values(?,?)";
        List<Object[]> lists=new ArrayList<Object[]>();
        lists.add(new Object[]{"as",200});
        lists.add(new Object[]{"bbb",300});
        helper.executeBatch(sql,lists);
        */
        String sql="select * from test";
        helper.executeQuery(sql, null, new JDBCHelper.QueryCallBack() {
            @Override
            public void process(ResultSet resultSet){
                try {
                    while (resultSet.next()){
                      System.out.println(resultSet.getInt(1)+"  "+resultSet.getString(2)+"  "+resultSet.getString(3));
                    }
                }catch (Exception  a){

                }

            }
        });
    }
}
