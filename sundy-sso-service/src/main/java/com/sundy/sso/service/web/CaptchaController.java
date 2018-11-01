package com.sundy.sso.service.web;

import com.sundy.sso.service.component.CaptchaComponent;
import com.sundy.sso.service.config.JwtProperties;
import com.sundy.sso.share.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2017/11/20
 *
 * @author plus.wang
 * @description
 */
@RestController
public class CaptchaController {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private CaptchaComponent captchaComponent;

    /**
     * 生成图片验证码
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void genCaptcha(String phone, HttpServletResponse resp) {

        String captchaKey = String.format(jwtProperties.getCaotchaKey(), phone);

        captchaComponent.genCaptcha(captchaKey, resp);
    }

    /**
     * 查询图片验证码
     */
    @RequestMapping(value = "/captcha/valid", method = RequestMethod.GET)
    public Result<String> findCaptcha(String phone, String captchaCode) {

        String captchaKey = String.format(jwtProperties.getCaotchaKey(), phone);

        return captchaComponent.checkCaptcha(captchaKey, captchaCode);
    }
}
