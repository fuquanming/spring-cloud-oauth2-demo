/*
 * @(#)Swagger2.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-service-user
 * 创建日期 : 2018年6月4日
 * 修改历史 : 
 *     1. [2018年6月4日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fcc.security.oauth2.web"))
                .paths(PathSelectors.any())
//                .pathMapping("/sss") // 请求统一前缀，如果有应用名称
                .build()
                ;
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2-使用io.springfox")
                .description("使用Java编码设置ApiInfo")
                .termsOfServiceUrl("https://github.com/fuquanming/spring-cloud-oauth2-demo")
                .contact(new Contact("傅泉明", "http://api.com", "67837343@qq.com"))
                .version("1.0")
                .build();
    }
}
