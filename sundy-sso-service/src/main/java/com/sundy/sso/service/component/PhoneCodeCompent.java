package com.sundy.sso.service.component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sundy.sms.share.dto.PhoneRequestDTO;
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
 * Created on 2017/11/20
 *
 * @author plus.wang
 * @description
 */

@Component
public class PhoneCodeCompent {

    private static final Logger log = LoggerFactory.getLogger(PhoneCodeCompent.class);

    private static final String REGIST_TEMPLATE_NO = "SMS_119091568";

    private static final String RESET_TEMPLATE_NO = "SMS_119091571";

    public static final String LOGINTEMP_TEMPLATE_NO = "SMS_129758793";

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Reference(version = "1.0.0", check = false, timeout = 10000)
    private SmsService smsService;

    @Autowired
    private SignatureComponent signatureComponent;

    @Autowired
    private ExecutorService executorService;

    /**
     * @param phone 随机生成手机验证码4位
     */
    public Boolean setPhoneCode(String phone, Integer sign) {

        PhoneRequestDTO phoneRequestDTO = new PhoneRequestDTO();

        if (1 == sign) {

            phoneRequestDTO.setTemplateNo(REGIST_TEMPLATE_NO);

        } else if (2 == sign) {

            phoneRequestDTO.setTemplateNo(RESET_TEMPLATE_NO);

        } else if (3 == sign) {

            phoneRequestDTO.setTemplateNo(LOGINTEMP_TEMPLATE_NO);
        }

        if (redisLimit(phone, phoneRequestDTO.getTemplateNo())) {

            String code = String.valueOf(randomCode());

            if (3 == sign) {

                stringRedisTemplate.opsForValue().set(LOGINTEMP_TEMPLATE_NO + ":" + phone, code, 600, TimeUnit.SECONDS);

            } else {

                stringRedisTemplate.opsForValue().set(phone, code, 600, TimeUnit.SECONDS);
            }

            phoneRequestDTO.setMobile(phone);

            Map<String, Object> stringObjectMap = new HashMap<>();

            stringObjectMap.put("code", code);

            phoneRequestDTO.setParams(stringObjectMap);

            asycSMS(phoneRequestDTO);

            log.info("PhoneCodeCompent.setPhoneCode  phone : {}  code : {}", phone, code);

            return true;

        } else {

            return false;
        }
    }

    private boolean redisLimit(String phone, String templateNo) {

        String mills = stringRedisTemplate.opsForValue().get(Constants.SSO_REDIS_KEY_PRIFIX + phone + "_" + templateNo);

        long now = System.currentTimeMillis();

        if (StringUtils.isEmpty(mills)) {

            stringRedisTemplate.opsForValue().set(Constants.SSO_REDIS_KEY_PRIFIX + phone + "_" + templateNo, now + "", Constants.SSO_SMS_RATE, TimeUnit.MILLISECONDS);

            return true;

        } else {

            long lastResetTime = Long.parseLong(mills);

            if (now > lastResetTime + Constants.SSO_SMS_RATE) {

                stringRedisTemplate.opsForValue().set(Constants.SSO_REDIS_KEY_PRIFIX + phone + "_" + templateNo, now + "", Constants.SSO_SMS_RATE, TimeUnit.MILLISECONDS);

                return true;
            }
        }

        return false;
    }

    //异步发送短信
    private void asycSMS(PhoneRequestDTO phoneRequestDTO) {

        executorService.submit(() -> {

            smsService.sendMsg(phoneRequestDTO);
        });

    }

    /**
     * @param phone 获取手机验证码
     */
    public String getPhoneCode(String phone) {

        return stringRedisTemplate.opsForValue().get(phone);
    }

    /**
     * @param phone     手机号
     * @param phoneCode 检验手机验证码
     */
    public Result<String> checkPhoneCode(String phone, String phoneCode) {

        Result<String> booleanResult = Result.failure();

        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(phoneCode)) {

            booleanResult.code(ResultCode.CLIENT_ERROR.getCode());

            booleanResult.message("手机号或者验证码不正确");

            return booleanResult;
        }

        String code = stringRedisTemplate.opsForValue().get(phone);

        if (StringUtils.isEmpty(code)) {

            booleanResult.code(ResultCode.CLIENT_ERROR.getCode());

            booleanResult.message("验证码已失效");

            return booleanResult;

        }

        if (code.equals(phoneCode)) {

            booleanResult.data(signatureComponent.genData(phone));

            booleanResult.message("phoneCode checked successfully");

        } else {

            booleanResult.code(ResultCode.CLIENT_ERROR.getCode());

            booleanResult.message("验证码错误");
        }

        return booleanResult;
    }


    private int randomCode() {

        return (int) ((Math.random() * 9 + 1) * 1000);
    }

}
