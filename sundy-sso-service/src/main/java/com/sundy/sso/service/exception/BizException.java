package com.sundy.sso.service.exception;

/**
 * Created on 2017/12/7
 *
 * @author plus.wang
 * @description 业务通用异常
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -9079010183705787652L;

    public BizException() {

    }

    public BizException(String message) {

        super(message);
    }

}
