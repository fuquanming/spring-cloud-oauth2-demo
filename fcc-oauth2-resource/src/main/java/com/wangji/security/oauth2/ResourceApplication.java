/*
 * @(#)ResourceApplication.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-resource
 * 创建日期 : 2018年5月30日
 * 修改历史 : 
 *     1. [2018年5月30日]创建文件 by 傅泉明
 */
package com.wangji.security.oauth2;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.wangji.security.oauth2.filter.OAuth2HeaderFilter;
import com.wangji.security.oauth2.service.RedisTokenStoreImpl;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

//import com.wangji.security.oauth2.service.RedisTokenStoreImpl;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@SpringBootApplication
@Configuration
@EnableResourceServer
@RestController
@EnableZuulProxy // zuul
@EnableSwagger2Doc // Swagger2
public class ResourceApplication extends ResourceServerConfigurerAdapter {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    
    // zuul 重定向api接口
    @Bean
    public OAuth2HeaderFilter oAuth2HeaderFilter() {
        return new OAuth2HeaderFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }

    /** 需要鉴权的URL地址 */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/api/**").authenticated();
        //.antMatchers(HttpMethod.POST, "/foo").hasAuthority("FOO_WRITE");
    }

    /** 设置鉴权token的来源 */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // resourceId 在数据库oauth_client_details中的资源resource_Ids
//        resources.resourceId("oauth2-resource").tokenStore((new RedisTokenStore(redisConnectionFactory)));// spring-boot
        resources.resourceId("oauth2-resource").tokenStore((new RedisTokenStoreImpl(redisConnectionFactory)));// spring-cloud
    }
    
    /**
     * 将swagger-api汇总
     * 
     * @version 
     * @author 傅泉明
     */
    @Component
    @EnableConfigurationProperties(SwaggerProperties.class)
    @Primary
    class DocumentationConfig implements SwaggerResourcesProvider {
        
        @Autowired
        private SwaggerProperties swaggerProperties;
        
        @Override
        public List<SwaggerResource> get() {
            List<SwaggerResource> resources = new ArrayList<SwaggerResource>();
            // 加载配置文件
//            resources.add(swaggerResource("service-user", "http://localhost:60011/v2/api-docs", "2.0"));
//            resources.add(swaggerResource("service-product", "http://localhost:60012/v2/api-docs", "2.0"));
            if (StringUtils.isNotEmpty(swaggerProperties.getNames())) {
                String[] names = StringUtils.split(swaggerProperties.getNames(), ",");
                String[] locations = StringUtils.split(swaggerProperties.getLocations(), ",");
                String[] versions = StringUtils.split(swaggerProperties.getVersions(), ",");
                int length = names.length;
                for (int i = 0; i < length; i++) {
                    resources.add(swaggerResource(names[i], locations[i], versions[i]));
                }
            }
            return resources;
        }

        private SwaggerResource swaggerResource(String name, String location, String version) {
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setName(name);
            swaggerResource.setLocation(location);
            swaggerResource.setSwaggerVersion(version);
            return swaggerResource;
        }
    }
}
