package com.songdexv.springboot.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * 检测用户是否登录，若未登录，跳转至登录页面（可调用统一用户中心服务）
 * <p>
 * Created by songdexv on 2017/8/7.
 */
public class CustomEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory
            .getLogger(CustomEntryPoint.class);
    private String loginUrl;
    private String encoding = "UTF-8";

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String currentUrl = ServletUriComponentsBuilder.fromRequest(request).build().normalize()
                .encode(encoding).toString();
        String targetUrl = currentUrl + ";" + loginUrl;
        logger.info("即将重定向Passport进行登录，URL为{}", targetUrl);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.sendRedirect(targetUrl);
    }
}
