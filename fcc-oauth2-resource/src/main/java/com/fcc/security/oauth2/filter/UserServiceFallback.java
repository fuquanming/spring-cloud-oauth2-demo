/*
 * @(#)UserServiceFallback.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-resource
 * 创建日期 : 2018年7月12日
 * 修改历史 : 
 *     1. [2018年7月12日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 * 为api-user服务，写一个断路器，需要配合eureka服务，用serviceId方式路由
 * 熔断器不支持URL配置的路由
 * @version 
 * @author 傅泉明
 */
@Component
public class UserServiceFallback implements FallbackProvider {

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {

            }

            /** 输出服务不能使用的错误信息 */
            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("{\"error\":\"service user lost\"}".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

    /** 指定要处理的 service */
    @Override
    public String getRoute() {
        return "user";
    }

}
