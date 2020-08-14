package cn.enjoyedu.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * 类说明：
 */
@Component
public class ReplyTo {

    @Autowired
    @Qualifier("jmsConsumerQueueTemplate")
    private JmsTemplate jmsTemplate;

    public void send(final String consumerMsg, Message producerMessage)
            throws JMSException {
        jmsTemplate.send(producerMessage.getJMSReplyTo(),
                new MessageCreator() {
                    public Message createMessage(Session session)
                            throws JMSException {
                        Message msg
                                = session.createTextMessage("ReplyTo " + consumerMsg);
                        return msg;
                    }
                });
    }
}
