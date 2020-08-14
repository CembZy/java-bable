package com.cemb.ssm.test.controller;

import com.cemb.ssm.annotation.CemBAutowired;
import com.cemb.ssm.annotation.CemBController;
import com.cemb.ssm.annotation.CemBRequestMapping;
import com.cemb.ssm.annotation.CemBRequestParam;
import com.cemb.ssm.common.util.Result;
import com.cemb.ssm.test.service.TestService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName: TestController
 * @Auther: CemB
 * @Description:
 * @Email: 729943791@qq.com
 * @Date: 2018/7/10 14:25
 */

@CemBRequestMapping
@CemBController
public class TestController {

    @CemBAutowired
    private TestService testService;


    @CemBRequestMapping("test")
    public Result test(@CemBRequestParam("name") String name,
                       @CemBRequestParam("age") String age,
                       HttpServletResponse response) throws IOException {
        String test = testService.test(name, age);
        PrintWriter inputStream = response.getWriter();
        inputStream.print(test);

        return Result.ok().put("name", name).put("age", age);
    }

}
