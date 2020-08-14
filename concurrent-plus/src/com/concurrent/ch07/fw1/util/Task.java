package com.concurrent.ch07.fw1.util;


import com.concurrent.ch07.fw1.po.Result;

/**
 * 对外暴露的任务框架接口
 */
public interface Task<T, R> {

    //任务执行,data是传入的要执行的数据
    Result<R> taskExecute(T data);
}
