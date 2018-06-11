/*
 * @(#)SwaggerProperties.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-resource
 * 创建日期 : 2018年6月6日
 * 修改历史 : 
 *     1. [2018年6月6日]创建文件 by 傅泉明
 */
package com.wangji.security.oauth2;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 加载swagger-api汇总地址
 * @version 
 * @author 傅泉明
 */
@ConfigurationProperties(
        prefix = "swagger.api"
)
public class SwaggerProperties {

    private String names;
    private String locations;
    private String versions;
    public String getNames() {
        return names;
    }
    public void setNames(String names) {
        this.names = names;
    }
    public String getLocations() {
        return locations;
    }
    public void setLocations(String locations) {
        this.locations = locations;
    }
    public String getVersions() {
        return versions;
    }
    public void setVersions(String versions) {
        this.versions = versions;
    }
    
}
