package com.sundy.sso.share.service;

/**
 * @author plus.wang
 * @description 基本响应code枚举
 * @date 2018/4/23
 */
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS("2000", "SUCCESS"),

    /**
     * 客户端参数错误
     */
    CLIENT_ERROR("4000", "Client Arguments Error"),

    /**
     * 服务端异常
     */
    SERVER_ERROR("5000", "Server ERROR");

    private String code;

    private String message;

    private ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
