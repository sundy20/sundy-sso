package com.sundy.sso.service.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.sundy.sso.service.config.Constants;
import com.sundy.sso.service.config.JwtProperties;
import com.sundy.sso.service.util.StringUtil;
import com.sundy.sso.share.service.Result;
import com.sundy.sso.share.service.ResultCode;
import com.sundy.sso.share.service.SsoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/11/28
 *
 * @author plus.wang
 * @description
 */
@Service(version = "1.0.0", protocol = {"dubbo"})
public class SsoServiceImpl implements SsoService {

    private static final Logger log = LoggerFactory.getLogger(SsoServiceImpl.class);

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * @param token
     * @return 用户id
     */
    @Override
    public Result<Long> checkUserSession(String token) {

        Result<Long> longResult = Result.failure();

        if (StringUtils.isEmpty(token)) {

            longResult.message("token is null");

            if (log.isInfoEnabled()) {

                log.info("SsoServiceImpl.checkUserSession token is null");
            }

            return longResult;
        }

        String userId;

        try {

            token = Constants.SSO_REDIS_KEY_PRIFIX + token;

            userId = stringRedisTemplate.opsForValue().get(token);

            if (log.isInfoEnabled()) {

                log.info("SsoServiceImpl.checkUserSession token : {} userId : {} ", token, userId);
            }

            if (StringUtil.isDigits(userId)) {

                longResult.code(ResultCode.SUCCESS.getCode()).data(Long.valueOf(userId));
            }

        } catch (Exception e) {

            if (log.isErrorEnabled()) {

                log.error("SsoServiceImpl.checkUserSession token : {} , Exception : {} ", token, e);
            }

            longResult.message(e.getMessage());
        }

        return longResult;
    }

    /**
     * @param token
     * @return Boolean
     * 延长会话
     */
    @Override
    public Result<Boolean> refreshAccount(String token) {

        Result<Boolean> booleanResult = Result.failure();

        Boolean refreshFlag = false;

        if (StringUtils.isEmpty(token)) {

            booleanResult.data(refreshFlag);

            booleanResult.code(ResultCode.SERVER_ERROR.getCode());

            booleanResult.message("token is null");

            if (log.isInfoEnabled()) {

                log.info("SsoServiceImpl.refreshAccount token is null");

            }

            return booleanResult;
        }

        try {

            token = Constants.SSO_REDIS_KEY_PRIFIX + token;

            refreshFlag = stringRedisTemplate.expire(token, jwtProperties.getTokenExpirationTime(), TimeUnit.DAYS);

            booleanResult.data(refreshFlag);

            booleanResult.message("refresh account success");

            if (log.isInfoEnabled()) {

                log.info("SsoServiceImpl.refreshAccount token : {}", token);

            }

        } catch (Exception e) {

            booleanResult.code(ResultCode.SERVER_ERROR.getCode());

            booleanResult.data(refreshFlag);

            booleanResult.message(e.getMessage());

            if (log.isErrorEnabled()) {

                log.error("SsoServiceImpl.refreshAccount token : {} Exception : {}", token, e);

            }
        }

        return booleanResult;
    }

    /**
     * @param token
     * @return Boolean
     * 注销
     */
    @Override
    public Result<Boolean> logout(String token) {

        Result<Boolean> booleanResult = Result.failure();

        token = Constants.SSO_REDIS_KEY_PRIFIX + token;

        String userId = stringRedisTemplate.opsForValue().get(token);

        if (!StringUtils.isEmpty(userId)) {

            stringRedisTemplate.delete(token);

            booleanResult.code(ResultCode.SUCCESS.getCode());

            booleanResult.data(true);

            log.info("SsoServiceImpl.logout success token : {}", token);

        } else {

            log.info("SsoServiceImpl.logout failure token : {} , userId : {}", token, userId);
        }

        return booleanResult;
    }
}
