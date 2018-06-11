/*
 * @(#)AuthenticationProviderImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-server
 * 创建日期 : 2018年5月30日
 * 修改历史 : 
 *     1. [2018年5月30日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2.service;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * spring-security 登录鉴权-检验登录方式为password时username、password值判断
 * @version 
 * @author 傅泉明
 */
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {
    
    private static final Logger log = LoggerFactory.getLogger(AuthenticationProviderImpl.class);
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @SuppressWarnings("unchecked")
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getDetails() instanceof Map) {
            Map<String, Object> map = (Map<String, Object>)authentication.getDetails();
            if ("password".equals(map.get("grant_type"))) {// password 鉴权，用户名密码必填
                String username = authentication.getName();
                String password = (String) authentication.getCredentials();
                if (StringUtils.isEmpty(username)) throw new InvalidGrantException("username is empty");
                if (StringUtils.isEmpty(password)) throw new InvalidGrantException("password is empty");
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (null == userDetails) {
                    throw new InvalidGrantException("username is error");
                }
                if (!userDetails.isEnabled()) {
                    throw new InvalidGrantException("username is disabled");
                }
                // 校验密码
                if (passwordEncoder.matches(password, userDetails.getPassword()) == false) {
                    log.info(username + "," + password);
                    throw new InvalidGrantException("username or password is error");
                }
            }
        }
        // 返回一个Token对象表示登陆成功
        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(),
                Collections.<GrantedAuthority>emptyList());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
