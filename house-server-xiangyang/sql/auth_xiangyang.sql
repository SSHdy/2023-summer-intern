/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.2.93
 Source Server Type    : MySQL
 Source Server Version : 50742
 Source Host           : 192.168.2.93:3306
 Source Schema         : auth_xiangyang

 Target Server Type    : MySQL
 Target Server Version : 50742
 File Encoding         : 65001

 Date: 09/05/2023 10:48:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_title` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `menu_url` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单对应的页面url',
  `menu_pid` int(11) NULL DEFAULT NULL COMMENT '父级编码',
  `menu_flag` tinyint(4) NULL DEFAULT NULL COMMENT '是否有效',
  `menu_key` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_imgurl` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `menu_img_virtual_url` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_describe` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单描述',
  `menu_cereatetime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `is_freeze` int(4) NULL DEFAULT NULL COMMENT '是否冻结（1冻结，0解冻）',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_order` int(4) NULL DEFAULT NULL COMMENT '排序',
  `display` tinyint(3) NULL DEFAULT 0 COMMENT '是否显示(0显示 1不显示)',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单模块表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (8, '办公用房管理', '', 0, 1, 'houseManagement', '/home/ryzw/housemanage/upload/menu-images/cc550883-4c94-41dd-a4c2-fa5fe4943ad1.png', '/upload/menu-images/cc550883-4c94-41dd-a4c2-fa5fe4943ad1.png', '党政机关办公用房管理系统针对党政机关办公用房的业务特点，结合现阶段办公用房的实际情况，遵照国家的标准和省级的管理办法，设计出了党政机关办公用房管理信息系统。', '2018-10-31 09:46:01', 0, '', 1, 0);
INSERT INTO `menu` VALUES (9, '办公用房总览', '/home/houseManagement/houseView', 8, 1, 'houseView', '', '', '', '2018-10-31 09:46:01', 0, '', 1, 0);
INSERT INTO `menu` VALUES (10, '示意图', '/home/houseManagement/houseGraph', 8, 1, 'houseGraph', '', '', '', '2018-10-31 09:46:01', 0, '', 5, 0);
INSERT INTO `menu` VALUES (16, '使用管理', '', 0, 1, 'useManagement', '/home/ryzw/housemanage/upload/menu-images/a9662469-387b-47d6-a8e1-5a992670f456.png', '/upload/menu-images/a9662469-387b-47d6-a8e1-5a992670f456.png', '这是一个统计报表菜单', '2018-10-31 09:46:01', 0, '', 4, 0);
INSERT INTO `menu` VALUES (17, '房屋类型统计', '/home/useManagement/houseType', 16, 1, 'houseType', '', '', '', '2018-10-31 09:46:01', 0, '', 1, 0);
INSERT INTO `menu` VALUES (18, '闲置用房统计', '/home/useManagement/spareRoom', 16, 1, 'spareRoom', '', '', '', '2018-10-31 09:46:01', 0, '', 2, 0);
INSERT INTO `menu` VALUES (19, '出租房间统计', '/home/useManagement/rentRoomReport', 16, 1, 'rentRoomReport', '', '', '', '2018-10-31 09:46:01', 0, '', 3, 0);
INSERT INTO `menu` VALUES (22, '超标房间统计', '/home/useManagement/houseProof', 16, 1, 'houseProof', '', '', '', '2018-10-31 09:46:01', 0, '', 4, 0);
INSERT INTO `menu` VALUES (25, '基础信息管理', '', 0, 1, 'baseInfoManagement', '/home/ryzw/housemanage/upload/menu-images/6412862f-7463-4d31-9491-bde63f02783d.png', '/upload/menu-images/6412862f-7463-4d31-9491-bde63f02783d.png', '这是一个基础信息管理菜单', '2018-10-31 09:46:01', 0, '', 10, 0);
INSERT INTO `menu` VALUES (26, '院落信息管理', '/home/baseInfoManagement/baseInfoYard', 25, 1, 'baseInfoYard', NULL, NULL, NULL, '2018-11-29 09:44:05', 0, NULL, 1, 0);
INSERT INTO `menu` VALUES (27, '楼座信息管理', '/home/baseInfoManagement/baseInfoBuild', 25, 1, 'baseInfoBuild', NULL, NULL, NULL, '2018-11-29 09:44:10', 0, NULL, 2, 0);
INSERT INTO `menu` VALUES (29, '单位信息管理', '/home/baseInfoManagement/baseInfoUnit', 25, 1, 'baseInfoUnit', NULL, NULL, NULL, '2018-11-29 09:48:45', 0, NULL, 4, 0);
INSERT INTO `menu` VALUES (30, '职级管理', '/home/baseInfoManagement/baseInfoProfession', 25, 1, 'baseInfoProfession', '', '', '', '2018-12-07 10:02:07', 0, NULL, 5, 0);
INSERT INTO `menu` VALUES (31, '系统管理', '', 0, 1, 'systemManagement', '/home/ryzw/housemanage/upload/menu-images/14d8be5b-92dd-4442-a31e-37e4ecfef43c.png', '/upload/menu-images/14d8be5b-92dd-4442-a31e-37e4ecfef43c.png', '这是一个系统管理菜单', '2018-10-31 09:46:01', 0, '', 12, 0);
INSERT INTO `menu` VALUES (32, '用户管理', '/home/systemManagement/user', 31, 1, 'user', '', '', '', '2018-10-31 09:46:01', 0, '', 1, 0);
INSERT INTO `menu` VALUES (33, '角色管理', '/home/systemManagement/role', 31, 1, 'role', NULL, NULL, NULL, '2018-10-31 09:46:01', 0, NULL, 2, 0);
INSERT INTO `menu` VALUES (34, '菜单管理', '/home/systemManagement/menus', 31, 1, 'menus', '/home/systemManagement/menus', '/home/systemManagement/menus', NULL, '2018-11-01 17:25:16', 0, NULL, 3, 0);
INSERT INTO `menu` VALUES (35, '入库资产', '/home/assetManagement/assetStorage', 1, 1, 'assetStorage', '', '', '', NULL, 0, '', 6, 0);
INSERT INTO `menu` VALUES (55, '出租管理', '/home/disposeManagement/rentManagement', 74, NULL, 'rentManagement', '', '', '', NULL, 0, '', 4, 0);
INSERT INTO `menu` VALUES (57, '房产证', '/home/houseManagement/houseCertificate', 8, NULL, 'houseCertificate', '', '', '', NULL, 0, '', 2, 0);
INSERT INTO `menu` VALUES (58, '土地证', '/home/houseManagement/landCertificate', 8, NULL, 'landCertificate', '', '', '', NULL, 0, '', 3, 0);
INSERT INTO `menu` VALUES (59, '不动产', '/home/houseManagement/immovables', 8, NULL, 'immovables', '', '', '', NULL, 0, '', 4, 0);
INSERT INTO `menu` VALUES (74, '处置管理', '', 0, NULL, 'disposeManagement', '/home/ryzw/housemanage/upload/menu-images/a2e9f924-b7d0-4b7a-b8af-a6c06b9ca784.png', '/upload/menu-images/a2e9f924-b7d0-4b7a-b8af-a6c06b9ca784.png', '处置管理', NULL, 0, '', 6, 0);
INSERT INTO `menu` VALUES (78, '系统日志', '/home/systemManagement/log', 31, NULL, 'log', '', '', '', NULL, 0, NULL, 4, 0);
INSERT INTO `menu` VALUES (79, '物业管理', '/home/houseManagement/estate', 8, NULL, 'estate', '', '', '', NULL, 0, NULL, 6, 0);
INSERT INTO `menu` VALUES (80, '处置申请', '/home/disposeManagement/disposeApply', 74, NULL, 'disposeApply', '', '', '', NULL, 0, '', 1, 0);
INSERT INTO `menu` VALUES (81, '处置待办事项', '/home/disposeManagement/disposeBacklog', 74, NULL, 'disposeBacklog', '', '', '', NULL, 0, '', 2, 0);
INSERT INTO `menu` VALUES (82, '处置查询', '/home/disposeManagement/disposeQuery', 74, NULL, 'disposeQuery', '', '', '', NULL, 0, '', 3, 0);
INSERT INTO `menu` VALUES (83, '配置管理', '', 0, NULL, 'configManagement', '/home/ryzw/housemanage/upload/menu-images/82b02bb6-012e-47bc-b17b-c7730b27de22.png', '/upload/menu-images/82b02bb6-012e-47bc-b17b-c7730b27de22.png', '党政机关办公用房管理系统针对党政机关办公用房的业务特点，结合现阶段办公用房的实际情况，遵照国家的标准和省级的管理办法，设计出了党政机关办公用房管理信息系统。', NULL, 0, '', 2, 0);
INSERT INTO `menu` VALUES (84, '配置申请', '/home/configManagement/configApply', 83, NULL, 'configApply', '', '', '', NULL, 0, NULL, 1, 0);
INSERT INTO `menu` VALUES (85, '配置待办事项', '/home/configManagement/configBacklog', 83, NULL, 'configBacklog', '', '', '', NULL, 0, NULL, 2, 0);
INSERT INTO `menu` VALUES (86, '配置查询', '/home/configManagement/configQuery', 83, NULL, 'configQuery', '', '', '', NULL, 0, NULL, 3, 0);
INSERT INTO `menu` VALUES (87, '客户端管理', '/home/systemManagement/client', 31, NULL, 'client', '', '', '', NULL, 0, NULL, 6, 0);
INSERT INTO `menu` VALUES (88, '工作统计', '', 0, NULL, 'workStatistics', '/home/ryzw/housemanage/upload/menu-images/e86eb908-a0af-4716-a40e-2fc13863b038.png', '/upload/menu-images/e86eb908-a0af-4716-a40e-2fc13863b038.png', '党政机关办公用房管理系统针对党政机关办公用房的业务特点，结合现阶段办公用房的实际情况，遵照国家的标准和省级的管理办法，设计出了党政机关办公用房管理信息系统。', NULL, 0, '', 9, 0);
INSERT INTO `menu` VALUES (89, '出借管理', '/home/disposeManagement/lentManagement', 74, NULL, 'lentManagement', '', '', '', NULL, 0, '', 5, 0);
INSERT INTO `menu` VALUES (90, '租金缴纳提醒', '/home/disposeManagement/rentRemind', 74, NULL, 'rentRemind', '', '', '', NULL, 0, NULL, 6, 0);
INSERT INTO `menu` VALUES (93, '处置列表', '/home/disposeManagement/disposeList', 74, NULL, 'disposeList', '', '', '', NULL, 0, NULL, 8, 0);
INSERT INTO `menu` VALUES (94, '超标单位统计', '/home/useManagement/unitOverproof', 16, NULL, 'unitOverproof', '', '', '', NULL, 0, '', 5, 0);
INSERT INTO `menu` VALUES (95, '房间明细', '/home/houseManagement/houseDetail', 8, NULL, 'houseDetail', '', '', '', NULL, 0, NULL, 7, 0);
INSERT INTO `menu` VALUES (96, '处置工作量年统计', '/home/workStatistics/handle-year', 88, NULL, 'handle-year', '', '', '', NULL, 0, '', 1, 0);
INSERT INTO `menu` VALUES (97, '处置工作量月统计', '/home/workStatistics/handle-month', 88, NULL, 'handle-month', '', '', '', NULL, 0, '', 2, 0);
INSERT INTO `menu` VALUES (98, '配置工作量年统计', '/home/workStatistics/config-year', 88, NULL, 'config-year', '', '', '', NULL, 0, '', 3, 0);
INSERT INTO `menu` VALUES (99, '配置工作量月统计', '/home/workStatistics/config-month', 88, NULL, 'config-month', '', '', '', NULL, 0, NULL, 4, 0);
INSERT INTO `menu` VALUES (102, '档案管理', '', 0, NULL, 'recordManagement', '/home/ryzw/housemanage/upload/menu-images/6a8ae0fe-c0fe-4934-8532-54f8c4728679.png', '/upload/menu-images/6a8ae0fe-c0fe-4934-8532-54f8c4728679.png', '档案管理菜单', NULL, 0, '', 7, 0);
INSERT INTO `menu` VALUES (103, '信息档案', '/home/recordManagement/infoRecord', 102, NULL, 'infoRecord', '', '', '', NULL, 0, NULL, 1, 0);
INSERT INTO `menu` VALUES (105, '字典表管理', '/home/systemManagement/dict', 31, NULL, 'dict', '', '', '', NULL, 0, NULL, 5, 0);
INSERT INTO `menu` VALUES (106, '租借到期提醒', '/home/disposeManagement/expireRemind', 74, NULL, 'expireRemind', '', '', '', NULL, 0, NULL, 7, 0);
INSERT INTO `menu` VALUES (107, '数据上报', '', 0, NULL, 'dataReport', '/home/ryzw/housemanage/upload/menu-images/4c6f707c-9007-42b0-8bb3-5ffee841443e.png', '/upload/menu-images/4c6f707c-9007-42b0-8bb3-5ffee841443e.png', '数据上报菜单', NULL, 0, '', 11, 0);
INSERT INTO `menu` VALUES (108, '数据汇总', '/home/dataReport/dataCollect', 107, NULL, 'dataCollect', '', '', '', NULL, 0, NULL, 1, 0);
INSERT INTO `menu` VALUES (109, '数据收集', '/home/dataReport/dataReport', 107, NULL, 'dataReport', '', '', '', NULL, 0, NULL, 2, 0);
INSERT INTO `menu` VALUES (112, '采购管理', '/home/assetManagement/assetList', 1, NULL, 'assetList', '', '', '', NULL, 0, '', 1, 0);
INSERT INTO `menu` VALUES (114, '重大设备', '/home/houseManagement/assetDevice', 8, NULL, 'assetDevice', '', '', '', NULL, 0, '', 8, 0);
INSERT INTO `menu` VALUES (119, '维修管理', '', 0, NULL, 'repairManagement', NULL, NULL, NULL, NULL, 0, NULL, 10, 0);
INSERT INTO `menu` VALUES (120, '项目申请', '/home/repairManagement/repairApply', 119, NULL, 'repairApply', NULL, NULL, NULL, NULL, 0, NULL, 1, 0);
INSERT INTO `menu` VALUES (127, '维修提醒', '/home/repairManagement/repairRemind', 119, NULL, 'repairRemind', NULL, NULL, NULL, NULL, 0, NULL, 2, 0);
INSERT INTO `menu` VALUES (128, '待办事项', '/home/repairManagement/waittingThing', 119, NULL, 'waittingThing', NULL, NULL, NULL, NULL, 0, NULL, 3, 0);
INSERT INTO `menu` VALUES (132, '项目查询', '/home/repairManagement/repairView', 119, NULL, 'repairView', NULL, NULL, NULL, NULL, 0, NULL, 8, 0);
INSERT INTO `menu` VALUES (133, '房间固定资产', '/home/houseManagement/fixedAssets', 8, NULL, 'fixedAssets', NULL, NULL, NULL, NULL, 0, NULL, 9, 0);
INSERT INTO `menu` VALUES (134, '资产列表', '/home/houseManagement/assetType', 8, NULL, 'assetType', NULL, NULL, NULL, NULL, 0, NULL, 10, 0);

-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`  (
  `token_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication_id` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authentication` blob NULL,
  `refresh_token` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth_access_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_approvals
-- ----------------------------
DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals`  (
  `userId` int(11) NOT NULL,
  `clientId` int(11) NULL DEFAULT NULL,
  `scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `expiresAt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lastModifiedAt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth_approvals
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `oauth_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL,
  `refresh_token_validity` int(11) NULL DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `company_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  PRIMARY KEY (`oauth_detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES (1, 'app', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid', 'authorization_code,password,refresh_token,client_credentials', 'http://192.168.2.131:8091/houseapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (2, 'app2', '', '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:8091/houseapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (3, 'app3', '', '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:8091/authorityapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (4, 'app4', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://192.168.2.130:8091/authorityapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (5, 'app5', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:9091/energyapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (6, 'app6', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:8093/authorityapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (7, 'InternalControl', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (8, 'webApp', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://192.168.2.130:8091/houseapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (9, 'webApps', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://192.168.2.130:9091/energyapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (10, 'webApps1', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:9091/energyapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (11, 'webApps2', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:9091/authorityapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (12, 'webApps3', NULL, '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://192.168.2.130:9091/authorityapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (15, 'app7', '', '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:7191/houseapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (16, 'app8', '', '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid', 'authorization_code,password,refresh_token,client_credentials', 'http://192.168.2.130:7191/houseapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (17, 'app9', '', '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://192.168.2.130:7191/authorityapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES (18, 'app10', '', '$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq', 'openid,Grants openid access', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:7191/authorityapi/webjars/springfox-swagger-ui/oauth2-redirect.html', NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`  (
  `token_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication_id` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth_client_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`  (
  `code` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authentication` blob NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth_code
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`  (
  `token_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication` blob NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth_refresh_token
-- ----------------------------
INSERT INTO `oauth_refresh_token` VALUES ('b96e057b4e1c4920428e833db48d4c15', 0xACED00057372004C6F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E636F6D6D6F6E2E44656661756C744578706972696E674F417574683252656672657368546F6B656E2FDF47639DD0C9B70200014C000A65787069726174696F6E7400104C6A6176612F7574696C2F446174653B787200446F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E636F6D6D6F6E2E44656661756C744F417574683252656672657368546F6B656E73E10E0A6354D45E0200014C000576616C75657400124C6A6176612F6C616E672F537472696E673B787074002462303132643438372D613930382D343361662D613865662D3533633533393963386264367372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001613B74C98E78, 0xACED0005737200416F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E70726F76696465722E4F417574683241757468656E7469636174696F6EBD400B02166252130200024C000D73746F7265645265717565737474003C4C6F72672F737072696E676672616D65776F726B2F73656375726974792F6F61757468322F70726F76696465722F4F4175746832526571756573743B4C00127573657241757468656E7469636174696F6E7400324C6F72672F737072696E676672616D65776F726B2F73656375726974792F636F72652F41757468656E7469636174696F6E3B787200476F72672E737072696E676672616D65776F726B2E73656375726974792E61757468656E7469636174696F6E2E416273747261637441757468656E7469636174696F6E546F6B656ED3AA287E6E47640E0200035A000D61757468656E746963617465644C000B617574686F7269746965737400164C6A6176612F7574696C2F436F6C6C656374696F6E3B4C000764657461696C737400124C6A6176612F6C616E672F4F626A6563743B787000737200266A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654C697374FC0F2531B5EC8E100200014C00046C6973747400104C6A6176612F7574696C2F4C6973743B7872002C6A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C65436F6C6C656374696F6E19420080CB5EF71E0200014C00016371007E00047870737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A65787000000009770400000009737200426F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E617574686F726974792E53696D706C654772616E746564417574686F7269747900000000000001A40200014C0004726F6C657400124C6A6176612F6C616E672F537472696E673B787074000A524F4C455F61646D696E7371007E000D740006617069646F637371007E000D74000C64617461626173652F6C6F677371007E000D74000673797374656D7371007E000D740008757365722F6164647371007E000D74000B757365722F64656C6574657371007E000D740009757365722F656469747371007E000D740009757365722F766965777371007E000D740008757365724C6973747871007E000C707372003A6F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E70726F76696465722E4F41757468325265717565737400000000000000010200075A0008617070726F7665644C000B617574686F72697469657371007E00044C000A657874656E73696F6E7374000F4C6A6176612F7574696C2F4D61703B4C000B726564697265637455726971007E000E4C00077265667265736874003B4C6F72672F737072696E676672616D65776F726B2F73656375726974792F6F61757468322F70726F76696465722F546F6B656E526571756573743B4C000B7265736F7572636549647374000F4C6A6176612F7574696C2F5365743B4C000D726573706F6E7365547970657371007E0024787200386F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E70726F76696465722E426173655265717565737436287A3EA37169BD0200034C0008636C69656E74496471007E000E4C001172657175657374506172616D657465727371007E00224C000573636F706571007E00247870740006776562417070737200256A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170F1A5A8FE74F507420200014C00016D71007E00227870737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F400000000000037708000000040000000274000A6772616E745F7479706574000870617373776F7264740008757365726E616D6574000561646D696E78737200256A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C65536574801D92D18F9B80550200007871007E0009737200176A6176612E7574696C2E4C696E6B656448617368536574D86CD75A95DD2A1E020000787200116A6176612E7574696C2E48617368536574BA44859596B8B7340300007870770C000000103F4000000000000174000361707078017371007E0033770C000000103F40000000000000787371007E002A3F40000000000000770800000010000000007870707371007E0033770C000000103F40000000000000787371007E0033770C000000103F40000000000000787372004F6F72672E737072696E676672616D65776F726B2E73656375726974792E61757468656E7469636174696F6E2E557365726E616D6550617373776F726441757468656E7469636174696F6E546F6B656E00000000000001A40200024C000B63726564656E7469616C7371007E00054C00097072696E636970616C71007E00057871007E0003017371007E00077371007E000B0000000977040000000971007E000F71007E001171007E001371007E001571007E001771007E001971007E001B71007E001D71007E001F7871007E003D737200176A6176612E7574696C2E4C696E6B6564486173684D617034C04E5C106CC0FB0200015A000B6163636573734F726465727871007E002A3F400000000000067708000000080000000271007E002C71007E002D71007E002E71007E002F780070737200326F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E7573657264657461696C732E5573657200000000000001A40200075A00116163636F756E744E6F6E457870697265645A00106163636F756E744E6F6E4C6F636B65645A001563726564656E7469616C734E6F6E457870697265645A0007656E61626C65644C000B617574686F72697469657371007E00244C000870617373776F726471007E000E4C0008757365726E616D6571007E000E7870010101017371007E0030737200116A6176612E7574696C2E54726565536574DD98509395ED875B0300007870737200466F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E7573657264657461696C732E5573657224417574686F72697479436F6D70617261746F7200000000000001A4020000787077040000000971007E000F71007E001171007E001371007E001571007E001771007E001971007E001B71007E001D71007E001F787074000561646D696E);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '角色创建时间',
  `is_freeze` int(4) NULL DEFAULT 0 COMMENT '是否冻结',
  `is_subscibe` int(4) NULL DEFAULT NULL COMMENT '是否订阅',
  `role_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色唯一标识',
  `role_banner` tinyint(4) NULL DEFAULT NULL COMMENT '对内对外（0对内 1对外）',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '机关事务管理局', '2018-12-28 14:22:42', 0, 1, 'supper', 0);
INSERT INTO `role` VALUES (12, '管理员角色', '2021-12-24 17:22:18', 0, 1, 'f10e7ca0-7e72-4eef-b889-787501139005', NULL);
INSERT INTO `role` VALUES (14, '办公用房', '2022-03-18 13:54:24', 0, 1, '85a9b18c-ad78-4e8c-9233-cf77a3846fba', 0);
INSERT INTO `role` VALUES (15, '市政府办公用房角色', '2022-03-29 14:16:41', 0, 1, 'e47e53e5-835b-40d3-8e48-b037613eef59', NULL);

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `role_menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单角色编号',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色编号',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单编号',
  PRIMARY KEY (`role_menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8227 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (5860, 10, 8);
INSERT INTO `role_menu` VALUES (5861, 10, 9);
INSERT INTO `role_menu` VALUES (5862, 10, 57);
INSERT INTO `role_menu` VALUES (5863, 10, 58);
INSERT INTO `role_menu` VALUES (5864, 10, 59);
INSERT INTO `role_menu` VALUES (5865, 10, 10);
INSERT INTO `role_menu` VALUES (5866, 10, 79);
INSERT INTO `role_menu` VALUES (5867, 10, 95);
INSERT INTO `role_menu` VALUES (5868, 10, 114);
INSERT INTO `role_menu` VALUES (5869, 10, 83);
INSERT INTO `role_menu` VALUES (5870, 10, 84);
INSERT INTO `role_menu` VALUES (5871, 10, 85);
INSERT INTO `role_menu` VALUES (5872, 10, 86);
INSERT INTO `role_menu` VALUES (5873, 10, 16);
INSERT INTO `role_menu` VALUES (5874, 10, 17);
INSERT INTO `role_menu` VALUES (5875, 10, 18);
INSERT INTO `role_menu` VALUES (5876, 10, 19);
INSERT INTO `role_menu` VALUES (5877, 10, 22);
INSERT INTO `role_menu` VALUES (5878, 10, 94);
INSERT INTO `role_menu` VALUES (5879, 10, 74);
INSERT INTO `role_menu` VALUES (5880, 10, 80);
INSERT INTO `role_menu` VALUES (5881, 10, 81);
INSERT INTO `role_menu` VALUES (5882, 10, 82);
INSERT INTO `role_menu` VALUES (5883, 10, 55);
INSERT INTO `role_menu` VALUES (5884, 10, 89);
INSERT INTO `role_menu` VALUES (5885, 10, 90);
INSERT INTO `role_menu` VALUES (5886, 10, 106);
INSERT INTO `role_menu` VALUES (5887, 10, 93);
INSERT INTO `role_menu` VALUES (5888, 10, 102);
INSERT INTO `role_menu` VALUES (5889, 10, 103);
INSERT INTO `role_menu` VALUES (5890, 10, 88);
INSERT INTO `role_menu` VALUES (5891, 10, 96);
INSERT INTO `role_menu` VALUES (5892, 10, 97);
INSERT INTO `role_menu` VALUES (5893, 10, 98);
INSERT INTO `role_menu` VALUES (5894, 10, 99);
INSERT INTO `role_menu` VALUES (5895, 10, 25);
INSERT INTO `role_menu` VALUES (5896, 10, 26);
INSERT INTO `role_menu` VALUES (5897, 10, 27);
INSERT INTO `role_menu` VALUES (5898, 10, 29);
INSERT INTO `role_menu` VALUES (5899, 10, 30);
INSERT INTO `role_menu` VALUES (5900, 10, 31);
INSERT INTO `role_menu` VALUES (5901, 10, 32);
INSERT INTO `role_menu` VALUES (5902, 10, 33);
INSERT INTO `role_menu` VALUES (5903, 10, 34);
INSERT INTO `role_menu` VALUES (5904, 10, 78);
INSERT INTO `role_menu` VALUES (5905, 10, 105);
INSERT INTO `role_menu` VALUES (5906, 10, 87);
INSERT INTO `role_menu` VALUES (5907, 8, 8);
INSERT INTO `role_menu` VALUES (5908, 8, 9);
INSERT INTO `role_menu` VALUES (5909, 8, 57);
INSERT INTO `role_menu` VALUES (5910, 8, 58);
INSERT INTO `role_menu` VALUES (5911, 8, 59);
INSERT INTO `role_menu` VALUES (5912, 8, 10);
INSERT INTO `role_menu` VALUES (5913, 8, 79);
INSERT INTO `role_menu` VALUES (5914, 8, 95);
INSERT INTO `role_menu` VALUES (5915, 8, 114);
INSERT INTO `role_menu` VALUES (5916, 8, 83);
INSERT INTO `role_menu` VALUES (5917, 8, 84);
INSERT INTO `role_menu` VALUES (5918, 8, 85);
INSERT INTO `role_menu` VALUES (5919, 8, 86);
INSERT INTO `role_menu` VALUES (5920, 9, 8);
INSERT INTO `role_menu` VALUES (5921, 9, 9);
INSERT INTO `role_menu` VALUES (5922, 9, 57);
INSERT INTO `role_menu` VALUES (5923, 9, 58);
INSERT INTO `role_menu` VALUES (5924, 9, 59);
INSERT INTO `role_menu` VALUES (5925, 9, 10);
INSERT INTO `role_menu` VALUES (5926, 9, 79);
INSERT INTO `role_menu` VALUES (5927, 9, 95);
INSERT INTO `role_menu` VALUES (5928, 9, 114);
INSERT INTO `role_menu` VALUES (5929, 9, 83);
INSERT INTO `role_menu` VALUES (5930, 9, 84);
INSERT INTO `role_menu` VALUES (5931, 9, 85);
INSERT INTO `role_menu` VALUES (5932, 9, 86);
INSERT INTO `role_menu` VALUES (5933, 9, 16);
INSERT INTO `role_menu` VALUES (5934, 9, 17);
INSERT INTO `role_menu` VALUES (5935, 9, 18);
INSERT INTO `role_menu` VALUES (5936, 9, 19);
INSERT INTO `role_menu` VALUES (5937, 9, 22);
INSERT INTO `role_menu` VALUES (5938, 9, 94);
INSERT INTO `role_menu` VALUES (5939, 9, 74);
INSERT INTO `role_menu` VALUES (5940, 9, 80);
INSERT INTO `role_menu` VALUES (5941, 9, 81);
INSERT INTO `role_menu` VALUES (5942, 9, 82);
INSERT INTO `role_menu` VALUES (5943, 9, 55);
INSERT INTO `role_menu` VALUES (5944, 9, 89);
INSERT INTO `role_menu` VALUES (5945, 9, 90);
INSERT INTO `role_menu` VALUES (5946, 9, 106);
INSERT INTO `role_menu` VALUES (5947, 9, 93);
INSERT INTO `role_menu` VALUES (5948, 9, 102);
INSERT INTO `role_menu` VALUES (5949, 9, 103);
INSERT INTO `role_menu` VALUES (5950, 9, 88);
INSERT INTO `role_menu` VALUES (5951, 9, 96);
INSERT INTO `role_menu` VALUES (5952, 9, 97);
INSERT INTO `role_menu` VALUES (5953, 9, 98);
INSERT INTO `role_menu` VALUES (5954, 9, 99);
INSERT INTO `role_menu` VALUES (5955, 9, 25);
INSERT INTO `role_menu` VALUES (5956, 9, 26);
INSERT INTO `role_menu` VALUES (5957, 9, 27);
INSERT INTO `role_menu` VALUES (5958, 9, 29);
INSERT INTO `role_menu` VALUES (5959, 9, 30);
INSERT INTO `role_menu` VALUES (5960, 9, 107);
INSERT INTO `role_menu` VALUES (5961, 9, 108);
INSERT INTO `role_menu` VALUES (5962, 9, 109);
INSERT INTO `role_menu` VALUES (5963, 9, 31);
INSERT INTO `role_menu` VALUES (5964, 9, 32);
INSERT INTO `role_menu` VALUES (5965, 9, 33);
INSERT INTO `role_menu` VALUES (5966, 9, 34);
INSERT INTO `role_menu` VALUES (5967, 9, 78);
INSERT INTO `role_menu` VALUES (5968, 9, 105);
INSERT INTO `role_menu` VALUES (5969, 9, 87);
INSERT INTO `role_menu` VALUES (6020, 2, 8);
INSERT INTO `role_menu` VALUES (6021, 2, 9);
INSERT INTO `role_menu` VALUES (6022, 2, 57);
INSERT INTO `role_menu` VALUES (6023, 2, 58);
INSERT INTO `role_menu` VALUES (6024, 2, 59);
INSERT INTO `role_menu` VALUES (6025, 2, 10);
INSERT INTO `role_menu` VALUES (6026, 2, 79);
INSERT INTO `role_menu` VALUES (6027, 2, 95);
INSERT INTO `role_menu` VALUES (6028, 2, 114);
INSERT INTO `role_menu` VALUES (6029, 2, 83);
INSERT INTO `role_menu` VALUES (6030, 2, 84);
INSERT INTO `role_menu` VALUES (6031, 2, 85);
INSERT INTO `role_menu` VALUES (6032, 2, 86);
INSERT INTO `role_menu` VALUES (6033, 2, 16);
INSERT INTO `role_menu` VALUES (6034, 2, 17);
INSERT INTO `role_menu` VALUES (6035, 2, 18);
INSERT INTO `role_menu` VALUES (6036, 2, 19);
INSERT INTO `role_menu` VALUES (6037, 2, 22);
INSERT INTO `role_menu` VALUES (6038, 2, 94);
INSERT INTO `role_menu` VALUES (6039, 2, 74);
INSERT INTO `role_menu` VALUES (6040, 2, 80);
INSERT INTO `role_menu` VALUES (6041, 2, 81);
INSERT INTO `role_menu` VALUES (6042, 2, 82);
INSERT INTO `role_menu` VALUES (6043, 2, 55);
INSERT INTO `role_menu` VALUES (6044, 2, 89);
INSERT INTO `role_menu` VALUES (6045, 2, 90);
INSERT INTO `role_menu` VALUES (6046, 2, 106);
INSERT INTO `role_menu` VALUES (6047, 2, 93);
INSERT INTO `role_menu` VALUES (6048, 2, 102);
INSERT INTO `role_menu` VALUES (6049, 2, 103);
INSERT INTO `role_menu` VALUES (6050, 2, 88);
INSERT INTO `role_menu` VALUES (6051, 2, 96);
INSERT INTO `role_menu` VALUES (6052, 2, 97);
INSERT INTO `role_menu` VALUES (6053, 2, 98);
INSERT INTO `role_menu` VALUES (6054, 2, 99);
INSERT INTO `role_menu` VALUES (6055, 2, 25);
INSERT INTO `role_menu` VALUES (6056, 2, 26);
INSERT INTO `role_menu` VALUES (6057, 2, 27);
INSERT INTO `role_menu` VALUES (6058, 2, 29);
INSERT INTO `role_menu` VALUES (6059, 2, 30);
INSERT INTO `role_menu` VALUES (6060, 2, 107);
INSERT INTO `role_menu` VALUES (6061, 2, 108);
INSERT INTO `role_menu` VALUES (6062, 2, 109);
INSERT INTO `role_menu` VALUES (6063, 2, 31);
INSERT INTO `role_menu` VALUES (6064, 2, 32);
INSERT INTO `role_menu` VALUES (6065, 2, 33);
INSERT INTO `role_menu` VALUES (6066, 2, 34);
INSERT INTO `role_menu` VALUES (6067, 2, 78);
INSERT INTO `role_menu` VALUES (6068, 2, 105);
INSERT INTO `role_menu` VALUES (6069, 2, 87);
INSERT INTO `role_menu` VALUES (8008, 12, 8);
INSERT INTO `role_menu` VALUES (8009, 12, 9);
INSERT INTO `role_menu` VALUES (8010, 12, 57);
INSERT INTO `role_menu` VALUES (8011, 12, 58);
INSERT INTO `role_menu` VALUES (8012, 12, 59);
INSERT INTO `role_menu` VALUES (8013, 12, 10);
INSERT INTO `role_menu` VALUES (8014, 12, 79);
INSERT INTO `role_menu` VALUES (8015, 12, 95);
INSERT INTO `role_menu` VALUES (8016, 12, 114);
INSERT INTO `role_menu` VALUES (8017, 12, 133);
INSERT INTO `role_menu` VALUES (8018, 12, 134);
INSERT INTO `role_menu` VALUES (8019, 12, 83);
INSERT INTO `role_menu` VALUES (8020, 12, 84);
INSERT INTO `role_menu` VALUES (8021, 12, 85);
INSERT INTO `role_menu` VALUES (8022, 12, 86);
INSERT INTO `role_menu` VALUES (8023, 12, 16);
INSERT INTO `role_menu` VALUES (8024, 12, 17);
INSERT INTO `role_menu` VALUES (8025, 12, 18);
INSERT INTO `role_menu` VALUES (8026, 12, 19);
INSERT INTO `role_menu` VALUES (8027, 12, 22);
INSERT INTO `role_menu` VALUES (8028, 12, 94);
INSERT INTO `role_menu` VALUES (8029, 12, 74);
INSERT INTO `role_menu` VALUES (8030, 12, 80);
INSERT INTO `role_menu` VALUES (8031, 12, 81);
INSERT INTO `role_menu` VALUES (8032, 12, 82);
INSERT INTO `role_menu` VALUES (8033, 12, 55);
INSERT INTO `role_menu` VALUES (8034, 12, 89);
INSERT INTO `role_menu` VALUES (8035, 12, 90);
INSERT INTO `role_menu` VALUES (8036, 12, 106);
INSERT INTO `role_menu` VALUES (8037, 12, 93);
INSERT INTO `role_menu` VALUES (8038, 12, 102);
INSERT INTO `role_menu` VALUES (8039, 12, 103);
INSERT INTO `role_menu` VALUES (8040, 12, 88);
INSERT INTO `role_menu` VALUES (8041, 12, 96);
INSERT INTO `role_menu` VALUES (8042, 12, 97);
INSERT INTO `role_menu` VALUES (8043, 12, 98);
INSERT INTO `role_menu` VALUES (8044, 12, 99);
INSERT INTO `role_menu` VALUES (8045, 12, 25);
INSERT INTO `role_menu` VALUES (8046, 12, 26);
INSERT INTO `role_menu` VALUES (8047, 12, 27);
INSERT INTO `role_menu` VALUES (8048, 12, 29);
INSERT INTO `role_menu` VALUES (8049, 12, 30);
INSERT INTO `role_menu` VALUES (8050, 12, 119);
INSERT INTO `role_menu` VALUES (8051, 12, 120);
INSERT INTO `role_menu` VALUES (8052, 12, 127);
INSERT INTO `role_menu` VALUES (8053, 12, 128);
INSERT INTO `role_menu` VALUES (8054, 12, 132);
INSERT INTO `role_menu` VALUES (8055, 12, 107);
INSERT INTO `role_menu` VALUES (8056, 12, 108);
INSERT INTO `role_menu` VALUES (8057, 12, 109);
INSERT INTO `role_menu` VALUES (8058, 12, 31);
INSERT INTO `role_menu` VALUES (8059, 12, 32);
INSERT INTO `role_menu` VALUES (8060, 12, 33);
INSERT INTO `role_menu` VALUES (8061, 12, 34);
INSERT INTO `role_menu` VALUES (8062, 12, 78);
INSERT INTO `role_menu` VALUES (8063, 12, 105);
INSERT INTO `role_menu` VALUES (8064, 12, 87);
INSERT INTO `role_menu` VALUES (8065, 14, 8);
INSERT INTO `role_menu` VALUES (8066, 14, 9);
INSERT INTO `role_menu` VALUES (8067, 14, 57);
INSERT INTO `role_menu` VALUES (8068, 14, 58);
INSERT INTO `role_menu` VALUES (8069, 14, 59);
INSERT INTO `role_menu` VALUES (8070, 14, 10);
INSERT INTO `role_menu` VALUES (8071, 14, 79);
INSERT INTO `role_menu` VALUES (8072, 14, 95);
INSERT INTO `role_menu` VALUES (8073, 14, 114);
INSERT INTO `role_menu` VALUES (8074, 14, 133);
INSERT INTO `role_menu` VALUES (8075, 14, 134);
INSERT INTO `role_menu` VALUES (8076, 14, 83);
INSERT INTO `role_menu` VALUES (8077, 14, 84);
INSERT INTO `role_menu` VALUES (8078, 14, 85);
INSERT INTO `role_menu` VALUES (8079, 14, 86);
INSERT INTO `role_menu` VALUES (8080, 14, 16);
INSERT INTO `role_menu` VALUES (8081, 14, 17);
INSERT INTO `role_menu` VALUES (8082, 14, 18);
INSERT INTO `role_menu` VALUES (8083, 14, 19);
INSERT INTO `role_menu` VALUES (8084, 14, 22);
INSERT INTO `role_menu` VALUES (8085, 14, 94);
INSERT INTO `role_menu` VALUES (8086, 14, 74);
INSERT INTO `role_menu` VALUES (8087, 14, 80);
INSERT INTO `role_menu` VALUES (8088, 14, 81);
INSERT INTO `role_menu` VALUES (8089, 14, 82);
INSERT INTO `role_menu` VALUES (8090, 14, 55);
INSERT INTO `role_menu` VALUES (8091, 14, 89);
INSERT INTO `role_menu` VALUES (8092, 14, 90);
INSERT INTO `role_menu` VALUES (8093, 14, 106);
INSERT INTO `role_menu` VALUES (8094, 14, 93);
INSERT INTO `role_menu` VALUES (8095, 14, 102);
INSERT INTO `role_menu` VALUES (8096, 14, 103);
INSERT INTO `role_menu` VALUES (8097, 14, 88);
INSERT INTO `role_menu` VALUES (8098, 14, 96);
INSERT INTO `role_menu` VALUES (8099, 14, 97);
INSERT INTO `role_menu` VALUES (8100, 14, 98);
INSERT INTO `role_menu` VALUES (8101, 14, 99);
INSERT INTO `role_menu` VALUES (8102, 14, 25);
INSERT INTO `role_menu` VALUES (8103, 14, 26);
INSERT INTO `role_menu` VALUES (8104, 14, 27);
INSERT INTO `role_menu` VALUES (8105, 14, 29);
INSERT INTO `role_menu` VALUES (8106, 14, 30);
INSERT INTO `role_menu` VALUES (8107, 14, 119);
INSERT INTO `role_menu` VALUES (8108, 14, 120);
INSERT INTO `role_menu` VALUES (8109, 14, 127);
INSERT INTO `role_menu` VALUES (8110, 14, 128);
INSERT INTO `role_menu` VALUES (8111, 14, 132);
INSERT INTO `role_menu` VALUES (8112, 14, 107);
INSERT INTO `role_menu` VALUES (8113, 14, 108);
INSERT INTO `role_menu` VALUES (8114, 14, 109);
INSERT INTO `role_menu` VALUES (8115, 14, 31);
INSERT INTO `role_menu` VALUES (8116, 14, 32);
INSERT INTO `role_menu` VALUES (8117, 14, 33);
INSERT INTO `role_menu` VALUES (8118, 14, 34);
INSERT INTO `role_menu` VALUES (8119, 14, 78);
INSERT INTO `role_menu` VALUES (8120, 14, 105);
INSERT INTO `role_menu` VALUES (8121, 14, 87);
INSERT INTO `role_menu` VALUES (8122, 15, 31);
INSERT INTO `role_menu` VALUES (8123, 15, 8);
INSERT INTO `role_menu` VALUES (8124, 15, 9);
INSERT INTO `role_menu` VALUES (8125, 15, 57);
INSERT INTO `role_menu` VALUES (8126, 15, 58);
INSERT INTO `role_menu` VALUES (8127, 15, 59);
INSERT INTO `role_menu` VALUES (8128, 15, 10);
INSERT INTO `role_menu` VALUES (8129, 15, 79);
INSERT INTO `role_menu` VALUES (8130, 15, 95);
INSERT INTO `role_menu` VALUES (8131, 15, 114);
INSERT INTO `role_menu` VALUES (8132, 15, 133);
INSERT INTO `role_menu` VALUES (8133, 15, 134);
INSERT INTO `role_menu` VALUES (8134, 15, 83);
INSERT INTO `role_menu` VALUES (8135, 15, 84);
INSERT INTO `role_menu` VALUES (8136, 15, 85);
INSERT INTO `role_menu` VALUES (8137, 15, 86);
INSERT INTO `role_menu` VALUES (8138, 15, 16);
INSERT INTO `role_menu` VALUES (8139, 15, 17);
INSERT INTO `role_menu` VALUES (8140, 15, 18);
INSERT INTO `role_menu` VALUES (8141, 15, 19);
INSERT INTO `role_menu` VALUES (8142, 15, 22);
INSERT INTO `role_menu` VALUES (8143, 15, 94);
INSERT INTO `role_menu` VALUES (8144, 15, 74);
INSERT INTO `role_menu` VALUES (8145, 15, 80);
INSERT INTO `role_menu` VALUES (8146, 15, 81);
INSERT INTO `role_menu` VALUES (8147, 15, 82);
INSERT INTO `role_menu` VALUES (8148, 15, 55);
INSERT INTO `role_menu` VALUES (8149, 15, 89);
INSERT INTO `role_menu` VALUES (8150, 15, 90);
INSERT INTO `role_menu` VALUES (8151, 15, 106);
INSERT INTO `role_menu` VALUES (8152, 15, 93);
INSERT INTO `role_menu` VALUES (8153, 15, 102);
INSERT INTO `role_menu` VALUES (8154, 15, 103);
INSERT INTO `role_menu` VALUES (8155, 15, 88);
INSERT INTO `role_menu` VALUES (8156, 15, 96);
INSERT INTO `role_menu` VALUES (8157, 15, 97);
INSERT INTO `role_menu` VALUES (8158, 15, 98);
INSERT INTO `role_menu` VALUES (8159, 15, 99);
INSERT INTO `role_menu` VALUES (8160, 15, 25);
INSERT INTO `role_menu` VALUES (8161, 15, 26);
INSERT INTO `role_menu` VALUES (8162, 15, 27);
INSERT INTO `role_menu` VALUES (8163, 15, 29);
INSERT INTO `role_menu` VALUES (8164, 15, 30);
INSERT INTO `role_menu` VALUES (8165, 15, 119);
INSERT INTO `role_menu` VALUES (8166, 15, 120);
INSERT INTO `role_menu` VALUES (8167, 15, 127);
INSERT INTO `role_menu` VALUES (8168, 15, 128);
INSERT INTO `role_menu` VALUES (8169, 15, 132);
INSERT INTO `role_menu` VALUES (8170, 15, 107);
INSERT INTO `role_menu` VALUES (8171, 15, 108);
INSERT INTO `role_menu` VALUES (8172, 15, 109);
INSERT INTO `role_menu` VALUES (8173, 15, 32);
INSERT INTO `role_menu` VALUES (8174, 15, 33);
INSERT INTO `role_menu` VALUES (8175, 1, 25);
INSERT INTO `role_menu` VALUES (8176, 1, 31);
INSERT INTO `role_menu` VALUES (8177, 1, 8);
INSERT INTO `role_menu` VALUES (8178, 1, 9);
INSERT INTO `role_menu` VALUES (8179, 1, 57);
INSERT INTO `role_menu` VALUES (8180, 1, 58);
INSERT INTO `role_menu` VALUES (8181, 1, 59);
INSERT INTO `role_menu` VALUES (8182, 1, 10);
INSERT INTO `role_menu` VALUES (8183, 1, 79);
INSERT INTO `role_menu` VALUES (8184, 1, 95);
INSERT INTO `role_menu` VALUES (8185, 1, 114);
INSERT INTO `role_menu` VALUES (8186, 1, 133);
INSERT INTO `role_menu` VALUES (8187, 1, 134);
INSERT INTO `role_menu` VALUES (8188, 1, 83);
INSERT INTO `role_menu` VALUES (8189, 1, 84);
INSERT INTO `role_menu` VALUES (8190, 1, 85);
INSERT INTO `role_menu` VALUES (8191, 1, 86);
INSERT INTO `role_menu` VALUES (8192, 1, 16);
INSERT INTO `role_menu` VALUES (8193, 1, 17);
INSERT INTO `role_menu` VALUES (8194, 1, 18);
INSERT INTO `role_menu` VALUES (8195, 1, 19);
INSERT INTO `role_menu` VALUES (8196, 1, 22);
INSERT INTO `role_menu` VALUES (8197, 1, 94);
INSERT INTO `role_menu` VALUES (8198, 1, 74);
INSERT INTO `role_menu` VALUES (8199, 1, 80);
INSERT INTO `role_menu` VALUES (8200, 1, 81);
INSERT INTO `role_menu` VALUES (8201, 1, 82);
INSERT INTO `role_menu` VALUES (8202, 1, 55);
INSERT INTO `role_menu` VALUES (8203, 1, 89);
INSERT INTO `role_menu` VALUES (8204, 1, 90);
INSERT INTO `role_menu` VALUES (8205, 1, 106);
INSERT INTO `role_menu` VALUES (8206, 1, 93);
INSERT INTO `role_menu` VALUES (8207, 1, 102);
INSERT INTO `role_menu` VALUES (8208, 1, 103);
INSERT INTO `role_menu` VALUES (8209, 1, 88);
INSERT INTO `role_menu` VALUES (8210, 1, 96);
INSERT INTO `role_menu` VALUES (8211, 1, 97);
INSERT INTO `role_menu` VALUES (8212, 1, 98);
INSERT INTO `role_menu` VALUES (8213, 1, 99);
INSERT INTO `role_menu` VALUES (8214, 1, 26);
INSERT INTO `role_menu` VALUES (8215, 1, 27);
INSERT INTO `role_menu` VALUES (8216, 1, 30);
INSERT INTO `role_menu` VALUES (8217, 1, 119);
INSERT INTO `role_menu` VALUES (8218, 1, 120);
INSERT INTO `role_menu` VALUES (8219, 1, 127);
INSERT INTO `role_menu` VALUES (8220, 1, 128);
INSERT INTO `role_menu` VALUES (8221, 1, 132);
INSERT INTO `role_menu` VALUES (8222, 1, 107);
INSERT INTO `role_menu` VALUES (8223, 1, 108);
INSERT INTO `role_menu` VALUES (8224, 1, 109);
INSERT INTO `role_menu` VALUES (8225, 1, 32);
INSERT INTO `role_menu` VALUES (8226, 1, 33);

-- ----------------------------
-- Table structure for role_unit
-- ----------------------------
DROP TABLE IF EXISTS `role_unit`;
CREATE TABLE `role_unit`  (
  `role_unit_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色单位编号',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色编号',
  `unit_id` bigint(20) NULL DEFAULT NULL COMMENT '单位编号',
  PRIMARY KEY (`role_unit_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3304 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色单位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_unit
-- ----------------------------
INSERT INTO `role_unit` VALUES (3136, 12, 100);
INSERT INTO `role_unit` VALUES (3137, 12, 237);
INSERT INTO `role_unit` VALUES (3138, 12, 238);
INSERT INTO `role_unit` VALUES (3139, 12, 239);
INSERT INTO `role_unit` VALUES (3140, 12, 240);
INSERT INTO `role_unit` VALUES (3141, 12, 241);
INSERT INTO `role_unit` VALUES (3142, 12, 242);
INSERT INTO `role_unit` VALUES (3143, 12, 243);
INSERT INTO `role_unit` VALUES (3144, 12, 244);
INSERT INTO `role_unit` VALUES (3145, 12, 245);
INSERT INTO `role_unit` VALUES (3146, 12, 246);
INSERT INTO `role_unit` VALUES (3147, 12, 247);
INSERT INTO `role_unit` VALUES (3148, 12, 248);
INSERT INTO `role_unit` VALUES (3149, 12, 249);
INSERT INTO `role_unit` VALUES (3150, 12, 250);
INSERT INTO `role_unit` VALUES (3151, 12, 251);
INSERT INTO `role_unit` VALUES (3152, 12, 252);
INSERT INTO `role_unit` VALUES (3153, 12, 253);
INSERT INTO `role_unit` VALUES (3154, 12, 254);
INSERT INTO `role_unit` VALUES (3155, 12, 255);
INSERT INTO `role_unit` VALUES (3156, 12, 256);
INSERT INTO `role_unit` VALUES (3157, 12, 257);
INSERT INTO `role_unit` VALUES (3158, 12, 258);
INSERT INTO `role_unit` VALUES (3159, 12, 259);
INSERT INTO `role_unit` VALUES (3160, 12, 260);
INSERT INTO `role_unit` VALUES (3161, 12, 261);
INSERT INTO `role_unit` VALUES (3162, 12, 262);
INSERT INTO `role_unit` VALUES (3163, 12, 263);
INSERT INTO `role_unit` VALUES (3164, 12, 264);
INSERT INTO `role_unit` VALUES (3165, 12, 265);
INSERT INTO `role_unit` VALUES (3166, 12, 266);
INSERT INTO `role_unit` VALUES (3167, 12, 267);
INSERT INTO `role_unit` VALUES (3168, 12, 268);
INSERT INTO `role_unit` VALUES (3169, 12, 269);
INSERT INTO `role_unit` VALUES (3170, 12, 270);
INSERT INTO `role_unit` VALUES (3171, 12, 271);
INSERT INTO `role_unit` VALUES (3172, 12, 272);
INSERT INTO `role_unit` VALUES (3173, 12, 273);
INSERT INTO `role_unit` VALUES (3174, 12, 274);
INSERT INTO `role_unit` VALUES (3175, 12, 275);
INSERT INTO `role_unit` VALUES (3176, 12, 276);
INSERT INTO `role_unit` VALUES (3177, 12, 277);
INSERT INTO `role_unit` VALUES (3178, 12, 278);
INSERT INTO `role_unit` VALUES (3179, 12, 279);
INSERT INTO `role_unit` VALUES (3180, 12, 280);
INSERT INTO `role_unit` VALUES (3181, 12, 281);
INSERT INTO `role_unit` VALUES (3182, 12, 282);
INSERT INTO `role_unit` VALUES (3183, 12, 283);
INSERT INTO `role_unit` VALUES (3184, 12, 284);
INSERT INTO `role_unit` VALUES (3185, 12, 285);
INSERT INTO `role_unit` VALUES (3186, 12, 286);
INSERT INTO `role_unit` VALUES (3187, 12, 287);
INSERT INTO `role_unit` VALUES (3188, 12, 288);
INSERT INTO `role_unit` VALUES (3189, 12, 289);
INSERT INTO `role_unit` VALUES (3190, 12, 290);
INSERT INTO `role_unit` VALUES (3191, 12, 291);
INSERT INTO `role_unit` VALUES (3192, 12, 292);
INSERT INTO `role_unit` VALUES (3193, 12, 293);
INSERT INTO `role_unit` VALUES (3194, 12, 294);
INSERT INTO `role_unit` VALUES (3195, 12, 295);
INSERT INTO `role_unit` VALUES (3196, 12, 296);
INSERT INTO `role_unit` VALUES (3197, 12, 297);
INSERT INTO `role_unit` VALUES (3198, 12, 298);
INSERT INTO `role_unit` VALUES (3199, 12, 299);
INSERT INTO `role_unit` VALUES (3200, 12, 300);
INSERT INTO `role_unit` VALUES (3201, 12, 301);
INSERT INTO `role_unit` VALUES (3202, 12, 302);
INSERT INTO `role_unit` VALUES (3203, 12, 303);
INSERT INTO `role_unit` VALUES (3204, 12, 304);
INSERT INTO `role_unit` VALUES (3205, 12, 305);
INSERT INTO `role_unit` VALUES (3206, 12, 306);
INSERT INTO `role_unit` VALUES (3207, 12, 307);
INSERT INTO `role_unit` VALUES (3208, 12, 308);
INSERT INTO `role_unit` VALUES (3209, 12, 309);
INSERT INTO `role_unit` VALUES (3210, 12, 310);
INSERT INTO `role_unit` VALUES (3211, 12, 311);
INSERT INTO `role_unit` VALUES (3212, 12, 312);
INSERT INTO `role_unit` VALUES (3213, 12, 313);
INSERT INTO `role_unit` VALUES (3214, 12, 314);
INSERT INTO `role_unit` VALUES (3215, 12, 315);
INSERT INTO `role_unit` VALUES (3216, 12, 316);
INSERT INTO `role_unit` VALUES (3217, 12, 317);
INSERT INTO `role_unit` VALUES (3218, 14, 100);
INSERT INTO `role_unit` VALUES (3219, 14, 237);
INSERT INTO `role_unit` VALUES (3220, 14, 238);
INSERT INTO `role_unit` VALUES (3221, 15, 238);
INSERT INTO `role_unit` VALUES (3222, 1, 100);
INSERT INTO `role_unit` VALUES (3223, 1, 237);
INSERT INTO `role_unit` VALUES (3224, 1, 238);
INSERT INTO `role_unit` VALUES (3225, 1, 239);
INSERT INTO `role_unit` VALUES (3226, 1, 240);
INSERT INTO `role_unit` VALUES (3227, 1, 241);
INSERT INTO `role_unit` VALUES (3228, 1, 242);
INSERT INTO `role_unit` VALUES (3229, 1, 243);
INSERT INTO `role_unit` VALUES (3230, 1, 244);
INSERT INTO `role_unit` VALUES (3231, 1, 245);
INSERT INTO `role_unit` VALUES (3232, 1, 246);
INSERT INTO `role_unit` VALUES (3233, 1, 247);
INSERT INTO `role_unit` VALUES (3234, 1, 248);
INSERT INTO `role_unit` VALUES (3235, 1, 249);
INSERT INTO `role_unit` VALUES (3236, 1, 250);
INSERT INTO `role_unit` VALUES (3237, 1, 251);
INSERT INTO `role_unit` VALUES (3238, 1, 252);
INSERT INTO `role_unit` VALUES (3239, 1, 253);
INSERT INTO `role_unit` VALUES (3240, 1, 254);
INSERT INTO `role_unit` VALUES (3241, 1, 255);
INSERT INTO `role_unit` VALUES (3242, 1, 256);
INSERT INTO `role_unit` VALUES (3243, 1, 257);
INSERT INTO `role_unit` VALUES (3244, 1, 258);
INSERT INTO `role_unit` VALUES (3245, 1, 259);
INSERT INTO `role_unit` VALUES (3246, 1, 260);
INSERT INTO `role_unit` VALUES (3247, 1, 261);
INSERT INTO `role_unit` VALUES (3248, 1, 262);
INSERT INTO `role_unit` VALUES (3249, 1, 263);
INSERT INTO `role_unit` VALUES (3250, 1, 264);
INSERT INTO `role_unit` VALUES (3251, 1, 265);
INSERT INTO `role_unit` VALUES (3252, 1, 266);
INSERT INTO `role_unit` VALUES (3253, 1, 267);
INSERT INTO `role_unit` VALUES (3254, 1, 268);
INSERT INTO `role_unit` VALUES (3255, 1, 269);
INSERT INTO `role_unit` VALUES (3256, 1, 270);
INSERT INTO `role_unit` VALUES (3257, 1, 271);
INSERT INTO `role_unit` VALUES (3258, 1, 272);
INSERT INTO `role_unit` VALUES (3259, 1, 273);
INSERT INTO `role_unit` VALUES (3260, 1, 274);
INSERT INTO `role_unit` VALUES (3261, 1, 275);
INSERT INTO `role_unit` VALUES (3262, 1, 276);
INSERT INTO `role_unit` VALUES (3263, 1, 277);
INSERT INTO `role_unit` VALUES (3264, 1, 278);
INSERT INTO `role_unit` VALUES (3265, 1, 279);
INSERT INTO `role_unit` VALUES (3266, 1, 280);
INSERT INTO `role_unit` VALUES (3267, 1, 281);
INSERT INTO `role_unit` VALUES (3268, 1, 282);
INSERT INTO `role_unit` VALUES (3269, 1, 283);
INSERT INTO `role_unit` VALUES (3270, 1, 284);
INSERT INTO `role_unit` VALUES (3271, 1, 285);
INSERT INTO `role_unit` VALUES (3272, 1, 286);
INSERT INTO `role_unit` VALUES (3273, 1, 287);
INSERT INTO `role_unit` VALUES (3274, 1, 288);
INSERT INTO `role_unit` VALUES (3275, 1, 289);
INSERT INTO `role_unit` VALUES (3276, 1, 290);
INSERT INTO `role_unit` VALUES (3277, 1, 291);
INSERT INTO `role_unit` VALUES (3278, 1, 292);
INSERT INTO `role_unit` VALUES (3279, 1, 293);
INSERT INTO `role_unit` VALUES (3280, 1, 294);
INSERT INTO `role_unit` VALUES (3281, 1, 295);
INSERT INTO `role_unit` VALUES (3282, 1, 296);
INSERT INTO `role_unit` VALUES (3283, 1, 297);
INSERT INTO `role_unit` VALUES (3284, 1, 298);
INSERT INTO `role_unit` VALUES (3285, 1, 299);
INSERT INTO `role_unit` VALUES (3286, 1, 300);
INSERT INTO `role_unit` VALUES (3287, 1, 301);
INSERT INTO `role_unit` VALUES (3288, 1, 302);
INSERT INTO `role_unit` VALUES (3289, 1, 303);
INSERT INTO `role_unit` VALUES (3290, 1, 304);
INSERT INTO `role_unit` VALUES (3291, 1, 305);
INSERT INTO `role_unit` VALUES (3292, 1, 306);
INSERT INTO `role_unit` VALUES (3293, 1, 307);
INSERT INTO `role_unit` VALUES (3294, 1, 308);
INSERT INTO `role_unit` VALUES (3295, 1, 309);
INSERT INTO `role_unit` VALUES (3296, 1, 310);
INSERT INTO `role_unit` VALUES (3297, 1, 311);
INSERT INTO `role_unit` VALUES (3298, 1, 312);
INSERT INTO `role_unit` VALUES (3299, 1, 313);
INSERT INTO `role_unit` VALUES (3300, 1, 314);
INSERT INTO `role_unit` VALUES (3301, 1, 315);
INSERT INTO `role_unit` VALUES (3302, 1, 316);
INSERT INTO `role_unit` VALUES (3303, 1, 317);

-- ----------------------------
-- Table structure for user_configure
-- ----------------------------
DROP TABLE IF EXISTS `user_configure`;
CREATE TABLE `user_configure`  (
  `user_configure_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户配置权限编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户编号',
  `configure_step` bigint(20) NULL DEFAULT NULL COMMENT '配置权限编号',
  PRIMARY KEY (`user_configure_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 133 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户配置权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_configure
-- ----------------------------
INSERT INTO `user_configure` VALUES (50, 1, 1);
INSERT INTO `user_configure` VALUES (51, 1, 2);
INSERT INTO `user_configure` VALUES (52, 1, 3);
INSERT INTO `user_configure` VALUES (53, 1, 4);
INSERT INTO `user_configure` VALUES (117, 26, 1);
INSERT INTO `user_configure` VALUES (118, 26, 2);
INSERT INTO `user_configure` VALUES (119, 26, 3);
INSERT INTO `user_configure` VALUES (120, 26, 4);
INSERT INTO `user_configure` VALUES (125, 28, 1);
INSERT INTO `user_configure` VALUES (126, 28, 2);
INSERT INTO `user_configure` VALUES (127, 28, 3);
INSERT INTO `user_configure` VALUES (128, 28, 4);
INSERT INTO `user_configure` VALUES (129, 29, 1);
INSERT INTO `user_configure` VALUES (130, 29, 2);
INSERT INTO `user_configure` VALUES (131, 29, 3);
INSERT INTO `user_configure` VALUES (132, 29, 4);

-- ----------------------------
-- Table structure for user_entity
-- ----------------------------
DROP TABLE IF EXISTS `user_entity`;
CREATE TABLE `user_entity`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `phone_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `sex` int(4) NULL DEFAULT NULL COMMENT '性别',
  `user_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  `is_freeze` int(4) NULL DEFAULT 0 COMMENT '是否冻结',
  `unit_id` bigint(20) NULL DEFAULT NULL COMMENT '单位编号',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_entity
-- ----------------------------
INSERT INTO `user_entity` VALUES (1, '机关事务管理局', '17452020150', 'jgswglj', '$2a$10$PgcR8oayzsr4IkMi9l5Lb.2t7Jj6gpnMvL2GzvZ1df..qvxcNaoMK', 1, '2019-01-07 19:49:46', 0, 100);
INSERT INTO `user_entity` VALUES (26, '超级管理员', '18827083100', 'admin', '$2a$10$1gSiL8YpXG/iTCRANjY2guUUMTr9CbqUbGA8V7nMzaVLnH/V/o0D.', 0, '2021-12-24 17:22:49', 0, 100);
INSERT INTO `user_entity` VALUES (29, '张城', '13871776128', '13871776128', '$2a$10$nBxg1gDUlaFUkBXY0BCH9uw46470OuF.emRBN9gUEZ.1pbSkeTTm2', 0, '2022-03-29 14:15:33', 0, 238);

-- ----------------------------
-- Table structure for user_handle
-- ----------------------------
DROP TABLE IF EXISTS `user_handle`;
CREATE TABLE `user_handle`  (
  `user_handle` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户处置权限编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户编号',
  `handle_step` bigint(20) NULL DEFAULT NULL COMMENT '处置权限编号',
  PRIMARY KEY (`user_handle`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 153 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户处置权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_handle
-- ----------------------------
INSERT INTO `user_handle` VALUES (8, 1, 2);
INSERT INTO `user_handle` VALUES (9, 1, 1);
INSERT INTO `user_handle` VALUES (10, 1, 3);
INSERT INTO `user_handle` VALUES (11, 1, 4);
INSERT INTO `user_handle` VALUES (12, 1, 5);
INSERT INTO `user_handle` VALUES (13, 1, 6);
INSERT INTO `user_handle` VALUES (14, 1, 7);
INSERT INTO `user_handle` VALUES (125, 26, 1);
INSERT INTO `user_handle` VALUES (126, 26, 2);
INSERT INTO `user_handle` VALUES (127, 26, 3);
INSERT INTO `user_handle` VALUES (128, 26, 4);
INSERT INTO `user_handle` VALUES (129, 26, 5);
INSERT INTO `user_handle` VALUES (130, 26, 6);
INSERT INTO `user_handle` VALUES (131, 26, 7);
INSERT INTO `user_handle` VALUES (139, 28, 1);
INSERT INTO `user_handle` VALUES (140, 28, 2);
INSERT INTO `user_handle` VALUES (141, 28, 3);
INSERT INTO `user_handle` VALUES (142, 28, 4);
INSERT INTO `user_handle` VALUES (143, 28, 5);
INSERT INTO `user_handle` VALUES (144, 28, 6);
INSERT INTO `user_handle` VALUES (145, 28, 7);
INSERT INTO `user_handle` VALUES (146, 29, 1);
INSERT INTO `user_handle` VALUES (147, 29, 2);
INSERT INTO `user_handle` VALUES (148, 29, 3);
INSERT INTO `user_handle` VALUES (149, 29, 4);
INSERT INTO `user_handle` VALUES (150, 29, 5);
INSERT INTO `user_handle` VALUES (151, 29, 6);
INSERT INTO `user_handle` VALUES (152, 29, 7);

-- ----------------------------
-- Table structure for user_repair
-- ----------------------------
DROP TABLE IF EXISTS `user_repair`;
CREATE TABLE `user_repair`  (
  `user_repair_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户维修权限编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户编号',
  `repair_id` bigint(20) NULL DEFAULT NULL COMMENT '维修权限编号',
  PRIMARY KEY (`user_repair_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户维修权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_repair
-- ----------------------------
INSERT INTO `user_repair` VALUES (1, 26, 1);
INSERT INTO `user_repair` VALUES (26, 26, 1);
INSERT INTO `user_repair` VALUES (51, 26, 2);
INSERT INTO `user_repair` VALUES (53, 26, 4);
INSERT INTO `user_repair` VALUES (55, 26, 6);
INSERT INTO `user_repair` VALUES (56, 26, 27);
INSERT INTO `user_repair` VALUES (57, 26, 3);
INSERT INTO `user_repair` VALUES (65, 28, 1);
INSERT INTO `user_repair` VALUES (66, 28, 2);
INSERT INTO `user_repair` VALUES (67, 28, 3);
INSERT INTO `user_repair` VALUES (68, 28, 4);
INSERT INTO `user_repair` VALUES (69, 28, 5);
INSERT INTO `user_repair` VALUES (70, 28, 6);
INSERT INTO `user_repair` VALUES (71, 28, 27);
INSERT INTO `user_repair` VALUES (72, 29, 1);
INSERT INTO `user_repair` VALUES (73, 29, 2);
INSERT INTO `user_repair` VALUES (74, 29, 3);
INSERT INTO `user_repair` VALUES (75, 29, 4);
INSERT INTO `user_repair` VALUES (76, 29, 5);
INSERT INTO `user_repair` VALUES (77, 29, 6);
INSERT INTO `user_repair` VALUES (78, 29, 27);
INSERT INTO `user_repair` VALUES (82, 26, 19);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户角色编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户编号',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色编号',
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (11, 1, 1);
INSERT INTO `user_role` VALUES (39, 26, 12);
INSERT INTO `user_role` VALUES (41, 28, 14);
INSERT INTO `user_role` VALUES (43, 29, 15);

SET FOREIGN_KEY_CHECKS = 1;
