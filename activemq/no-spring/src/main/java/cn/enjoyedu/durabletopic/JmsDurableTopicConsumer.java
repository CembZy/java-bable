package cn.enjoyedu.durabletopic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 *类说明：
 */
public class JmsDurableTopicConsumer {

    private static final String USERNAME
            = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD
            = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    private static final String BROKEURL
            = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;//连接工厂
        Connection connection = null;//连接

        Session session;//会话 接受或者发送消息的线程

        TopicSubscriber messageConsumer;

        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(JmsDurableTopicConsumer.USERNAME,
                JmsDurableTopicConsumer.PASSWORD, JmsDurableTopicConsumer.BROKEURL);

        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //需要有个Id
            connection.setClientID("Mark");
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //创建一个消息队列
            Topic destination = session.createTopic("DurableTopic2");
            //创建消息消费者-持久订阅
            messageConsumer
                    = session.createDurableSubscriber(destination,"xiangxue");

            messageConsumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        System.out.println("Accept msg : "
                                +((TextMessage)message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
