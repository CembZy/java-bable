package cn.enjoyedu.queue;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 类说明：
 */
@Component
public class GetResponse implements MessageListener {
    public void onMessage(Message message) {
        String textMsg = null;
        try {
            textMsg = ((TextMessage) message).getText();
            System.out.println("GetResponse accept msg : " + textMsg);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
