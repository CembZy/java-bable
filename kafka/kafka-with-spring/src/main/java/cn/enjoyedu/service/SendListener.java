package cn.enjoyedu.service;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
public class SendListener implements ProducerListener {

    public void onSuccess(String topic, Integer partition,
                          Object key, Object value, RecordMetadata recordMetadata) {
        System.out.println("offset:"+recordMetadata.offset()+"-"
                +"partition:"+recordMetadata.partition());
    }

    public void onError(String topic, Integer partition,
                        Object key, Object value, Exception exception) {

    }

    public boolean isInterestedInSuccess() {
        return true;
    }
}
