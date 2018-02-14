package com.songdexv.springboot.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * 获取访问特定URL所需角色
 * Created by songdexv on 2017/8/7.
 */
public class JdbcFilterInvocationDefinitionSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger logger = LoggerFactory
            .getLogger(JdbcFilterInvocationDefinitionSource.class);
    private String defaultRole = "ANONYMOUS";
    private RoleService roleService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String url = ((FilterInvocation) object).getRequestUrl();
        return loadAttribute(url);
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * load URL-ROLE mapping
     */
    public Collection<ConfigAttribute> loadAttribute(String url) {
        Collection<ConfigAttribute> urlRoleConfigAttrs = new ArrayList<ConfigAttribute>();
        List<String> roles = null;
        try {
            roles = roleService.getAllRolesOfURL(url);
        } catch (Exception e) {
            logger.error("数据库查询url-Role出错", e);
        }
        if (roles != null) {
            for (String role : roles) {
                urlRoleConfigAttrs.add(new SecurityConfig(role));
            }
        }

        if (urlRoleConfigAttrs.isEmpty() && !StringUtils.isBlank(defaultRole)) {
            urlRoleConfigAttrs.add(new SecurityConfig(defaultRole));
        }

        return urlRoleConfigAttrs;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }
}
