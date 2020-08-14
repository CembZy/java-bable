package ch01;

import java.util.LinkedList;
import java.util.List;

/**
 * 测试方法区溢出。
 * -XX:MaxMetaspaceSize=3M
 */
public class MetaSacpe {


    public static void main(String[] args) {
        List list = new LinkedList();
        for(;;){
            list.add(new Object());
        }

    }
}
