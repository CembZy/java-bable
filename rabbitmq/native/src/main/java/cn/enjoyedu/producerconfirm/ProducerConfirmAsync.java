package cn.enjoyedu.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：存放到延迟队列的元素，对业务数据进行了包装
 */
public class ProducerConfirmAsync {

    public final static String EXCHANGE_NAME = "producer_async_confirm";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 启用发送者确认模式
        channel.confirmSelect();

        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple)
                    throws IOException {
                System.out.println("deliveryTag:"+deliveryTag
                        +",multiple:"+multiple);

            }

            public void handleNack(long deliveryTag, boolean multiple)
                    throws IOException {

            }
        });

        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties,
                                     byte[] body)
                    throws IOException {
                String message = new String(body);
                System.out.println("RabbitMq路由失败:  "+routingKey+"."+message);
            }
        });

        String[] severities={"error","warning"};
        for(int i=0;i<100;i++){
            String severity = severities[i%2];
            // 发送的消息
            String message = "Hello World_"+(i+1)+("_"+System.currentTimeMillis());
            channel.basicPublish(EXCHANGE_NAME, severity, true,
                    MessageProperties.PERSISTENT_BASIC, message.getBytes());
            System.out.println("----------------------------------------------------");
            System.out.println(" Sent Message: [" + severity +"]:'"+ message + "'");
        }

        // 关闭频道和连接
        //channel.close();
        //connection.close();
    }


}
