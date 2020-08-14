package cn.enjoyedu.qos;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：批量确认消费者
 */
public class BatchAckConsumer extends DefaultConsumer {

    private int messasgeCount = 0;

    public BatchAckConsumer(Channel channel) {
        super(channel);
        System.out.println("批量消费者启动...");
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println("批量消费者Received["+envelope.getRoutingKey()
                +"]"+message);
        messasgeCount++;
        if (messasgeCount % 50==0){
            this.getChannel().basicAck(envelope.getDeliveryTag(),
                    true);
            System.out.println("批量消费者进行消息的确认-------------");
        }
        if(message.equals("stop")){
            this.getChannel().basicAck(envelope.getDeliveryTag(),
                    true);
            System.out.println("批量消费者进行最后部分业务消息的确认-------------");
        }
    }
}
