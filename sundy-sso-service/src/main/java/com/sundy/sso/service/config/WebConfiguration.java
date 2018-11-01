package com.sundy.sso.service.config;

import com.sundy.sso.service.web.filter.CommonFilter;
import com.sundy.sso.service.web.filter.CorsFilter;
import com.sundy.sso.service.web.filter.ParamsFilter;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @desc web 配置
 */
@Configuration
public class WebConfiguration {

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/404.html");
            ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/error/404.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.html");
            ErrorPage error504Page = new ErrorPage(HttpStatus.GATEWAY_TIMEOUT, "/error/500.html");

            container.addErrorPages(error401Page, error400Page, error404Page, error500Page, error504Page);
        });
    }

    /**
     * @param commonFilter 拦截所有请求
     */
    @Bean
    FilterRegistrationBean commonFilterRegistration(CommonFilter commonFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(commonFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * @param corsFilter 支持跨域
     */
    @Bean
    FilterRegistrationBean corsFilterRegistration(CorsFilter corsFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(corsFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * @param paramsFilter redirect 记录
     */
    @Bean
    FilterRegistrationBean paramsFilterRegistration(ParamsFilter paramsFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(paramsFilter);
        registration.addUrlPatterns("/tologin");
        registration.addUrlPatterns("/toregist");
        registration.addUrlPatterns("/logout");
        return registration;
    }

    @Bean
    public ExecutorService singleThreadPool() {

        return Executors.newSingleThreadExecutor();
    }

}
