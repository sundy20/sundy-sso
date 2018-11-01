package com.sundy.sso.service.web.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * 通用拦截  编码处理
 */
@Component
public class CommonFilter implements Filter {

    @Autowired
    private ExecutorService executorService;

    @Override
    public void destroy() {

        //释放线程池资源 异步
        executorService.shutdown();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletResponse response = (HttpServletResponse) resp;

        encodeFilter(request, response, chain);

    }

    private void encodeFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");

        resp.setCharacterEncoding("UTF-8");

        chain.doFilter(req, resp);
    }

}
