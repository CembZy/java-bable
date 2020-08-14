package cn.enjoyedu.normal.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * 类说明：
 */
@Service
public class ProducerQueue {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;
    @Autowired
    private JmsTemplate template;//可以做更细微的控制

    // 发送消息，destination是发送到的队列，message是待发送的消息
    public void sendMessage(Destination destination, final String message){
        jmsTemplate.convertAndSend(destination, message);
        template.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session)
                    throws JMSException {
                TextMessage msg = session.createTextMessage();
                msg.setText("othre information");
                return msg;
            }
        });
    }


}
