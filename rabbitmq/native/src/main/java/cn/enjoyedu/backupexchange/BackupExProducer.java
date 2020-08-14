package cn.enjoyedu.backupexchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：存放到延迟队列的元素，对业务数据进行了包装
 */
public class BackupExProducer {

    public final static String EXCHANGE_NAME = "main-exchange";
    public final static String BAK_EXCHANGE_NAME = "ae";

    public static void main(String[] args)
            throws IOException, TimeoutException {
        /**
         * 创建连接连接到RabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();

        // 声明备用交换器
        Map<String,Object> argsMap = new HashMap<String,Object>();
        argsMap.put("alternate-exchange",BAK_EXCHANGE_NAME);
        //主交换器
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",
                false,false,argsMap);
        //备用交换器
        channel.exchangeDeclare(BAK_EXCHANGE_NAME,BuiltinExchangeType.FANOUT,
                true,false,null);

        //所有日志严重性级别
        String[] severities={"error","info","warning"};
        for(int i=0;i<3;i++){
            //每一次发送一条不同严重性的日志
            String severity = severities[i%3];

            // 发送的消息
            String message = "Hello World_"+(i+1);
            //参数1：exchange name
            //参数2：routing key
            channel.basicPublish(EXCHANGE_NAME, severity,
                    null, message.getBytes());
            System.out.println(" [x] Sent '" + severity +"':'"
                    + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}
