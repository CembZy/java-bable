package cn.enjoyedu.normal.topic;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 类说明：
 */
@Component
public class ConsumerBTopic {
    // 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
    @JmsListener(destination = "springboot.topic",
            containerFactory = "jmsTopicListenerContainerFactory"
             )
    public void receiveTopic(String text) {
        System.out.println(this.getClass().getName()+" 收到的报文为:"+text);
    }
}
