package cn.enjoy.controller;

import cn.enjoy.db.EnjoyDataSource;
import cn.enjoy.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by VULCAN on 2018/9/14.
 */

@RestController
public class UserController {

    @RequestMapping("/sayhello")
    public String sayHello() {
        return "sayhello";
    }


    @Resource
    private UserService userService;

    @RequestMapping("/register")
    public String register(String username,String passwd) {
        boolean register = userService.register(username, passwd);
        if(register) {
            return  "注册成功";
        }else {
            return  "注册失败";
        }

    }

    @RequestMapping("/login")
    public String login(String username,String passwd) {
        boolean login = userService.login(username, passwd);
        if(login) {
            return  "登陆成功";
        }else {
            return  "登陆失败";
        }
    }

    @Resource
    private DataSource dataSource;

    @RequestMapping("/demo")
    public String demo() {
        EnjoyDataSource dataSource = (EnjoyDataSource) this.dataSource;
        dataSource.setUrl("jdbc:mysql://localhost:3306/mysqldemo2");

            dataSource.changeDataSource();

        //EnjoyDataSource.prop.put(EnjoyDataSource.PROP_KEY_URL,"jdbc:mysql://localhost:3306/mysqldemo2");
        //EnjoyDataSource.changeDataSource();

        return "切换成功";
    }

}
