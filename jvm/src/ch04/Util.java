package ch04;

import java.io.*;

/**
 * 类说明：加密和解密的服务类
 */
public class Util {

    //异或操作,可以进行加密和解密
    private void xor(InputStream in, OutputStream out) throws Exception {
        int ch;
        while ((ch = in.read()) != -1) {
            ch = ch ^ 0xff;
            out.write(ch);
        }
        in.close();
        out.close();
    }


    //加密方法
    public void encrypt(File src, File des) throws Exception {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(des);

        xor(in, out);
        in.close();
        out.close();
    }


    //解密方法
    //加密后的class文件
    public byte[] decrypt(File src) throws Exception {
        InputStream in = new FileInputStream(src);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        xor(in, bos);
        byte[] data = bos.toByteArray();

        return data;
    }


    public static void main(String[] args) throws Exception {
        File src = new File("E:\\project\\gitee\\project\\cemb-ssm\\jvm\\out\\production\\jvm\\ch04\\Src.class");
        File dest = new File("E:\\project\\gitee\\project\\cemb-ssm\\jvm\\out\\production\\jvm\\ch04\\Dest.class");
        Util demoEncryptUtil = new Util();
        demoEncryptUtil.encrypt(src, dest);
        System.out.println("加密完成！");
    }
}
