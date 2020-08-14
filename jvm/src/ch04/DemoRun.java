package ch04;


/**
 * 测试
 */
public class DemoRun {

    public static void main(String[] args) throws Exception {
        CustomClassLoader demoCustomClassLoader
                = new CustomClassLoader("My ClassLoader");
        demoCustomClassLoader.setBasePath("E:\\project\\gitee\\project\\cemb-ssm\\jvm\\out\\production\\jvm\\");
        Class<?> clazz =
                demoCustomClassLoader.findClass("ch04.Src");
        System.out.println(clazz.getClassLoader());
        Object o = clazz.newInstance();
        System.out.println(o);
        //new User(xxx,yyyy,ddd);//
    }
}