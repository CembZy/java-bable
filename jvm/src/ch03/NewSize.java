package ch03;


/**
 * 测试新生代配置的JVM参数
 * -Xms20M -Xmx20M -XX:+PrintGCDetails –Xmn2m -XX:SurvivorRatio=2
 * 没有垃圾回收
 * 数组都在老年代
 *
 * -Xms20M -Xmx20M -XX:+PrintGCDetails -Xmn7m -XX:SurvivorRatio=2
 * 发生了垃圾回收
 * 新生代存了部分数组，老年代也保存了部分数组，发生了晋升现象
 *
 * -Xms20M -Xmx20M -XX:+PrintGCDetails -Xmn15m -XX:SurvivorRatio=8
 * 新生代可以放下所有的数组
 * 老年代没放
 *
 * -Xms20M -Xmx20M -XX:+PrintGCDetails -XX:NewRatio=2
 * 发生了垃圾回收
 * 出现了空间分配担保，而且发生了FullGC
 */
public class NewSize {

    public static void main(String[] args) {
        int cap = 1 * 1024 * 1024;//1M
        byte[] b1 = new byte[cap];
        byte[] b2 = new byte[cap];
        byte[] b3 = new byte[cap];
        byte[] b4 = new byte[cap];
        byte[] b5 = new byte[cap];
        byte[] b6 = new byte[cap];
        byte[] b7 = new byte[cap];
        byte[] b8 = new byte[cap];
        byte[] b9 = new byte[cap];
        byte[] b0 = new byte[cap];
//		for(int i=0;i<10;i++) {
//			b = new byte[1*1024*1024];
//		}
    }
}
