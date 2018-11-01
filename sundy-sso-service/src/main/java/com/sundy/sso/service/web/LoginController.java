package com.sundy.sso.service.web;

import com.sundy.sso.service.service.UserService;
import com.sundy.sso.service.web.vo.UserVO;
import com.sundy.sso.share.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created on 2017/11/17
 *
 * @author plus.wang
 * @description
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/", "/index", "/tologin"}, method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/tologin", method = RequestMethod.GET)
    public String loginHtml() {

        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, String>> login(@RequestBody UserVO userVO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        return userService.login(userVO, null, httpServletRequest, httpServletResponse);

    }

    @RequestMapping(value = "/logintemp", method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, String>> logintemp(@RequestBody UserVO userVO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        return userService.logintemp(userVO, httpServletRequest, httpServletResponse);

    }

    @RequestMapping(value = "/checkregist/{phone}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> checkRegist(@PathVariable String phone) {

        UserVO userVO = new UserVO();

        userVO.setPhone(phone);

        return userService.checkRegist(userVO);

    }
}
