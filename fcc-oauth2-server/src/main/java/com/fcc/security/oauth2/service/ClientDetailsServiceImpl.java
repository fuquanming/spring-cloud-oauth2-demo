/*
 * @(#)ClientDetailsServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-oauth2-server
 * 创建日期 : 2018年5月30日
 * 修改历史 : 
 *     1. [2018年5月30日]创建文件 by 傅泉明
 */
package com.fcc.security.oauth2.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.util.StringUtils;

import com.fcc.security.oauth2.common.PasswordEncoderEnum;

/**
 * 自定义查看clientId接口，判断clientId是否存在
 * @version 
 * @author 傅泉明
 */
public class ClientDetailsServiceImpl implements ClientDetailsService {

    private Logger log = LoggerFactory.getLogger(ClientDetailsServiceImpl.class);
    
    public String selectClientDetailsSql = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details where client_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails details = null;
        try {
            details = (ClientDetails) jdbcTemplate.queryForObject(selectClientDetailsSql, new RowMapper<ClientDetails>() {
                @Override
                public ClientDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                    BaseClientDetails details = new BaseClientDetails(rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5),
                            rs.getString(7), rs.getString(6));
                    details.setClientSecret(PasswordEncoderEnum.equals.name() + ":" + rs.getString(2));// 文本比较密码
                    if (rs.getObject(8) != null) {
                        details.setAccessTokenValiditySeconds(Integer.valueOf(rs.getInt(8)));
                    }
                    if (rs.getObject(9) != null) {
                        details.setRefreshTokenValiditySeconds(Integer.valueOf(rs.getInt(9)));
                    }
                    String scopes = rs.getString(11);
                    if (scopes != null) {
                        details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes));
                    }
                    return details;
                }
            }, new Object[] { clientId });
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("loadClientByClientId error", e);
            throw new ClientRegistrationException("Server ClientDetails error");
        }
        return details;
    }

}
