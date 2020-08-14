package ch02;


import java.lang.ref.WeakReference;

/**
 * 测试弱引用，一些有用（程度比软引用更低）但是并非必需，只能生存到下一次垃圾回收之前，GC发生时，
 * 不管内存够不够，都会被回收。二级缓存可以使用弱引用
 */
public class WeakReferenceTest {

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

        //弱引用,执行user所指向的User堆内存
        WeakReference<User> reference = new WeakReference<>(user);

        //将user对象指向User堆内存设置为null
        user = null;

        //因为是弱引用，所以还未被GC，此时还是可以取到user
        System.out.println(reference.get());

        //开启垃圾回收,弱引用一定会被回收
        System.gc();

        //取不到
        System.out.println(reference.get());

    }
}
