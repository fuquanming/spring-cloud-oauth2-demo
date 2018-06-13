/*
 * @(#)OAuth2ServerConfig.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-server
 * 创建日期 : 2018年5月30日
 * 修改历史 : 
 *     1. [2018年5月30日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import com.fcc.security.oauth2.service.ClientDetailsServiceImpl;
import com.fcc.security.oauth2.service.RedisTokenStoreImpl;

/**
 * 可以使用grant_type方式为client_credentials,refresh_token,password
 * 参见 AuthenticationProviderImpl 拦截username,password
 * @version 
 * @author 傅泉明
 */
@Configuration
public class OAuth2ServerConfig {

    private static final String RESOURCE_ID = "oauth2-resource";
    
    @Autowired
    private DataSource dataSource;
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
    @Bean
    public ClientDetailsService clientDetails() {
        ClientDetailsServiceImpl service = new ClientDetailsServiceImpl();
        return service;
    }
    /** 测试：拦截访问的资源 */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(RESOURCE_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                    .antMatchers("/api/**").authenticated();//配置api访问控制，必须认证过后才可以访问

        }
    }

    /**
     * 设置鉴权配置：authenticationManager，redis，userDetailsService，clientDetailsService
     * 
     * @version 
     * @author 傅泉明
     */
    @Configuration
    @EnableAuthorizationServer
    protected class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        RedisConnectionFactory redisConnectionFactory;
        @Autowired
        UserDetailsService userDetailsService;
        @Autowired
        ClientDetailsService clientDetailsService;
        
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints
//                    .tokenStore(new RedisTokenStore(redisConnectionFactory))// spring-boot
                    .tokenStore(new RedisTokenStoreImpl(redisConnectionFactory))// spring-cloud
                    .authenticationManager(authenticationManager)
                    .userDetailsService(userDetailsService)
                    .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
            //允许表单认证
            oauthServer.allowFormAuthenticationForClients();
        }
        
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientDetails());
        }
    }
}
