package cn.enjoyedu.topic;

import cn.enjoyedu.RmConst;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
@Component
public class TopicSender {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        String msg1 = "I am email mesaage msg======";
        System.out.println("TopicSender send the 1st : " + msg1);
        this.rabbitTemplate.convertAndSend(RmConst.EXCHANGE_TOPIC, RmConst.RK_EMAIL, msg1);

        String msg2 = "I am user mesaages msg########";
        System.out.println("TopicSender send the 2nd : " + msg2);
        this.rabbitTemplate.convertAndSend(RmConst.EXCHANGE_TOPIC, RmConst.RK_USER, msg2);

        String msg3 = "I am error mesaages msg";
        System.out.println("TopicSender send the 3rd : " + msg3);
        this.rabbitTemplate.convertAndSend(RmConst.EXCHANGE_TOPIC, "errorkey", msg3);
    }

}
