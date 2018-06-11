/*
 * @(#)PasswordEncoderEnum.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-server
 * 创建日期 : 2018年5月30日
 * 修改历史 : 
 *     1. [2018年5月30日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2.common;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public enum PasswordEncoderEnum {
    /** 比较字符串 */
    equals("equals"),
    /** Md5Hash */
    md5Hash("md5Hash");
    
    private final String info;
    
    private PasswordEncoderEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
