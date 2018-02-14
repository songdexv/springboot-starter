package com.songdexv.springboot.config.security;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 将csrf的值写到response的header中
 * <p>
 * Created by songdexv on 2017/8/7.
 */
public class ResponseInjectCsrfHeaderFilter extends OncePerRequestFilter {
    private WebSecurityConfigProperties securityConfigProperties;

    public ResponseInjectCsrfHeaderFilter(WebSecurityConfigProperties securityConfigProperties) {
        this.securityConfigProperties = securityConfigProperties;
    }

    private Pattern allowedMethods = getAllowedMethods();

    private Pattern getAllowedMethods() {
        try {
            String responseInjectCsrfHeaderMethod = securityConfigProperties.getCsrfHttpMethod();
            if (StringUtils.isNotBlank(responseInjectCsrfHeaderMethod)) {
                return Pattern.compile(responseInjectCsrfHeaderMethod);
            }
        } catch (Exception e) {
            logger.warn("获取向response中种csrf.header的method配置错误", e);
        }
        //返回默认
        return Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    }

    private boolean matches(HttpServletRequest request) {
        return allowedMethods.matcher(request.getMethod()).matches();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (matches(request)) {
                Object csrfTokenObj = request.getAttribute(CsrfToken.class.getName());
                if (csrfTokenObj != null && csrfTokenObj instanceof CsrfToken) {
                    CsrfToken csrfToken = (CsrfToken) csrfTokenObj;
                    response.addHeader(csrfToken.getHeaderName(), csrfToken.getToken());
                }
            }
        } catch (Exception e) {
            logger.error("response中添加csrf的token值失败", e);
        }
        filterChain.doFilter(request, response);
    }
}
