package cn.enjoyedu.selfserial;

import cn.enjoyedu.vo.DemoUser;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：反序列化器
 */
public class SelfDeserializer implements Deserializer<DemoUser> {


    public void configure(Map<String, ?> configs, boolean isKey) {
        //do nothing
    }

    public DemoUser deserialize(String topic, byte[] data) {
        try {
            if(data==null){
                return null;
            }
            if(data.length<8){
                throw new SerializationException("Error data size.");
            }
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int id;
            String name;
            int nameSize;
            id = buffer.getInt();
            nameSize = buffer.getInt();
            byte[] nameByte = new byte[nameSize];
            buffer.get(nameByte);
            name = new String(nameByte,"UTF-8");
            return new DemoUser(id,name);
        } catch (Exception e) {
            throw new SerializationException("Error Deserializer DemoUser."+e);
        }

    }

    public void close() {
        //do nothing
    }
}
