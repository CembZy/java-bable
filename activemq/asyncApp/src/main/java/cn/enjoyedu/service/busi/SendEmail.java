package cn.enjoyedu.service.busi;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 类说明：发送邮件的服务
 */
@Service
public class SendEmail {

    public void sendEmail(String email) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-------------Already Send email to " + email);
    }

}
