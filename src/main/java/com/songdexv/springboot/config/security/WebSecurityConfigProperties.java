package com.songdexv.springboot.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by songdexv on 2017/8/7.
 */
@ConfigurationProperties(prefix = "web.security", ignoreUnknownFields = true)
public class WebSecurityConfigProperties {
    private boolean autoConfig = false;
    private String loginUrl;
    private String csrfHttpMethod;

    public boolean isAutoConfig() {
        return autoConfig;
    }

    public void setAutoConfig(boolean autoConfig) {
        this.autoConfig = autoConfig;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getCsrfHttpMethod() {
        return csrfHttpMethod;
    }

    public void setCsrfHttpMethod(String csrfHttpMethod) {
        this.csrfHttpMethod = csrfHttpMethod;
    }
}
