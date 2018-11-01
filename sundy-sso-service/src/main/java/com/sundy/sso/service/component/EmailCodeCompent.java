package com.sundy.sso.service.component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sundy.sms.share.dto.EmailRequestDTO;
import com.sundy.sms.share.service.SmsService;
import com.sundy.sso.service.config.Constants;
import com.sundy.sso.share.service.Result;
import com.sundy.sso.share.service.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author plus.wang
 * @description 邮箱验证码组件
 * @date 2018/8/4
 */
@Component
public class EmailCodeCompent {

    private static final Logger log = LoggerFactory.getLogger(EmailCodeCompent.class);

    private static final String EMAIL_REGIST_TEMPLATE_NO = "registEmail";

    private static final String EMAIL_RESET_TEMPLATE_NO = "resetEmail";

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Reference(version = "1.0.0", check = false, timeout = 10000)
    private SmsService smsService;

    @Autowired
    private SignatureComponent signatureComponent;

    @Autowired
    private ExecutorService executorService;

    private static final int EMAIL_CODE_SECONDS = 900;

    /**
     * @param email 随机生成邮箱验证码4位
     */
    public Result<Boolean> setEmailCode(String email, Integer sign) {

        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();

        if (1 == sign) {

            emailRequestDTO.setTemplateNo(EMAIL_REGIST_TEMPLATE_NO);

        } else if (2 == sign) {

            emailRequestDTO.setTemplateNo(EMAIL_RESET_TEMPLATE_NO);

        }

        if (redisLimit(email, emailRequestDTO.getTemplateNo())) {

            String code = String.valueOf(randomCode());

            stringRedisTemplate.opsForValue().set(email, code, EMAIL_CODE_SECONDS, TimeUnit.SECONDS);

            emailRequestDTO.setEmail(email);

            Map<String, Object> stringObjectMap = new HashMap<>();

            stringObjectMap.put("code", code);

            emailRequestDTO.setParams(stringObjectMap);

            asycSMS(emailRequestDTO);

            log.info("EmailCodeCompent.setEmailCode  email : {}  code : {}", email, code);

            return Result.success(ResultCode.SUCCESS.getCode(), "A verification code has been sent to your email");

        } else {

            return Result.success(ResultCode.SUCCESS.getCode(), "A verification code has been sent to your email");
        }
    }

    private boolean redisLimit(String email, String templateNo) {

        String mills = stringRedisTemplate.opsForValue().get(Constants.SSO_REDIS_KEY_PRIFIX + email + "_" + templateNo);

        long now = System.currentTimeMillis();

        if (StringUtils.isEmpty(mills)) {

            stringRedisTemplate.opsForValue().set(Constants.SSO_REDIS_KEY_PRIFIX + email + "_" + templateNo, now + "", Constants.SSO_SMS_RATE, TimeUnit.MILLISECONDS);

            return true;

        } else {

            long lastResetTime = Long.parseLong(mills);

            if (now > lastResetTime + Constants.SSO_SMS_RATE) {

                stringRedisTemplate.opsForValue().set(Constants.SSO_REDIS_KEY_PRIFIX + email + "_" + templateNo, now + "", Constants.SSO_SMS_RATE, TimeUnit.MILLISECONDS);

                return true;
            }
        }

        return false;
    }

    //异步发送邮件
    private void asycSMS(EmailRequestDTO emailRequestDTO) {

        executorService.submit(() -> {

            smsService.sendEmail(emailRequestDTO);
        });

    }

    /**
     * @param email 获取邮件验证码
     */
    public String getEmailCode(String email) {

        return stringRedisTemplate.opsForValue().get(email);
    }

    /**
     * @param email     邮箱
     * @param phoneCode 检验邮箱验证码
     */
    public Result<String> checkEmailCode(String email, String phoneCode) {

        Result<String> booleanResult = Result.failure();

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(phoneCode)) {

            booleanResult.code(ResultCode.CLIENT_ERROR.getCode()).message("Verification code is incorrect");

            return booleanResult;
        }

        String code = stringRedisTemplate.opsForValue().get(email);

        if (StringUtils.isEmpty(code)) {

            booleanResult.code(ResultCode.CLIENT_ERROR.getCode()).message("Verification code is incorrect");

            return booleanResult;

        }

        if (code.equals(phoneCode)) {

            booleanResult.data(signatureComponent.genData(email));

            booleanResult.message("verification code checked successfully");

        } else {

            booleanResult.code(ResultCode.CLIENT_ERROR.getCode()).message("Verification code is incorrect");
        }

        return booleanResult;
    }


    private int randomCode() {

        return (int) ((Math.random() * 9 + 1) * 1000);
    }

}
