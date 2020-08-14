package cn.enjoyedu.dlx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：普通的消费者,但是自己无法消费的消息，将投入死信队列
 */
public class WillMakeDlxConsumer {

    public static void main(String[] argv)
            throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(DlxProducer.EXCHANGE_NAME,
                BuiltinExchangeType.TOPIC);

        /*声明一个队列，并绑定死信交换器*/
        String queueName = "dlx_make";
        Map<String,Object> args = new HashMap<String,Object>();
        args.put("x-dead-letter-exchange",
                DlxProcessConsumer.DLX_EXCHANGE_NAME);
        channel.queueDeclare(queueName,false,true,
                false,
                args);

        /*绑定，将队列和交换器通过路由键进行绑定*/
        channel.queueBind(queueName,
                DlxProducer.EXCHANGE_NAME,"#");

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                if(envelope.getRoutingKey().equals("error")){
                    System.out.println("Received["
                            +envelope.getRoutingKey()
                            +"]"+message);
                    channel.basicAck(envelope.getDeliveryTag(),
                            false);
                }else{
                    System.out.println("Will reject["
                            +envelope.getRoutingKey()
                            +"]"+message);
                    channel.basicReject(envelope.getDeliveryTag(),
                            false);
                }

            }
        };
        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,false,consumer);


    }

}
