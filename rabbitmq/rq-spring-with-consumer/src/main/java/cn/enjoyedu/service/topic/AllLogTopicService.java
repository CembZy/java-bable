package cn.enjoyedu.service.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
@Component
public class AllLogTopicService implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(AllLogTopicService.class);
    public void onMessage(Message message) {
        logger.info("Get message: "+new String( message.getBody()));
    }
}
