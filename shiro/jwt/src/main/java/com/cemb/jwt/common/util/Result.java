package com.cemb.jwt.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CemB
 */

public class Result extends HashMap {

    private String msg;

    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public Result() {
        put("code", 200);
    }


    /**
     * 构造HashMap类型的结果集对象
     *
     * @param key
     * @param value
     * @return
     */
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 错误信息
     *
     * @param msg
     * @return
     */
    public static Result error(String msg, int code) {
        Result result = new Result();
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }

    /**
     * 错误信息
     *
     * @param msg
     * @return
     */
    public static Result error(String msg) {
        return error(msg, 500);
    }


    /**
     * 错误信息
     *
     * @return
     */
    public static Result error() {
        return error("未知异常", 500);
    }


    /**
     * 正确信息
     *
     * @param msg
     * @return
     */
    public static Result ok(String msg) {
        Result result = new Result();
        result.put("msg", msg);
        return result;
    }

    /**
     * 正确信息
     *
     * @param map
     * @return
     */
    public static Result ok(Map<String, Object> map) {
        Result result = new Result();
        result.putAll(map);
        return result;
    }


    /**
     * 正确信息
     *
     * @return
     */
    public static Result ok() {
        return new Result();
    }


}