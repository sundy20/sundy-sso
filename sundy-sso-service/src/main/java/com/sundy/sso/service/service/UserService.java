package com.sundy.sso.service.service;

import com.alibaba.fastjson.JSON;
import com.sundy.sso.service.component.PhoneCodeCompent;
import com.sundy.sso.service.component.SignatureComponent;
import com.sundy.sso.service.config.Constants;
import com.sundy.sso.service.config.JwtProperties;
import com.sundy.sso.service.util.CookieUtil;
import com.sundy.sso.service.util.JwtUtil;
import com.sundy.sso.service.util.WebUtil;
import com.sundy.sso.service.validate.ValidateVOUtil;
import com.sundy.sso.service.validate.group.UserLoginGroup;
import com.sundy.sso.service.validate.group.UserRegistGroup;
import com.sundy.sso.service.web.vo.UserVO;
import com.sundy.sso.share.service.Result;
import com.sundy.sso.share.service.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/11/21
 *
 * @author plus.wang
 * @description
 */

@Component
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private SignatureComponent signatureComponent;

    @Value("${oldLoginCheckApi}")
    private String oldLoginApi;

    @Autowired
    private PhoneCodeCompent phoneCodeCompent;

    private static final String MEMBER_ID_RDS_KEY = "member_id_rds_key";

    private static final String MOBILE_DEVICE = "app_";

    private static final String PC_DEVICE = "pc_";

    private static final String SUCCESS_UID_KEY = "login_success_uid_";

    public Result<Map<String, String>> regist(UserVO userVO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        Result<Map<String, String>> mapResult = Result.failure();

        Map<String, String> stringMap = ValidateVOUtil.checkVO(userVO, UserRegistGroup.class);

        if (CollectionUtils.isEmpty(stringMap)) {

            if (!signatureComponent.checkSignature(userVO.getPhone(), userVO.getSignData())) {

                //后台校验失败
                mapResult.code(ResultCode.SERVER_ERROR.getCode());

                mapResult.message("是一个非法的请求");

                return mapResult;
            }


        } else {

            //后台校验失败
            mapResult.code(ResultCode.CLIENT_ERROR.getCode());

            mapResult.data(stringMap);

            mapResult.message(JSON.toJSONString(stringMap));

            log.info("UserService.regist failure message: {}", JSON.toJSONString(stringMap));

        }

        return mapResult;
    }

    public Result<Map<String, String>> login(UserVO userVO, Result<Map<String, String>> mapResultRegist, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        Result<Map<String, String>> mapResult = Result.failure();

        Map<String, String> checkMap = ValidateVOUtil.checkVO(userVO, UserLoginGroup.class);

        if (CollectionUtils.isEmpty(checkMap)) {

            String countStr = stringRedisTemplate.opsForValue().get(String.format(jwtProperties.getLoginPrefix(), userVO.getPhone()));

            int loginCount = jwtProperties.getLoginCount();

            int realCount = Integer.valueOf(StringUtils.isEmpty(countStr) ? "0" : countStr);

            if (realCount > loginCount) {

                if (!signatureComponent.checkSignature(userVO.getPhone(), userVO.getSignData())) {

                    //后台校验失败
                    mapResult.code(ResultCode.SERVER_ERROR.getCode());

                    mapResult.message("是一个非法的请求");

                    return mapResult;
                }
            }

        } else {

            //后台校验失败
            mapResult.code(ResultCode.SERVER_ERROR.getCode());

            mapResult.data(checkMap);

            mapResult.message(JSON.toJSONString(checkMap));

            log.info("UserService.login failure message: {}", JSON.toJSONString(checkMap));

        }

        return mapResult;

    }

    private void loginFailure(UserVO userVO, Result<Map<String, String>> mapResult) {

        Map<String, String> checkMap = new HashMap<>(16);

        String countStr = stringRedisTemplate.opsForValue().get(String.format(jwtProperties.getLoginPrefix(), userVO.getPhone()));

        int loginCount = jwtProperties.getLoginCount();

        int realCount = Integer.valueOf(StringUtils.isEmpty(countStr) ? "0" : countStr);

        if (realCount < loginCount) {

            realCount++;

            stringRedisTemplate.opsForValue().set(String.format(jwtProperties.getLoginPrefix(), userVO.getPhone()), realCount + "", 24, TimeUnit.HOURS);

            checkMap.put("captchaSign", "false");

            log.info("UserService.login failure loginFailedCounts: {}", realCount);

        } else {
            //登录两次输错密码
            checkMap.put("captchaSign", "true");

            log.info("UserService.login failure loginFailedCounts: {}", realCount);

        }

        mapResult.data(checkMap);

        mapResult.code(ResultCode.SERVER_ERROR.getCode());

        mapResult.message("账号或者密码错误");
    }

    public Result<Map<String, String>> logintemp(UserVO userVO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        Result<Map<String, String>> mapResult = Result.failure();

        Result<String> stringResult = phoneCodeCompent.checkPhoneCode(PhoneCodeCompent.LOGINTEMP_TEMPLATE_NO + ":" + userVO.getPhone(), userVO.getCode());

        if (stringResult.succeed()) {

            Result<Boolean> checkRegist = checkRegist(userVO);

            if (checkRegist.succeed()) {

            }
        }

        return mapResult.message("验证码输入有误");
    }

    private void loginSuccess(UserVO userVO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Result<Map<String, String>> mapResult) {

        Map<String, String> checkMap = new HashMap<>(16);

        StringBuilder sb = new StringBuilder();

        if (WebUtil.isMobileDevice(httpServletRequest)) {
            //移动端登录
            sb.append(SUCCESS_UID_KEY).append(MOBILE_DEVICE).append(userVO.getPhone());

        } else {
            //pc端登录
            sb.append(SUCCESS_UID_KEY).append(PC_DEVICE).append(userVO.getPhone());
        }

        String successKey = sb.toString();

        String token1 = stringRedisTemplate.opsForValue().get(successKey);

        if (!StringUtils.isEmpty(token1)) {

            stringRedisTemplate.delete(token1);
        }

        token1 = JwtUtil.generateToken(jwtProperties.getTokenSigningKey(), userVO.getPhone());

        String token = Constants.SSO_REDIS_KEY_PRIFIX + token1;

        stringRedisTemplate.opsForValue().set(successKey, token, jwtProperties.getTokenExpirationTime(), TimeUnit.DAYS);

        CookieUtil.create(httpServletResponse, jwtProperties.getName(), token1, false, false, Integer.MAX_VALUE, jwtProperties.getDomain());

        stringRedisTemplate.opsForValue().set(token, userVO.getPhone() + "", jwtProperties.getTokenExpirationTime(), TimeUnit.DAYS);

        stringRedisTemplate.opsForValue().set(String.format(jwtProperties.getLoginPrefix(), userVO.getPhone()), "0", 24, TimeUnit.HOURS);

        checkMap.put(jwtProperties.getName(), token1);

        mapResult.message("phone and password checked successfully!");

        mapResult.code(ResultCode.SUCCESS.getCode()).data(checkMap);

        log.info("UserService.login success phone: {} , username: {}", userVO.getPhone(), userVO.getNickName());
    }

    public Result<Boolean> checkRegist(UserVO userVO) {

        Result<Boolean> mapResult = Result.failure();


        return mapResult;

    }

    public Result<Boolean> resetPwd(UserVO userVO) {

        Result<Boolean> mapResult = Result.failure();

        if (!StringUtils.isEmpty(userVO.getPhone()) && !signatureComponent.checkSignature(userVO.getPhone(), userVO.getSignData())) {

            //后台校验失败
            mapResult.code(ResultCode.SERVER_ERROR.getCode());

            mapResult.message("是一个非法的请求");

            return mapResult;
        }


        return mapResult;
    }

    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        String redirect = CookieUtil.getValue(httpServletRequest, "redirectKey");

        String token = Constants.SSO_REDIS_KEY_PRIFIX + CookieUtil.getValue(httpServletRequest, jwtProperties.getName());

        String userId = stringRedisTemplate.opsForValue().get(token);

        if (!StringUtils.isEmpty(userId)) {

            stringRedisTemplate.delete(token);

            CookieUtil.clear(httpServletResponse, jwtProperties.getName(), jwtProperties.getDomain());

            log.info("UserService.logout success token : {}", token);

        } else {

            log.info("UserService.logout failure token : {} , userId : {}", token, userId);
        }

        try {

            httpServletResponse.sendRedirect(redirect);

            log.info("UserService.logout redirect url : {}", redirect);

        } catch (IOException e) {

            log.info("UserService.logout failure token : {}", token, e);
        }
    }

    public Result<String> restLogout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        Result<String> stringResult = Result.failure();

        String token = getToken(httpServletRequest);

        if (WebUtil.isAJAX(httpServletRequest) || WebUtil.isWEIXIN(httpServletRequest)) {

            logout(token);

            CookieUtil.clear(httpServletResponse, jwtProperties.getName(), jwtProperties.getDomain());

            stringResult.code(ResultCode.SUCCESS.getCode());

            stringResult.message("注销成功");
        }

        return stringResult;
    }

    private void logout(String token) {

        String userId = stringRedisTemplate.opsForValue().get(token);

        if (!StringUtils.isEmpty(userId)) {

            stringRedisTemplate.delete(token);

            log.info("UserService.logout success token : {}", token);

        }
    }

    private String getToken(HttpServletRequest httpServletRequest) {

        String token = Constants.SSO_REDIS_KEY_PRIFIX + CookieUtil.getValue(httpServletRequest, jwtProperties.getName());

        if (StringUtils.isEmpty(token)) {

            if (WebUtil.isAJAX(httpServletRequest) || WebUtil.isWEIXIN(httpServletRequest)) {

                token = Constants.SSO_REDIS_KEY_PRIFIX + CookieUtil.getAjaxHeaderValue(httpServletRequest, "x-requested-with");

                if ("XMLHttpRequest".equalsIgnoreCase(token)) {

                    return "";
                }
            }
        }

        return token;
    }
}
