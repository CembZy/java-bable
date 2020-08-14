package com.cemb.shiro.common.exception;

import com.cemb.shiro.common.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author CemB
 */


@RestControllerAdvice
public class RExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RExceptionHandler.class);

    /**
     * @param e
     * @return
     */
    @ExceptionHandler(RException.class)
    public Result handlerRException(RException e) {
        Result result = new Result();
        result.put("code", e.getCode());
        result.put("msg", e.getMsg());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return Result.error();
    }

}