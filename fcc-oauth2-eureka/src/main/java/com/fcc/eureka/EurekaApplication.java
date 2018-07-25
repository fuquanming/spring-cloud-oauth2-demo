/*
 * @(#)EurekaApplication.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-eureka
 * 创建日期 : 2018年7月12日
 * 修改历史 : 
 *     1. [2018年7月12日]创建文件 by 傅泉明
 */
package com.fcc.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka注册中心
 * @version 
 * @author 傅泉明
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
    
}
