package cn.enjoyedu.service.fanout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
public class H2_Service implements MessageListener{
    private Logger logger = LoggerFactory.getLogger(H2_Service.class);
    public void onMessage(Message message) {
        logger.info("Get message: "+new String( message.getBody()));
    }
}
