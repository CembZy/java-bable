package cn.enjoyedu.service.consumer.queue;

import cn.enjoyedu.service.busi.SendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 类说明：：处理短信服务的消费者
 */
@Service
public class ProcessSms implements MessageListener {

    @Autowired
    private ReplyProducer replyProducer;

    @Autowired
    private SendSms sendSms;

    public void onMessage(Message message) {
        System.out.println("Accept message,ready process..............");
        try {
            String textMsg = ((TextMessage) message).getText();
            String checkCode = sendSms.sendSms(textMsg);
            replyProducer.send(textMsg, message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
