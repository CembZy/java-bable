package com.cemb.ssm.common.exception;

import java.io.Serializable;

/**
 * @ClassName: BaseException
 * @Auther: CemB
 * @Description: 自定义异常
 * @Email: 729943791@qq.com
 * @Date: 2018/7/11 15:08
 */
public class BaseException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    int code = 500;

    String msg;

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

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public BaseException(String msg, Throwable throwable) {
        super(msg, throwable);
        this.msg = msg;
    }

    public BaseException(int code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.msg = msg;
        this.code = code;
    }

}
