package cn.enjoyedu.service.consumer.topic;

import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 类说明：客户服务：订阅了topic消息的消费者
 */
@Service
public class CustomerService implements MessageListener {

    public void onMessage(Message message) {
        try {
            String textMsg = ((TextMessage) message).getText();
            System.out.println("I will post gift to : " + textMsg);
            //do my 业务工作
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
