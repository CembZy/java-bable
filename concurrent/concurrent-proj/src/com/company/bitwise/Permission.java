package com.company.bitwise;


/**
 * 用位运算实现权限控制，还可以用在电商，或者物品类型比较多的情况下。
 */
public class Permission {

    //增加的权限
    private static final int ADD = 1 << 0;//0001
    //删除的权限
    private static final int DELETE = 1 << 1;//0010
    //修改的权限
    private static final int UPDATE = 1 << 2;//0100
    //查询的权限
    private static final int QUERY = 1 << 3;//1000

    //目前的权限状态
    private int flag;

    //添加权限
    public void setFlag(int per) {
        this.flag = per;
    }

    //增加权限(1个或者多个，多个的时候参数传入 per1|per2 ）
    public void enable(int per) {
        flag = flag | per;
    }

    //删除权限(1个或者多个）
    public void disable(int per) {
        flag = flag & ~per;
    }

    //判断用户拥有某个权限
    public boolean isAllow(int per) {
        return (flag & per) == per;
    }

    //判断用户没有某个权限
    public boolean isNotAllow(int per) {
        return (flag & per) != per;
    }


    public static void main(String[] args) {
        int flag = 15;
        Permission permission = new Permission();
        permission.setFlag(flag);
        permission.disable(ADD | UPDATE);
        System.out.println("add = " + permission.isAllow(ADD));
        System.out.println("update = " + permission.isAllow(UPDATE));
        System.out.println("delete = " + permission.isAllow(DELETE));
        System.out.println("query = " + permission.isAllow(QUERY));


    }

}
