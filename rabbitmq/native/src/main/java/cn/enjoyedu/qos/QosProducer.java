package cn.enjoyedu.qos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：存放到延迟队列的元素，对业务数据进行了包装
 */
public class QosProducer {

    public final static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * 创建连接连接到MabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        // 设置MabbitMQ所在主机ip或者主机名
        factory.setHost("127.0.0.1");

        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定转发
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        //发送210条消息，其中第210条消息表示本批次消息的结束
        for(int i=0;i<210;i++){
            // 发送的消息
            String message = "Hello World_"+(i+1);
            if(i==209){
                message = "stop";
            }
            //参数1：exchange name
            //参数2：routing key
            channel.basicPublish(EXCHANGE_NAME, "error",
                    null, message.getBytes());
            System.out.println(" [x] Sent 'error':'"
                    + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}
