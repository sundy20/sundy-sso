package com.sundy.sso.service.exception;

import com.sundy.sso.share.service.Result;
import com.sundy.sso.share.service.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created on 2017/12/7
 *
 * @author plus.wang
 * @description 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常处理
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public Result<String> defaultErrorHandler(Exception e) {

        logger.error("GlobalExceptionHandler.defaultErrorHandler Error Message : ", e);

        return Result.failure(ResultCode.SERVER_ERROR.getCode(), e.getMessage());
    }

    /**
     * 未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result<String> notFount(RuntimeException e) {

        logger.error("GlobalExceptionHandler.notFount  500,Internal Server Error Message : ", e);

        return Result.failure(ResultCode.SERVER_ERROR.getCode(), e.getMessage());
    }
}
