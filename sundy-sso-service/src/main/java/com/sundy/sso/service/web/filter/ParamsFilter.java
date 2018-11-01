package com.sundy.sso.service.web.filter;

import com.alibaba.fastjson.JSON;
import com.sundy.sso.service.config.JwtProperties;
import com.sundy.sso.service.util.CookieUtil;
import com.sundy.sso.service.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 通用拦截  编码处理
 */
@Component
public class ParamsFilter implements Filter {

    @Autowired
    private JwtProperties jwtProperties;

    private static final Logger log = LoggerFactory.getLogger(ParamsFilter.class);

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        ServletContext servletContext = filterConfig.getServletContext();

        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        jwtProperties = applicationContext.getBean("jwtProperties", JwtProperties.class);

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletResponse response = (HttpServletResponse) resp;

        paramsFilter(request, response, chain);

    }

    private void paramsFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        Map<String, Object> requestParamMap = WebUtil.getRequestParamMap(req);

        if (null != requestParamMap && !requestParamMap.isEmpty() && requestParamMap.containsKey("redirect")) {

            String redirectUrl = requestParamMap.get("redirect").toString();

            CookieUtil.create(resp, "redirectKey", redirectUrl, false, false, Integer.MAX_VALUE, jwtProperties.getDomain());
        }

        if (log.isInfoEnabled()) {

            log.info("ParamsFilter.paramsFilter requestParamMap : {} ", JSON.toJSONString(requestParamMap));
        }

        chain.doFilter(req, resp);
    }

}
