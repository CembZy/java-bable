package cn.enjoyedu.service.producer;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * <p>
 * 类说明：生产者端接收消费者的应答
 */
@Component
public class GetCustomResp implements MessageListener {
    public void onMessage(Message message) {
        try {
            String textMsg = ((TextMessage) message).getText();
            System.out.println("GetResponse accept response : " + textMsg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
