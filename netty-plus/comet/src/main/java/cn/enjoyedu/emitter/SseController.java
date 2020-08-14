package cn.enjoyedu.emitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类说明：
 */
@Controller
public class SseController {
    private static Logger logger = LoggerFactory.getLogger(SseController.class);

    private static Map<String,SseEmitter> sseEmitters
            = new ConcurrentHashMap<>();
    private ExecutorService executorService
            = Executors.newFixedThreadPool(2);

    @RequestMapping("/weChatPay")
    public String stock(){
        return "weChatPay";
    }

    @RequestMapping(value="/payMoney")
    @ResponseBody
    public SseEmitter pay(String weCharId){
        SseEmitter emitter = new SseEmitter();
        sseEmitters.put(weCharId,emitter);
        executorService.submit(new Pay(weCharId) );
        return emitter;
    }

    private static class Pay implements Runnable{

        private String weCharId;

        public Pay(String weCharId) {
            this.weCharId = weCharId;
        }

        @Override
        public void run() {
            SseEmitter sseEmitter = sseEmitters.get(weCharId);
            try {
                logger.info("联系支付服务，准备扣款");
                Thread.sleep(500);
                sseEmitter.send("支付完成");
                logger.info("准备通知自动售货机");
                Thread.sleep(1500);//售货机的动作
                sseEmitter.send("已通知自动售货机C9出货，请勿走开！");
                sseEmitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
