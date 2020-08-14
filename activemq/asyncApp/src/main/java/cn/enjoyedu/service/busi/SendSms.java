package cn.enjoyedu.service.busi;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 类说明：发送短信的服务
 */
@Service
public class SendSms {

    public String sendSms(String phoneNumber) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String checkCode = "123456";
        System.out.println("-------------Already Send Sms["+checkCode+"] to "
                + phoneNumber);
        return checkCode;
    }

}
