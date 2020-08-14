package ch02;


import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.List;

/**
 * 测试软引用，一些有用但是并非必需，用软引用关联的对象，系统将要发生OOM之前，这些对象就会被回收
 * -Xmx5m -Xms5m -XX:+PrintGC
 */
public class SoftReferenceTest {

    public static class User {
        private int age;

        public User(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "age=" + age +
                    '}';
        }
    }


    public static void main(String[] args) {

        User user = new User(1);

        //软引用,执行user所指向的User堆内存
        SoftReference<User> reference = new SoftReference<>(user);

        //将user对象指向User堆内存设置为null
        user = null;

        //因为是软引用，所以还未被GC，此时还是可以取到user
        System.out.println(reference.get());

        //开启垃圾回收,弱引用不一定会被回收
        System.gc();

        //还是会取到
        System.out.println(reference.get());

        List list = new LinkedList();
        for (int i = 0; i < 100; i++) {
            try {
                list.add(new byte[1024 * 1024 * 1]);
            } catch (Throwable throwable) {

                //只有在OOM之前，才会被回收，此时取不到user
                System.out.println(reference.get());
            }
        }

    }
}
