package test;

import java.util.TreeMap;

public class TestTreeMap {
    public static void main(String[] args) {
        TreeMap<Long,String> map =new TreeMap<Long,String>();
         map.put(1L,"12");
         map.put(2L,"34");
         map.put(1L,"89");
         System.out.println(map.get(map.firstKey()));
    }
}
