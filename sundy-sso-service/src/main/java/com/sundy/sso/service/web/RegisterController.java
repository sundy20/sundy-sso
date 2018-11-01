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
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/toregist", method = RequestMethod.GET)
    public String registHtml() {

        return "register";
    }

    @RequestMapping(value = "/protocol", method = RequestMethod.GET)
    public String protocolHtml() {

        return "protocol";
    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, String>> regist(@RequestBody UserVO userVO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        return userService.regist(userVO, httpServletRequest, httpServletResponse);
    }
}
