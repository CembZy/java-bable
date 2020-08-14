package cn.enjoyedu.service.reg.impl;

import cn.enjoyedu.service.busi.SaveUser;
import cn.enjoyedu.service.producer.GetCustomResp;
import cn.enjoyedu.service.reg.IUserReg;
import cn.enjoyedu.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * <p>
 * 类说明：消息队列的实现
 */
@Service
@Qualifier("mq")
public class MqProcess implements IUserReg {

    @Autowired
    private SaveUser saveUser;

    @Autowired
    @Qualifier("jmsTopicTemplate")
    private JmsTemplate jmsTopicTemplate;

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsQueueTemplate;

    @Autowired
    private GetCustomResp getCustomResp;

    public boolean userRegister(final User user) {
        saveUser.saveUser(user);
        sendMq(jmsQueueTemplate, "user.sms", user.getPhoneNumber(), true);
        sendMq(jmsQueueTemplate, "user.email", user.getEmail());
        sendMq(jmsTopicTemplate,"user.topic",user.toString());
        return true;
    }

    private void sendMq(JmsTemplate jmsTemplate,
                        String dest, final String message) {
        System.out.println("Ready send msg:[" + message + "]");
        jmsTemplate.send(dest, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                Message msg = session.createTextMessage(message);
                return msg;
            }
        });
    }

    private void sendMq(JmsTemplate jmsTemplate,
                        String dest, final String message,
                        final boolean needReply) {
        jmsTemplate.send(dest, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                Message msg = session.createTextMessage(message);
                if (needReply) {
                    System.out.println("Ready send msg:[" + message + "],"
                            + "the msg need reply.");
                    //配置消费者应答相关内容
                    Destination tempDest =
                            session.createTemporaryQueue();
                    MessageConsumer responseConsumer
                            = session.createConsumer(tempDest);
                    //监听消费者应答的监听器
                    responseConsumer.setMessageListener(getCustomResp);
                    msg.setJMSReplyTo(tempDest);
                    //消费者应答的id，发送出的消息和应答消息进行匹配
                    String uid = System.currentTimeMillis() + "";
                    msg.setJMSCorrelationID(uid);
                }

                return msg;
            }
        });
    }
}
