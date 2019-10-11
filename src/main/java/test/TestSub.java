package test;

public class TestSub {
    public static void main(String[] args) {
        StringBuffer sb=new StringBuffer();
        sb.append("aaaa");
        sb.append(",");
        System.out.println(sb.length());
        System.out.println(sb.substring(0,sb.length()-1).getClass());
    }
}
