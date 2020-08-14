package cn.enjoyedu.controller;

import cn.enjoyedu.service.reg.IUserReg;
import cn.enjoyedu.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 类说明：用户注册的控制器
 */
@Controller
public class UserRegController {
    private static final String SUCCESS = "suc";
    private static final String FAILUER = "failure";

    @Autowired
    @Qualifier("mq")
    private IUserReg userReg;

    @RequestMapping("/index")
    public String userReg() {
        return "userReg";
    }

    @RequestMapping("/saveUser")
    @ResponseBody
    public String saveUser(@RequestParam("name") String userName,
                           @RequestParam("email") String email,
                           @RequestParam("number") String phoneNumber,
                           @RequestParam("address") String address) {
        try {
            if (userReg.userRegister(new User(userName, email, phoneNumber, address)))
                return SUCCESS;
            else
                return FAILUER;
        } catch (Exception e) {
            return FAILUER;
        }
    }


}
