package com.sundy.sso.service.web;

import com.sundy.sso.service.component.PhoneCodeCompent;
import com.sundy.sso.share.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2017/11/20
 *
 * @author plus.wang
 * @description
 */
@RestController
public class PhoneCodeController {

    @Autowired
    private PhoneCodeCompent phoneCodeCompent;

    @RequestMapping(value = "/phonecode/get", method = RequestMethod.GET)
    public String getPhoneCode(String phone) {

        return phoneCodeCompent.getPhoneCode(phone);
    }

    @RequestMapping(value = "/phonecode/set", method = RequestMethod.GET)
    public Boolean setPhoneCode(String phone, Integer sign) {

        return phoneCodeCompent.setPhoneCode(phone, sign);
    }

    /**
     * 校验手机验证码
     */
    @RequestMapping(value = "/phonecode/valid", method = RequestMethod.GET)
    public Result<String> checkPhoneCode(String phone, String phonecode) {

        return phoneCodeCompent.checkPhoneCode(phone, phonecode);
    }
}
