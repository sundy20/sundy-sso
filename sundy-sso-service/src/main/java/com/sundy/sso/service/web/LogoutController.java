package com.sundy.sso.service.web;

import com.sundy.sso.service.service.UserService;
import com.sundy.sso.share.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2017/11/17
 *
 * @author plus.wang
 * @description
 */
@RestController
public class LogoutController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        userService.logout(httpServletRequest, httpServletResponse);

    }

    @RequestMapping(value = "/rest/logout", method = RequestMethod.GET)
    public Result<String> restLogout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        return userService.restLogout(httpServletRequest, httpServletResponse);

    }
}
