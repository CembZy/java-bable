package cn.enjoyedu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
@Service
public class ProcessOrder {
    private Logger logger = LoggerFactory.getLogger(ProcessOrder.class);

    @Autowired
    @Qualifier("mq")
    private IProDepot proDepot;

    public void processOrder(String goodsId,int amount){
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("--------------------["+goodsId+"]订单入库完成，准备变动库存！");
        proDepot.processDepot(goodsId,amount);

    }

}
