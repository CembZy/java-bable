package cn.enjoyedu.service.reg.impl;

import cn.enjoyedu.service.busi.SaveUser;
import cn.enjoyedu.service.busi.SendEmail;
import cn.enjoyedu.service.busi.SendSms;
import cn.enjoyedu.service.reg.IUserReg;
import cn.enjoyedu.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 类说明：串行的实现
 */
@Service
@Qualifier("serial")
public class SerialProcess implements IUserReg {

    @Autowired
    private SaveUser saveUser;
    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private SendSms sendSms;

    public boolean userRegister(User user) {
        try {
            saveUser.saveUser(user);
            sendEmail.sendEmail(user.getEmail());
            sendSms.sendSms(user.getPhoneNumber());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
