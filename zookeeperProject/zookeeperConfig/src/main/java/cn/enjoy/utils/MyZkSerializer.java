package cn.enjoy.utils;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

/**
 * Created by VULCAN on 2018/10/12.
 */
public class MyZkSerializer implements ZkSerializer
{
    public Object deserialize(byte[] bytes) throws ZkMarshallingError
    {
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public byte[] serialize(Object obj) throws ZkMarshallingError
    {
        try {
            return String.valueOf(obj).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

