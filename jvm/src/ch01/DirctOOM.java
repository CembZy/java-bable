package ch01;

import java.nio.ByteBuffer;

/**
 * 模拟直接内存溢出，直接内存一般只有在NIO通讯的时候会分配。
 * <p>
 * -Xmx10m -XX:MaxDirectMemorySize=10m
 */
public class DirctOOM {

    public static void main(String[] args) {
        //设定为14M大小的直接内存,如果大于设置的直接内存，
        //就会报出java.lang.OutOfMemoryError: Direct buffer memory
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 14);
    }


}
