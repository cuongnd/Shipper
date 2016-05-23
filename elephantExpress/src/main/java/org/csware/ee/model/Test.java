package org.csware.ee.model;

import org.csware.ee.consts.AreaItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yu on 2015/5/26.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Map<Integer,List<People>> map=new HashMap<Integer,List<People>>();
        People p1=new People(1,"2");
        addPersonToMap(map,p1);
        People p2=new People(3,"4");
        addPersonToMap(map,p2);
        People p3=new People(1,"2");
        addPersonToMap(map,p3);
        System.out.println(map.get(1));
    }
    private static boolean addPersonToMap(Map<Integer,List<People>> map,People p1){
        boolean flag;
        int key=p1.getAge();
        if(map.containsKey(key)){
            List<People> value=map.get(1);
            value.add(p1);
            map.put(key, value);
            flag=true;
        }else{
            List<People> value=new ArrayList<People>();
            value.add(p1);
            map.put(key, value);
            flag=false;
        }
        return flag;
    }


    void Test()
    {
        AreaItem[] as = new AreaItem[]{};


    }

}
class People{
    int age;
    String name;
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public People() {
        super();
    }
    public People(int age, String name) {
        super();
        this.age = age;
        this.name = name;
    }

}