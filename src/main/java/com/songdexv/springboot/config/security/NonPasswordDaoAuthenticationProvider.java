package com.songdexv.springboot.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by songdexv on 2017/8/7.
 */
public class NonPasswordDaoAuthenticationProvider extends DaoAuthenticationProvider {
    /**
     * 覆盖Provider,不进行密码校验
     *
     * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider#additionalAuthenticationChecks
     * (org.springframework.security.core.userdetails.UserDetails, org.springframework.security.authentication
     * .UsernamePasswordAuthenticationToken)
     */
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

    }
}
