/*
 * @(#)OAuth2HeaderFilter.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-resource
 * 创建日期 : 2018年6月1日
 * 修改历史 : 
 *     1. [2018年6月1日]创建文件 by 傅泉明
 */
package com.wangji.security.oauth2.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

import java.net.URLEncoder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
/**
 * 自定义拦截器Oauth2认证成功后添加自定义头文件信息
 * @version 
 * @author 傅泉明
 */
public class OAuth2HeaderFilter extends ZuulFilter {

    /**
     * @see com.netflix.zuul.IZuulFilter#run()
     **/
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 在header参数中添加 username  
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            // 设置oauth2的username，URLEncoder编码（如果有中文的话）
            ctx.addZuulRequestHeader("oauth2-username", URLEncoder.encode(username, "UTF-8"));
            // api 获取方式如下：
//            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("oauth2-username");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定需要执行该Filter的规则
     * 返回true则执行run()
     * 返回false则不执行run()
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 指定该Filter执行的顺序（Filter从小到大执行）
     * DEBUG_FILTER_ORDER = 1;
     * FORM_BODY_WRAPPER_FILTER_ORDER = -1;
     * PRE_DECORATION_FILTER_ORDER = 5;
     * RIBBON_ROUTING_FILTER_ORDER = 10;
     * SEND_ERROR_FILTER_ORDER = 0;
     * SEND_FORWARD_FILTER_ORDER = 500;
     * SEND_RESPONSE_FILTER_ORDER = 1000;
     * SIMPLE_HOST_ROUTING_FILTER_ORDER = 100;
     * SERVLET_30_WRAPPER_FILTER_ORDER = -2;
     * SERVLET_DETECTION_FILTER_ORDER = -3;
     */
    @Override
    public int filterOrder() {
        return 100;
    }

    /**
     * 指定该Filter的类型
     * ERROR_TYPE = "error";
     * POST_TYPE = "post";
     * PRE_TYPE = "pre";
     * ROUTE_TYPE = "route";
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

}
