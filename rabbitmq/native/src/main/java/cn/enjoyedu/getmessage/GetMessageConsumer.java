package cn.enjoyedu.getmessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
public class GetMessageConsumer {


    public static void main(String[] argv)
            throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(GetMessageProducer.EXCHANGE_NAME,
                "direct");
        // 声明一个队列
        String queueName = "focuserror";
        channel.queueDeclare(queueName,
                false,false,
                false,null);

        String severity="error";//只关注error级别的日志，然后记录到文件中去。
        channel.queueBind(queueName,
                GetMessageProducer.EXCHANGE_NAME, severity);

        System.out.println(" [*] Waiting for messages......");

        while(true){
            GetResponse getResponse =
                    channel.basicGet(queueName, true);
            if(null!=getResponse){
                System.out.println("received["
                        +getResponse.getEnvelope().getRoutingKey()+"]"
                        +new String(getResponse.getBody()));
            }
            Thread.sleep(1000);
        }


    }

}
