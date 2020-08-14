package cn.enjoyedu.service.consumer.topic;

import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * <p>
 * 类说明：数据中心服务：订阅了topic消息的消费者
 */
@Service
public class DataCenter implements MessageListener {

    public void onMessage(Message message) {
        try {
            String textMsg = ((TextMessage) message).getText();
            //do my 业务工作
            System.out.println("We save User[" + textMsg + "] into DataCenter ");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
