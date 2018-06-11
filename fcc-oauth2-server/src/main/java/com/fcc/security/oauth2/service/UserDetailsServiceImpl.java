/*
 * @(#)UserDetailsServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-server
 * 创建日期 : 2018年5月30日
 * 修改历史 : 
 *     1. [2018年5月30日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.fcc.security.oauth2.common.PasswordEncoderEnum;

/**
 * 自定义用户查询接口-username是否存在
 * @version 
 * @author 傅泉明
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    public String DEF_USERS_BY_USERNAME_QUERY = "select username,user_pass,user_salt,user_status from sys_user where username = ?";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetails> users = Collections.emptyList(); 
        try {
            users = jdbcTemplate.query(DEF_USERS_BY_USERNAME_QUERY,
                    new String[] { username }, new RowMapper<UserDetails>() {
                        @Override
                        public UserDetails mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            String username = rs.getString(1);
                            String password = rs.getString(2);
                            String salt = rs.getString(3);
                            String status = rs.getString(4);
                            boolean enabled = (status == null || !"1".equals(status)) ? false : true;
                            // 密码设置编码方式
                            return new User(username, PasswordEncoderEnum.md5Hash.name() + ":"+ username + ":" + password + ":" + salt, enabled, true, true, true,
                                    AuthorityUtils.NO_AUTHORITIES);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("loadUserByUsername error", e);
        }
        if (users.size() == 0) {
            throw new UsernameNotFoundException(username);
        }
        UserDetails user = users.get(0); // contains no GrantedAuthority[]
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), user.getAuthorities());
    }

}
