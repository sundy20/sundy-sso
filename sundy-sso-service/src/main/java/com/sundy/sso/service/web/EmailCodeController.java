package com.sundy.sso.service.web;

import com.sundy.sso.service.component.EmailCodeCompent;
import com.sundy.sso.share.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author plus.wang
 * @description 邮箱验证码控制器
 * @date 2018/8/4
 */
@RestController
public class EmailCodeController {

    @Autowired
    private EmailCodeCompent emailCodeCompent;

    @RequestMapping(value = "/emailcode/get", method = RequestMethod.GET)
    public String getPhoneCode(String email) {

        return emailCodeCompent.getEmailCode(email);
    }

    @RequestMapping(value = "/emailcode/regist", method = RequestMethod.GET)
    public Result<Boolean> setRegistEmailCode(String email) {

        return emailCodeCompent.setEmailCode(email, 1);
    }

    @RequestMapping(value = "/emailcode/reset", method = RequestMethod.GET)
    public Result<Boolean> setResetEmailCode(String email) {

        return emailCodeCompent.setEmailCode(email, 2);
    }

    /**
     * 校验邮箱验证码
     */
    @RequestMapping(value = "/emailcode/valid", method = RequestMethod.GET)
    public Result<String> checkPhoneCode(String email, String emailcode) {

        return emailCodeCompent.checkEmailCode(email, emailcode);
    }
}
