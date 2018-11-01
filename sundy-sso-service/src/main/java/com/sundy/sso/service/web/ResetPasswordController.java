package com.sundy.sso.service.web;

import com.sundy.sso.service.service.UserService;
import com.sundy.sso.service.web.vo.UserVO;
import com.sundy.sso.share.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created on 2017/11/17
 *
 * @author plus.wang
 * @description
 */
@Controller
public class ResetPasswordController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/toresetpwd", method = RequestMethod.GET)
    public String resetPwdHtml() {

        return "resetpassword";
    }

    @RequestMapping(value = "/tosetpwd", method = RequestMethod.GET)
    public String setPwdHtml() {

        return "setpassword";
    }

    @RequestMapping(value = "/resetpwd", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> resetPwd(@RequestBody UserVO userVO) {

        return userService.resetPwd(userVO);
    }
}
