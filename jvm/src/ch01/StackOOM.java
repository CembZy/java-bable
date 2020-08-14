package ch01;


/**
 * 测试栈溢出
 */
public class StackOOM {
    static int i;

    /**
     * 一般的方法调用是很难出现的，如果出现了要考虑是否有无限递归。
     * 虚拟机栈带给我们的启示：方法的执行因为要打包成栈桢，所以天生要比实现同样功能的循环慢，
     * 所以树的遍历算法中：递归和非递归(循环来实现)都有存在的意义。递归代码简洁，非递归代码复杂但是速度较快。
     */
    public static void test1() {
        i++;
        test1();
    }

    public static void test2(int k,String j) {
        i++;
        test2(k,j);
    }

    public static void main(String[] args) {
        try {
//            test1();
            test2(1,"1");
        } catch (Throwable e) {
            System.out.println(i);
            e.printStackTrace();
        }
    }
}
