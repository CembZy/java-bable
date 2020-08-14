package com.concurrent.ch07.fw1;

import com.concurrent.SleepTools;
import com.concurrent.ch07.fw1.po.Result;
import com.concurrent.ch07.fw1.po.ResultType;
import com.concurrent.ch07.fw1.util.Task;

import java.util.Random;

/**
 *类说明：一个实际任务类，将数值加上一个随机数，并休眠随机时间
 */
public class MyTask implements Task<Integer, Integer> {

    @Override
    public Result<Integer> taskExecute(Integer data) {
        //随机休眠
        Random r = new Random();
        int flag = r.nextInt(500);
        SleepTools.ms(flag);


        //模拟业务处理情况

        //正常处理的情况
        if (flag <= 300) {
            Integer returnValue = data.intValue() + flag;
            return new Result<Integer>(ResultType.SUCCESS, returnValue);
        } else if (flag > 301 && flag <= 400) {//处理失败的情况
            return new Result<Integer>("失败", ResultType.FAIL, -1);
        } else {//发生异常的情况
            try {
                throw new RuntimeException("异常发生了！！");
            } catch (Exception e) {
                return new Result<Integer>(e.getMessage(), ResultType.EXCEPTIN,
                        -1);
            }
        }

    }

}
