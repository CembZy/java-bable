package cn.enjoyedu.haproxy;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：普通的消费者
 */
public class HaProxyConsumerNode1 {

    public static void connectMq(ConnectionFactory factory,boolean isReconnect)
            throws IOException, TimeoutException {
        if(isReconnect) System.out.println("需要重新连接....");
        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(HaProxyProducerNode2.EXCHANGE_NAME,
                "fanout");

        /*声明一个队列*/
        String queueName = "focuserror";
        channel.queueDeclare(queueName,false,false,
                false,null);

        /*绑定，将队列和交换器通过路由键进行绑定*/
        String routekey = "error";/*表示只关注error级别的日志消息*/
        channel.queueBind(queueName,HaProxyProducerNode2.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()
                        +"]"+message);
            }
        };
        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,true,consumer);
    }

    public static void main(String[] argv)
            throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.103");
        factory.setVirtualHost("enjoyedu");
        factory.setUsername("mark");
        factory.setPassword("123456");
        factory.setPort(5670);
//        try {
            connectMq(factory,false);
//        } catch (IOException e) {
//            e.printStackTrace();
//            connectMq(factory,true);
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//            connectMq(factory,true);
//
//        }


    }

}
