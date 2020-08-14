package com.cemb.shiro.modeuls.login.controller;


import com.cemb.shiro.common.util.JwtUtil;
import com.cemb.shiro.common.util.RedisUtils;
import com.cemb.shiro.common.util.Result;
import com.cemb.shiro.common.util.SystemConfig;
import com.cemb.shiro.modeuls.login.model.User;
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

    @Autowired
    private RedisUtils redisUtils;


    @PostMapping("login")
    public Result login(@RequestBody User user) {


        //生成一个token
        String subject = jwtUtil.generalSubject(user);
        String token = jwtUtil.createJWT(systemConfig.getId(), subject, systemConfig.getRefreshTtl());

        //可以将token存入数据库，对应当前用户，然后在shiro过滤处取出来，然后比较
        redisUtils.set(token, user, 60);

        return Result.ok().put("token", token);
    }

    @GetMapping("echo")
    public Result echo() {
        return Result.ok("hello world..");
    }


    @GetMapping("logout")
    public Result logout(HttpServletRequest request) {
        redisUtils.delete(request.getHeader("token"));
        return Result.ok();
    }

}
