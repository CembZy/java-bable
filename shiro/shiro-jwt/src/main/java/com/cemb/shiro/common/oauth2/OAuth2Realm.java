package com.cemb.shiro.common.oauth2;

import com.cemb.shiro.common.exception.RException;
import com.cemb.shiro.common.util.JwtUtil;
import com.cemb.shiro.common.util.RedisUtils;
import com.cemb.shiro.modeuls.login.model.User;
import io.jsonwebtoken.Claims;
import net.sf.json.JSONObject;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 认证
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        Claims claims = null;
        try {
            claims = jwtUtil.parseJWT(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.fromObject(subject);
        String account = jsonObject.getString("account");
        String password = jsonObject.getString("password");

        //根据token，查询用户信息
        User user = redisUtils.get(accessToken, User.class);
        if (user == null) {
            throw new RException("token失效，请重新登录");
        }

        //验证
        if (account.equals(user.getAccount()) && password.equals(user.getPassword())) {
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
            return info;
        }
        throw new RException("token失效，请重新登录");
    }
}
