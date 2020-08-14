package cn.enjoyedu.service;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
@Component
public class SendInfo implements ProducerListener {
    @Override
    public void onSuccess(String topic, Integer partition,
                          Object key, Object value,
                          RecordMetadata recordMetadata) {
        System.out.println(String.format(
                "主题：%s，分区：%d，偏移量：%d，" +
                        "key：%s，value：%s",
                recordMetadata.topic(),recordMetadata.partition(),
                recordMetadata.offset(),key,value));

    }

    @Override
    public void onError(String topic, Integer partition,
                        Object key, Object value, Exception exception) {
        exception.printStackTrace();
    }

    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }
}
