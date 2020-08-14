package com.cemb.jwt.common.intercepter;

import com.cemb.jwt.common.exception.RException;
import com.cemb.jwt.common.util.JwtUtil;
import com.cemb.jwt.modeuls.login.model.User;
import io.jsonwebtoken.Claims;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: CemB
 * Descriotion: 登录拦截器
 */
@Component
public class AppLoginIntercepter extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger LOG = LoggerFactory.getLogger(AppLoginIntercepter.class);

    //执行时机：进入action方法之前执行
    //使用场景：用于用户认证、用户授权拦截
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        /**
         * 判断，如果是静态资源的访问就不进行拦截处理。
         */
        if (requestURI.contains("favicon.ico")) {
            return true;
        }
        if (requestURI.contains(".html") || requestURI.contains(".js") || requestURI.contains(".css")
                || requestURI.contains(".jpg") || requestURI.contains(".png") || requestURI.contains(".gif")
                || requestURI.contains(".woff") || requestURI.contains(".ttf") || requestURI.contains(".map")) {
            return true;
        }
        Claims token = null;
        String token_str = "";


        /**
         * 获取token
         */
        if (request.getParameter("token") != null) {
            token_str = request.getParameter("token");
        }
        if (request.getHeader("token") != null) {
            token_str = request.getHeader("token");
        }

        try {
            token = jwtUtil.parseJWT(token_str);
        } catch (Exception e) {
            throw new RException("此操作需要登录后进行");
        }

        if (token != null) {
            String subject = token.getSubject();
            JSONObject jsonObject = JSONObject.fromObject(subject);
            String account = jsonObject.getString("account");
            String password = jsonObject.getString("password");
            LOG.info("account:" + account + "..........password:" + password);

            User user = (User) request.getServletContext().getAttribute(token_str);
            //验证
            if (account.equals(user.getAccount()) && password.equals(user.getPassword())) {
                return true;
            }
            return false;
        } else {
            throw new RException("此操作需要登录后进行");
        }

    }

    //执行时机：进入action方法，在返回modelAndView之前执行
    //使用场景：在这里统一对返回数据进行处理，比如统一添加菜单 导航
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

}
