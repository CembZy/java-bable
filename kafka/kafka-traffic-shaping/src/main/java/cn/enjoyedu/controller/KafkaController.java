package cn.enjoyedu.controller;

import cn.enjoyedu.service.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
@RestController
public class KafkaController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DBService dbService;
    /**
     * 对外开放的接口，地址为：http://127.0.0.1:8090/buyTicket
     * @return TicketInfo对象
     * @throws Exception
     */
    @RequestMapping("/buyTicket")
    public String buyTicket(){
        try {
            //模拟出票……
            System.out.println("开始购票业务－－－－－－");
            return dbService.useDb("select ticket ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
