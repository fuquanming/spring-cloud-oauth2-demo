/*
 * @(#)ProductController.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-service-product
 * 创建日期 : 2018年5月31日
 * 修改历史 : 
 *     1. [2018年5月31日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@RestController
public class ProductController {
    
    @ApiOperation(value="获取商品信息", notes="根据url的id来指定查询对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "String")
    })
    @RequestMapping(value = {"/api/product/{id}"}, method = RequestMethod.GET)
    public Product getProduct(@PathVariable String id,
            @ApiParam(required = false, value = "Token") @RequestParam(name = "access_token", defaultValue = "") String access_token,
            @ApiParam(required = false, value = "名称") @RequestParam(name = "name", defaultValue = "") String name,
            HttpServletRequest request) {
        Set<String> keySet =request.getParameterMap().keySet();
        StringBuilder sb = new StringBuilder();
        for (String key : keySet) {
            sb.append(key).append(":").append(request.getParameter(key)).append("<br/>");
        }
        
//        Enumeration<String> headers = request.getHeaderNames();
//        while (headers.hasMoreElements()) {
//            String key = headers.nextElement();
//            sb.append(key).append(":").append(request.getHeader(key)).append("<br/>");
//        }
        
        // 获取自定义header
        String headerKey = "oauth2-username";
        String username = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(headerKey);
        try {
            if (username != null) sb.append(headerKey).append(":").append(URLDecoder.decode(username, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        String path = request.getContextPath();
        String sPort = (request.getServerPort() == 80)?"":(":" + request.getServerPort());
        String base = request.getScheme()+"://"+request.getServerName()+sPort;
        //String base = request.getScheme()+"://"+request.getServerName();
        String basePath = base + path + "/";
//        return basePath + "-product:" + id + "<br/>" +sb.toString();
        
        Product product = new Product();
        
        product.setProductId(id);
        product.setProductName(name);
        product.setInfo(basePath + "-product:" + sb.toString());
        return product;
    }

    class Product {
        private String productId;
        private String productName;
        private String info;
        public String getInfo() {
            return info;
        }
        public void setInfo(String info) {
            this.info = info;
        }
        public String getProductId() {
            return productId;
        }
        public void setProductId(String productId) {
            this.productId = productId;
        }
        public String getProductName() {
            return productName;
        }
        public void setProductName(String productName) {
            this.productName = productName;
        }
        
    }
}
