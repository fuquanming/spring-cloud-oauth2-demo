/*
 * @(#)ShutdownController.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-cloud-common
 * 创建日期 : 2018年6月12日
 * 修改历史 : 
 *     1. [2018年6月12日]创建文件 by 傅泉明
 */
package com.fcc.common.web.controller;

import javax.annotation.PreDestroy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关闭应用
 * @version 
 * @author 傅泉明
 */
@RestController
public class ShutdownController implements ApplicationContextAware {

    private ApplicationContext context;
    
    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     **/
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;
    }
    
    @PostMapping("/shutdownContext")
    public void shutdownContext() {
        ((ConfigurableApplicationContext) context).close();
    }
    
    @PreDestroy
    public void onDestroy() throws Exception {
    }

}
