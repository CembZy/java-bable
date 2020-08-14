package com.cemb.jwt.modeuls.login.controller;


import com.cemb.jwt.common.util.JwtUtil;
import com.cemb.jwt.common.util.Result;
import com.cemb.jwt.common.util.SystemConfig;
import com.cemb.jwt.modeuls.login.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("test")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SystemConfig systemConfig;

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);


    @PostMapping("login")
    public Result login(@RequestBody User user, HttpServletRequest request) {

        LOG.info("account:" + user.getAccount() + ".......password:" + user.getPassword());

        //处理登录逻辑

        //登录成功后传回token
        String subject = jwtUtil.generalSubject(user);
        String jwt = jwtUtil.createJWT(systemConfig.getId(), subject, systemConfig.getRefreshTtl());
        request.getServletContext().setAttribute(jwt, user);

        return Result.ok().put("token", jwt);
    }

    @GetMapping("echo")
    public Result echo() {
        return Result.ok("hello world..");
    }


    @GetMapping("logout")
    public Result logout(HttpServletRequest request) {
        request.getServletContext().removeAttribute(request.getHeader("token"));
        return Result.ok();
    }

}
