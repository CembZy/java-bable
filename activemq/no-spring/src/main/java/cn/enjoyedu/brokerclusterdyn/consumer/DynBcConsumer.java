package cn.enjoyedu.brokerclusterdyn.consumer;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 *类说明：
 */
public class DynBcConsumer {

    private static final String USERNAME
            = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD
            = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    private static final String BROKEURL
            = "failover:(tcp://localhost:61618)?randomize=false";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;//连接工厂
        Connection connection = null;//连接

        Session session;//会话 接受或者发送消息的线程
        Destination destination;//消息的目的地

        MessageConsumer messageConsumer;//消息的消费者

        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(DynBcConsumer.USERNAME,
                DynBcConsumer.PASSWORD, DynBcConsumer.BROKEURL);

        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //创建一个连接HelloWorld的消息队列
            //destination = session.createTopic("HelloTopic8");
            destination = session.createQueue("BrokerCluster");

            //创建消息消费者
            messageConsumer = session.createConsumer(destination);
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
