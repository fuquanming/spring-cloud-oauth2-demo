/*
 * @(#)PasswordEncoderImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-server
 * 创建日期 : 2018年5月30日
 * 修改历史 : 
 *     1. [2018年5月30日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2.service;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fcc.security.oauth2.common.PasswordEncoderEnum;

/**
 * 自定义密码编码
 * @version 
 * @author 傅泉明
 */
@Component
public class PasswordEncoderImpl implements PasswordEncoder {

    /**
     * @see org.springframework.security.crypto.password.PasswordEncoder#encode(java.lang.CharSequence)
     **/
    @Override
    public String encode(CharSequence rawPassword) {
        return null;
    }

    /**
     * @see org.springframework.security.crypto.password.PasswordEncoder#matches(java.lang.CharSequence, java.lang.String)
     **/
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String password = rawPassword.toString();
      if (password == null || encodedPassword == null) return false;
      String[] encodedPasswords = encodedPassword.split(":");// 标示加密方式
      int length = encodedPasswords.length;
      String encodedType = encodedPasswords[0];
      if (PasswordEncoderEnum.equals.name().equals(encodedType)) {
          if (length == 2) {
              String passwordDb = encodedPasswords[1];
              if (password.equals(passwordDb)) {
                  return true;
              }
          }
      } else if (PasswordEncoderEnum.md5Hash.name().equals(encodedType)) {
          if (length == 4) {
              String username = encodedPasswords[1];
              String passwordDb = encodedPasswords[2];
              String salt = encodedPasswords[3];
              if (passwordDb.equals(new Md5Hash(password, username + salt, 2).toBase64())) {
                  return true;
              }
          }
      }
      return false;
    }

}
