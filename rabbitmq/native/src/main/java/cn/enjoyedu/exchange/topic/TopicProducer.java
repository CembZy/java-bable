package cn.enjoyedu.exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
public class TopicProducer {

    public final static String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args)
            throws IOException, TimeoutException {
        /**
         * 创建连接连接到RabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        // 设置MabbitMQ所在主机ip或者主机名
        factory.setHost("127.0.0.1");

        // 创建一个连接
        Connection connection = factory.newConnection();

        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定转发
        channel.exchangeDeclare(EXCHANGE_NAME,
                BuiltinExchangeType.TOPIC);

        /*日志消息，路由键最终格式类似于：info.order.B*/
        String[] severities={"error","info","warning"};
        for(int i=0;i<3;i++){
            String[]  modules={"user","order","email"};
            for(int j=0;j<3;j++){
                String[]  servers={"A","B","C"};
                for(int k=0;k<3;k++){
                    // 发送的消息
                    String message = "Hello Topic_["+i+","+j+","+k+"]";
                    String routeKey = severities[i%3]+"."+modules[j%3]
                            +"."+servers[k%3];
                    channel.basicPublish(EXCHANGE_NAME,routeKey,
                            null, message.getBytes());
                    System.out.println(" [x] Sent '" + routeKey +":'"
                            + message + "'");
                }
            }

        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}
