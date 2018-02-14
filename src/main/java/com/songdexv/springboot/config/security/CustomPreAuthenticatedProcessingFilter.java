package com.songdexv.springboot.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

/**
 * 基于统一认证中心的身份预验证处理过滤器
 * 用户已经通过统一认证中心登录过了，这里直接取头里的信息
 * Created by songdexv on 2017/8/7.
 */
public class CustomPreAuthenticatedProcessingFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory
            .getLogger(
                    CustomPreAuthenticatedProcessingFilter.class);
    private SessionDataGetter sessionDataGetter;
    private AuthenticationManager authenticationManager;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
            new WebAuthenticationDetailsSource();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //检验用户是否登录，登录成功向SecurityContext中写入身份信息，否则

    }

    public SessionDataGetter getSessionDataGetter() {
        return sessionDataGetter;
    }

    public void setSessionDataGetter(SessionDataGetter sessionDataGetter) {
        this.sessionDataGetter = sessionDataGetter;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationDetailsSource<HttpServletRequest, ?> getAuthenticationDetailsSource() {
        return authenticationDetailsSource;
    }

    public void setAuthenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
    }
}
