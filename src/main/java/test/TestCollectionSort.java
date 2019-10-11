package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestCollectionSort {
    public static void main(String[] args) {
        Person a=new Person("zhasnsa",12);
        Person b=new Person("lisi",23);
        Person c=new Person("wangwu",3);
        List<Person> list =new ArrayList<Person>();
        list.add(a);
        list.add(b);
        list.add(c);
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge()-o2.getAge();
            }
        });
        for(Person p:list){
            System.out.println(p.getAge());
        }
    }
}
