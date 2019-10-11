package test;

import java.util.Calendar;
import java.util.Date;

public class TestDate {
    public static void main(String[] args) {
        Date a=new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(a);
        System.out.println(c.get(Calendar.HOUR_OF_DAY));
    }
}
