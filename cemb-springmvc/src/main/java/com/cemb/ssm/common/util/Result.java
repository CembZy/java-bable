package com.cemb.ssm.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Result
 * @Auther: CemB
 * @Description: 结果集实体
 * @Email: 729943791@qq.com
 * @Date: 2018/7/11 14:25
 */
public class Result extends HashMap<Object, Object> {
    private int code = 200;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result() {
        super.put("code", code);
    }


    public static Result ok() {
        return new Result();
    }

    public static Result ok(String msg) {
        Result result = new Result();
        result.put("msg", msg);
        return result;
    }

    public Result put(Object v1, Object v2) {
        super.put(v1, v2);
        return this;
    }

    public static Result ok(Map<Object, Object> map) {
        Result result = new Result();
        result.putAll(map);
        return result;
    }

    public static Result error() {
        return error(500, null);
    }

    public static Result error(String msg) {
        return error(500, msg);
    }

    public static Result error(int code, String msg) {
        Result result = new Result();
        result.put("msg", msg);
        result.put("code", code);
        return result;
    }

}
