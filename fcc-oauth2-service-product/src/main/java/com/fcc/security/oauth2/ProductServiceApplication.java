/*
 * @(#)ProductServiceApplication.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : wangji-oauth2-service-product
 * 创建日期 : 2018年5月30日
 * 修改历史 : 
 *     1. [2018年5月30日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring4all.swagger.EnableSwagger2Doc;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@EnableSwagger2Doc // 使用后Swagger2则不用
@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
    
}
