package com.songdexv.springboot.config.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.songdexv.springboot.constant.Constant;
import com.google.common.collect.Lists;

/**
 * Created by songdexv on 2017/8/7.
 */
public class NonPasswordUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory
            .getLogger(NonPasswordUserDetailsService.class);
    protected final MessageSourceAccessor messages = SpringSecurityMessageSource
            .getAccessor();
    /**
     * 如果user-role不存在，则添加默认user-role
     */
    private boolean addDefaultRoleIfNotExist = true;
    /**
     * 默认的角色名
     */
    private String defaultRoleCode = "DEFAULT";

    /**
     * 默认密码
     */
    private String defaultPassword = "";
    private String defaultRole = "ANONYMOUS";
    /**
     * 查询role的service
     */
    private RoleService roleService;

    /**
     * 通过用户查询角色，若角色为空，则抛出 UsernameNotFoundException 异常
     *
     * @param username
     *
     * @return
     *
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> dbAuths = null;
        dbAuths = getGrantedAuthorityByUserName(username);
        if (null == dbAuths || dbAuths.size() == 0) {
            logger.debug("User '" + username
                    + "' has no authorities and will be treated as 'not found'");
            dbAuths = Lists.newArrayList();
        }
        return new User(username, defaultPassword, true, true, true, true, dbAuths);
    }

    /**
     * 创建GrantedAuthority
     *
     * @param rolename
     *
     * @return
     */
    protected GrantedAuthority newGrantedAuthority(String rolename) {
        String roleName = Constant.ROLE_PREFIX + rolename;
        if (StringUtils.isBlank(roleName.trim())) {
            return null;
        }
        return new SimpleGrantedAuthority(roleName);
    }

    /**
     * 通过username查询Role，并封装成 List<GrantedAuthority>
     *
     * @param username
     *
     * @return
     */
    private List<GrantedAuthority> getGrantedAuthorityByUserName(String username) {
        if (StringUtils.isBlank(username)) {
            return new ArrayList<GrantedAuthority>();
        }
        List<String> roles = loadUserRolesAndAddDefaultIfNull(username,
                addDefaultRoleIfNotExist ? this.defaultRoleCode : null);

        Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
        if (roles != null && !roles.isEmpty()) {
            for (String role : roles) {
                GrantedAuthority auth = newGrantedAuthority(role);
                if (auth != null) {
                    dbAuthsSet.add(auth);
                }
            }
        }
        if (!StringUtils.isBlank(defaultRole)) {
            dbAuthsSet.add(newGrantedAuthority(defaultRole));
        }
        return new ArrayList<GrantedAuthority>(dbAuthsSet);
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    /**
     * 获取用户对应的Roles
     *
     * @param userName
     * @param defaultRoleName
     *
     * @return
     */
    protected List<String> loadUserRolesAndAddDefaultIfNull(String userName, String defaultRoleName) {
        return roleService.getAllRolesOfUserAndAddDefaultIfNull(userName, defaultRoleName);
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setDefaultRoleCode(String defaultRoleCode) {
        this.defaultRoleCode = defaultRoleCode;
    }

    public void setAddDefaultRoleIfNotExist(boolean addDefaultRoleIfNotExist) {
        this.addDefaultRoleIfNotExist = addDefaultRoleIfNotExist;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }
}
