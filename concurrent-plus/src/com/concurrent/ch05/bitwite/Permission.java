package com.concurrent.ch05.bitwite;


/**
 * 位运算用在权限控制
 */
public class Permission {

    //增
    private static final int ADD = 1 << 0; // 0001
    //删
    private static final int DELETE = 1 << 1; // 0010
    //改
    private static final int UPDATE = 1 << 2; // 0100
    //查
    private static final int GET = 1 << 3; // 1000

    //权限
    private int per;

    //设置权限
    private void setPer(int flag) {
        per = flag;
    }

    //增加某些权限
    private void addPer(int flag) {
        per = per | flag;
    }

    //删除某些权限
    private void detelePer(int flag) {
        per = per & ~flag;
    }

    //判断用户是否拥有某些权限
    private boolean isAllow(int flag) {
        return ((flag & per) == flag);
    }

    //判断用户是否没有某些权限
    private boolean isNotAllow(int flag) {
        return ((per & flag) == 0);
    }

    public static void main(String[] args) {
        int flag = 15; // 1111
        Permission permission = new Permission();
        permission.setPer(flag);
        permission.detelePer(ADD | GET);
        System.out.println("select = " + permission.isAllow(GET));
        System.out.println("update = " + permission.isAllow(UPDATE));
        System.out.println("insert = " + permission.isAllow(ADD));
        System.out.println("delete = " + permission.isAllow(DELETE));
    }
}
