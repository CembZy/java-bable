package ch04;

import java.io.File;


/**
 * 自定义类加载器，用于加载解密之后的class
 */
public class CustomClassLoader extends ClassLoader {

    private final String name;
    private String basePath;
    private final static String FILE_EXT = ".class";

    public CustomClassLoader(String name) {
        this.name = name;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }


    //实际解密
    private byte[] loadClassData(String name) {
        int x = 0;
        byte[] data = null;
        Util demoEncryptUtil = new Util();
        // use x;
        int y = 1;
        try {
            String tempName = name.replaceAll("\\.", "\\\\");
            //获取解密之后的class
            data = demoEncryptUtil.decrypt(new File(basePath + tempName + FILE_EXT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return this.defineClass(name, data, 0, data.length);
    }
}
