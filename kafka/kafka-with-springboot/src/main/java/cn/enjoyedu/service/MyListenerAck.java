package cn.enjoyedu.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
public class MyListenerAck {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = {"testAck"},containerFactory = "factoryAck")
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        try {
            logger.info("自行确认方式收到消息的key: " + record.key());
            logger.info("自行确认方式收到消息的value: " + record.value().toString());
        } finally {
            logger.info("消息确认！");
            ack.acknowledge();
        }
    }
}
