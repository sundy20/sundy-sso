package com.sundy.sso.share.service;

public interface SsoService {


    /**
     * @param token
     * @return 用户id
     * @throws Exception
     * check用户session
     */
    Result<Long> checkUserSession(String token) throws Exception;


    /**
     * @param token
     * @return
     * @throws Exception
     * 刷新用户session
     */
    Result<Boolean> refreshAccount(String token) throws Exception;

    /**
     * @param token
     * @return Boolean
     * 注销
     */
    Result<Boolean> logout(String token) throws Exception;

}
