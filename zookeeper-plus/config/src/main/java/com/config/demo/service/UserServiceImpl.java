package com.config.demo.service;

import com.config.demo.dao.EnjoyUserMapper;
import com.config.demo.model.EnjoyUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class UserServiceImpl implements UserService {
    @Resource
    private EnjoyUserMapper enjoyUserMapper;

    @Override
    public String sayHello() {
        return "hello enjoy";
    }

    @Override
    public boolean register(String username, String passwd) {
        EnjoyUser enjoyUser = new EnjoyUser();
        enjoyUser.setPasswd(passwd);
        enjoyUser.setUsername(username);
        return enjoyUserMapper.insertSelective(enjoyUser) >0;
    }

    @Override
    public boolean login(String username, String passwd) {
        return enjoyUserMapper.findUserByUsernameAndPwd(username, passwd) !=null;
    }


}
