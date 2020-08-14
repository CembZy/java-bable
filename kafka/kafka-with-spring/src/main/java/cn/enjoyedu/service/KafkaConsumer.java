package cn.enjoyedu.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
public class KafkaConsumer  implements MessageListener<String,String> {

    public void onMessage(ConsumerRecord<String, String> data) {
        String name = Thread.currentThread().getName();
        System.out.println(name+"|"+String.format(
                "主题：%s，分区：%d，偏移量：%d，key：%s，value：%s",
                data.topic(),data.partition(),data.offset(),
                data.key(),data.value()));
    }
}

