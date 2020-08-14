package cn.enjoyedu;

import cn.enjoyedu.normal.queue.ProducerQueue;
import cn.enjoyedu.normal.topic.ProducerTopic;
import cn.enjoyedu.replyto.ProducerR;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmSpringbootApplicationTests {

    @Autowired
    private ProducerQueue producerQueue;
    @Autowired
    private ProducerR producerR;
    @Autowired
    private ProducerTopic producerTopic;

    @Test
    public void testQueueNormal() {
        Destination destination
                = new ActiveMQQueue("springboot.queue");
        for(int i=0; i<10; i++){
            producerQueue.sendMessage(destination,
                    "NO:"+i+";my name is Mark!!!");
        }
    }

    @Test
    public void testTopicNormal() {
        Destination destination
                = new ActiveMQTopic("springboot.topic");
        for(int i=0; i<3; i++){
            producerTopic.sendMessage(destination,
                    "NO:"+i+";James like 13 !!!");
        }
    }

    @Test
    public void testReplyTo() {
        Destination destination
                = new ActiveMQQueue("springboot.replyto.queue");
        for(int i=0; i<3; i++){
            producerR.sendMessage(destination,
                    "NO:"+i+";my name is Mark!!!");
        }
    }

}
