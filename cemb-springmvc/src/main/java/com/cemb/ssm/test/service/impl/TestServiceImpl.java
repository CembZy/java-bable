package com.cemb.ssm.test.service.impl;

import com.cemb.ssm.annotation.CemBService;
import com.cemb.ssm.test.service.TestService;

/**
 * @ClassName: TestServiceImpl
 * @Auther: CemB
 * @Description:
 * @Email: 729943791@qq.com
 * @Date: 2018/7/10 14:57
 */
@CemBService("testService")
public class TestServiceImpl implements TestService {

    @Override
    public String test(String name, String age) {
        return "name:" + name + "----age:" + age;
    }

}
