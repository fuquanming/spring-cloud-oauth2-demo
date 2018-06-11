/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50713
Source Host           : localhost:3307
Source Database       : oauth2

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2018-06-06 14:38:34
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `oauth_client_details`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(128) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  `username` varchar(256) DEFAULT NULL COMMENT '用户名称',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('acme', 'oauth2-resource', 'acmesecret', 'app', 'authorization_code,client_credentials,refresh_token,password', '', null, null, null, null, 'app', null);
INSERT INTO `oauth_client_details` VALUES ('client', 'oauth2-resource', 'secret', 'app', 'authorization_code,client_credentials,refresh_token,password,implicit', 'http://www.baidu.com', null, null, null, null, null, null);
INSERT INTO `oauth_client_details` VALUES ('client_2', 'oauth2-resource', 'client_2secret', 'select', 'client_credentials,refresh_token,password', 'http://www.baidu.com', null, null, null, null, null, 'fqm');
INSERT INTO `oauth_client_details` VALUES ('resource', 'oauth2-resource', 'secret', 'app', 'authorization_code', '', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(32) NOT NULL COMMENT '账号',
  `USER_PASS` varchar(32) NOT NULL COMMENT '密码',
  `USER_SALT` varchar(32) NOT NULL,
  `USER_STATUS` tinyint(4) NOT NULL COMMENT '状态 (0-失效 1-有效)',
  `CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建者',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '更新者',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=200183016662302722 DEFAULT CHARSET=utf8 COMMENT='账户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('159995201127972870', 'fqm', 'H9a/GHaFalzY1E3QUQc14Q==', '10d8e9b3e181e2bdcea290a9df4f2341', '1', 'fqm', '2018-05-01 11:22:13', null, null);
