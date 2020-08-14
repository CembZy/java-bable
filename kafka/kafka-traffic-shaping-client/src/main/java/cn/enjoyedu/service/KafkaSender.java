package cn.enjoyedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void messageSender(String tpoic,String key,String message){
        try {
            System.out.println("准备发送..."+tpoic+","+","+key);
            kafkaTemplate.send(tpoic,key,message);
            System.out.println("已发送");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
