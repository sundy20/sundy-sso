package com.sundy.sso.service;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created on 2017/11/17
 *
 * @author plus.wang
 * @description
 */
@EnableDubbo(scanBasePackages = "com.sundy.sso.service")
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication(scanBasePackages = "com.sundy.sso.service")
public class SsoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SsoApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}

