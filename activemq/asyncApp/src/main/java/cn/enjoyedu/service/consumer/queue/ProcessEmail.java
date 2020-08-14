package cn.enjoyedu.service.consumer.queue;

import cn.enjoyedu.service.busi.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * <p>
 * 类说明：处理邮件服务的消费者
 */
@Component
public class ProcessEmail implements MessageListener {

    @Autowired
    private SendEmail sendEmail;

    public void onMessage(Message message) {
        System.out.println("Accept message,ready process..............");
        try {
            sendEmail.sendEmail(((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
