package com.sundy.sso.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2017/11/17
 *
 * @author plus.wang
 * @description
 */
@Configuration
@ConfigurationProperties(prefix = "sso.jwtToken.cookie")
public class JwtProperties {

    private String name;

    private String domain;

    /**
     * JwtToken will expire after this time.
     */
    private Integer tokenExpirationTime;

    /**
     * JwtToken can be refreshed during this timeframe.
     */
    private Integer refreshTokenExpTime;

    /**
     * Key is used to sign
     */
    private String tokenSigningKey;

    private Integer loginCount;

    private String loginPrefix;

    private String caotchaKey;

    public Integer getRefreshTokenExpTime() {
        return refreshTokenExpTime;
    }

    public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public Integer getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(Integer tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getLoginPrefix() {
        return loginPrefix;
    }

    public void setLoginPrefix(String loginPrefix) {
        this.loginPrefix = loginPrefix;
    }

    public String getCaotchaKey() {
        return caotchaKey;
    }

    public void setCaotchaKey(String caotchaKey) {
        this.caotchaKey = caotchaKey;
    }
}

