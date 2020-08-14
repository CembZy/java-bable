package ch01;

/**
 * 测试栈分配。
 * -server -Xmx10m -Xms10m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations -XX:+UseTLAB
 *
 * -server JVM运行的模式之一, server模式才能进行逃逸分析， JVM运行的模式还有mix/client
 * -Xmx10m和-Xms10m：堆的大小
 * -XX:+DoEscapeAnalysis：启用逃逸分析(1.8默认打开)
 * -XX:+PrintGC：打印GC日志
 * -XX:+EliminateAllocations：标量替换(默认打开)
 * -XX:-UseTLAB 关闭本地线程分配缓冲
 * TLAB： ThreadLocalAllocBuffer，具体解释参见下文《虚拟机中的对象---对象的分配----2）》
 * 对栈上分配发生影响的参数就是三个，-server、-XX:+DoEscapeAnalysis和-XX:+EliminateAllocations，
 * 任何一个发生变化都不会发生栈上分配，因为启用逃逸分析和标量替换默认是打开的，所以，在我们的例子中，
 * JVM的参数只用-server一样可以有栈上替换的效果。
 */
public class StackAlloc {

    public static class User {
        public int i = 0;
        public String name = "";
    }


    public static void main(String[] args) {
        long a = System.currentTimeMillis();

        for (int i = 0; i < 1000000000; i++) {
            User user = new User();
        }

        long b = System.currentTimeMillis();

        System.out.println((b - a) + "ms");
    }
}
