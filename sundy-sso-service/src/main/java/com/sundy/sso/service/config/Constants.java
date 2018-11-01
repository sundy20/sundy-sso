package com.sundy.sso.service.config;

/**
 * Created on 2017/12/28
 *
 * @author plus.wang
 * @description
 */
public interface Constants {

    //sso 统一redis key前缀
    String SSO_REDIS_KEY_PRIFIX = "sundy-sso:";

    //短信验证码五分钟限流
    long SSO_SMS_RATE = 300000;

}
