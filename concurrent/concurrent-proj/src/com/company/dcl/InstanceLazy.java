package com.company.dcl;


/**
 * 类说明：使用内部类实例化对象，可以替换单例，这样当外部类创建的时候不会在内存中实例匿名内部类，
 * 只有当调用的时候才会去初始化
 */
public class InstanceLazy {

    private Integer value;
    private Integer val;//可能很大，如巨型数组1000000;

    public InstanceLazy(Integer value) {
        super();
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    private static class ValHolder {
        public static Integer vHolder = new Integer(1000000);
    }

    //返回的时候用这种方式，避免安全问题
    public Integer getVal() {
        return ValHolder.vHolder;
    }

}
