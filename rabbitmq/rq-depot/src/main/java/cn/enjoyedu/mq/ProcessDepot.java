package cn.enjoyedu.mq;

import cn.enjoyedu.service.DepotManager;
import cn.enjoyedu.vo.GoodTransferVo;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
@Service
public class ProcessDepot implements ChannelAwareMessageListener {

    private static Logger logger = LoggerFactory.getLogger(ProcessDepot.class);

    @Autowired
    private DepotManager depotManager;

    private static Gson gson = new Gson();

    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String msg = new String(message.getBody());
            logger.info(">>>>>>>>>>>>>>接收到消息:"+msg);
            GoodTransferVo goodTransferVo = gson.fromJson(msg,GoodTransferVo.class);
            try {
                depotManager.operDepot(goodTransferVo);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                        false);
                logger.info(">>>>>>>>>>>>>>库存处理完成，应答Mq服务");
            } catch (Exception e) {
                logger.error(e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                        false,true);
                logger.info(">>>>>>>>>>>>>>库存处理失败，拒绝消息，要求Mq重新派发");
                throw e;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


}
