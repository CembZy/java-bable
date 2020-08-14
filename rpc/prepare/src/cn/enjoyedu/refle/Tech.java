package cn.enjoyedu.refle;

/**
 * 类说明：技师
 */
public class Tech {
    //洗脚服务
    public boolean XJ(String name) {
        System.out.println("13号技师为("+name+")服务");
        return true;
    }

    @Override
    public String toString() {
        return "这是一名技师";
    }
}
