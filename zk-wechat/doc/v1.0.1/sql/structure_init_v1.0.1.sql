CREATE DATABASE  IF NOT EXISTS `zk-wechat` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `zk-wechat`;
-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.211.55.11    Database: zk-wechat
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_log_access`
--

DROP TABLE IF EXISTS `t_log_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_log_access` (
  `c_pk_id` bigint NOT NULL COMMENT '主键 ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '集团代码',
  `c_company_id` bigint DEFAULT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司代码',
  `c_user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `c_date_time` datetime DEFAULT NULL COMMENT '修改时间戳 “yyyy-MM-dd HH:mm:ss.ssssss”',
  `c_user_agent` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户访问媒介【操作系统、浏览器类型、终端情况等信息】',
  `c_title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '功能名称',
  `c_request_uri` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求路径',
  `c_remote_addr` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '访问者IP地址',
  `c_method` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求方式',
  `c_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '请求参数',
  `c_exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '异常信息',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_cert_platform`
--

DROP TABLE IF EXISTS `t_wx_cert_platform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_cert_platform` (
  `c_pk_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键 ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_mchid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信商户号的 mchid ',
  `c_wx_cert_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '平台证书存放路径； 不要以 “/” 开头。',
  `c_wx_cert_serial_no` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '平台证书的序列号',
  `c_wx_cert_effective_time` datetime NOT NULL COMMENT '平台证书生效时间',
  `c_wx_cert_expiration_time` datetime NOT NULL COMMENT '平台证书过期时间',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='平台证书维护表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_merchant`
--

DROP TABLE IF EXISTS `t_wx_merchant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_merchant` (
  `c_pk_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键，也是微信商户号的 mchid ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_status` int NOT NULL COMMENT '商户号接入状态；0-启用；1-禁用；enabled, disabled；',
  `c_wx_apiv3_aes_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商户号 APIv3密钥 ',
  `c_wx_api_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'API 密钥',
  `c_wx_api_cert_path_pkcs12` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '格式为 PKCS12 的 商户API证书存放路径；不要以 “/” 开头；',
  `c_wx_api_cert_path_pem` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商户API证书导出的 pem 格式的证书文件 存放路径；不要以 “/” 开头；',
  `c_wx_api_cert_path_key_pem` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商户API证书导出的 pem 格式的私钥文件存放路径；不要以 “/” 开头；',
  `c_wx_api_cert_serial_no` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商户API证书的序列号',
  `c_wx_api_cert_update_date` datetime DEFAULT NULL COMMENT '商户API证书更新时间',
  `c_wx_api_cert_expiration_time` datetime DEFAULT NULL COMMENT '商户API证书过期时间',
  `c_wx_api_cert_effective_time` datetime DEFAULT NULL COMMENT '商户API证书生效时间',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信商户号';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_official_accounts`
--

DROP TABLE IF EXISTS `t_wx_official_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_official_accounts` (
  `c_pk_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_data_space_platform` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据域平台标识；第三方数据绑定标识',
  `c_data_space_group` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据域分组标识；第三方数据绑定标识',
  `c_data_space_owner` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据域拥有标识；第三方数据绑定标识',
  `c_group_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '集团代码',
  `c_company_id` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司ID',
  `c_company_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司代码',
  `c_add_type` int NOT NULL COMMENT '添加方式；0-第三方平台授权；',
  `c_wx_third_party_appid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信第三方平台 Appid',
  `c_wx_account_appid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信第三方平台，目标授权方账号',
  `c_wx_auth_account_type` int NOT NULL DEFAULT '0' COMMENT '微信第三方平台，目标授权方账号类型；0-未知；1-公众号；2-小程序',
  `c_wx_authorizer_refresh_token` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信第三方平台，目标授权方账号的刷新令牌，获取授权信息时得到',
  `c_wx_func_info` json DEFAULT NULL COMMENT '目标授权方账号。授权的权限集',
  `c_wx_nick_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称: nick_name',
  `c_wx_head_img` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像: head_img',
  `c_wx_service_type_info` json DEFAULT NULL COMMENT '授权方在微信平台的类型: service_type_info',
  `c_wx_verify_type_info` json DEFAULT NULL COMMENT '认证类型:  verify_type_info',
  `c_wx_user_name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '原始 ID：user_name	',
  `c_wx_principal_name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '主体名称：principal_name',
  `c_wx_business_info` json DEFAULT NULL COMMENT '用以了解功能的开通状况：business_info',
  `c_wx_qrcode_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '二维码图片的 URL，开发者最好自行也进行保存：qrcode_url',
  `c_wx_alias` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公众号所设置的微信号，可能为空：alias',
  `c_wx_signature` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '小程序帐号介绍，可能为空：signature',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `auth_account_UNIQUE` (`c_wx_account_appid`,`c_wx_third_party_appid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信开放平台中第三方平台的目标授权账号';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_official_accounts_user`
--

DROP TABLE IF EXISTS `t_wx_official_accounts_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_official_accounts_user` (
  `c_pk_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_data_space_platform` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据域平台标识；第三方数据绑定标识',
  `c_data_space_group` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据域分组标识；第三方数据绑定标识',
  `c_data_space_owner` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据域拥有标识；第三方数据绑定标识',
  `c_group_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '集团代码',
  `c_company_id` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司ID',
  `c_company_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司代码',
  `c_wx_third_party_appid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信第三方平台 Appid',
  `c_wx_official_account_appid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '账号，公众号或小程序的 appid',
  `c_wx_subscribe` int NOT NULL DEFAULT '0' COMMENT '用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。\\\\\\\\n0-未关注；1-已关注；',
  `c_wx_openid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户的标识，对当前公众号唯一: openid',
  `c_wx_phone_num` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信绑定的手机号',
  `c_wx_nickname` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户的昵称: nickname',
  `c_wx_sex` int DEFAULT NULL COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知: sex',
  `c_wx_city` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户所在城市: city',
  `c_wx_country` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户所在国家: country',
  `c_wx_province` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户所在省份: province',
  `c_wx_language` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户的语言，简体中文为zh_CN: language',
  `c_wx_headimgurl` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效:  headimgurl',
  `c_wx_privilege` json DEFAULT NULL COMMENT '用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）',
  `c_wx_unionid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段：unionid',
  `c_wx_remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注：remark',
  `c_wx_groupid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户所在的分组ID（兼容旧的用户分组接口）：groupid',
  `c_wx_tagid_list` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户被打上的标签ID列表：tagid_list',
  `c_wx_subscribe_scene` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他：subscribe_scene',
  `c_wx_qr_scene` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '二维码扫码场景（开发者自定义）：qr_scene',
  `c_wx_qr_scene_str` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '二维码扫码场景描述（开发者自定义）：qr_scene_str',
  `c_wx_session_key` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '小程序会话密钥',
  `c_wx_zk_access_token` json DEFAULT NULL COMMENT '网页授权时，存放的 zk 平台的 用户授权 access_token 对象',
  `c_wx_subscribe_time_str` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间：subscribe_time	',
  `c_wx_subscribe_date` datetime DEFAULT NULL COMMENT '关注时间',
  `c_wx_channel` int NOT NULL DEFAULT '0' COMMENT '用户来源渠道；0-未知；1-网面授权；2-关注公众号；',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `user_unique` (`c_wx_openid`,`c_wx_official_account_appid`,`c_wx_third_party_appid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信开放平台中第三方平台目标授权账号的用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_official_accounts_user_gps`
--

DROP TABLE IF EXISTS `t_wx_official_accounts_user_gps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_official_accounts_user_gps` (
  `c_pk_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_third_party_auth_account_user_pk_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '目标授权用户主键',
  `c_data_space_platform` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据域平台标识；第三方数据绑定标识',
  `c_data_space_group` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据域分组标识；第三方数据绑定标识',
  `c_data_space_owner` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据域拥有标识；第三方数据绑定标识',
  `c_wx_third_party_appid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信第三方平台 Appid',
  `c_wx_authorizer_appid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信第三方平台，目标授权方账号',
  `c_wx_openid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户的标识，对当前公众号唯一: openid',
  `c_wx_latitude` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地理位置纬度: Latitude',
  `c_wx_longitude` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地理位置经度: Longitude',
  `c_wx_precision` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地理位置精度: Precision',
  `c_wx_create_time` int NOT NULL DEFAULT '0' COMMENT '上报时间: CreateTime',
  `c_group_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '集团代码',
  `c_company_id` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司ID',
  `c_company_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司代码',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信开放平台中第三方平台目标授权账号的用户上报地理位置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pay_get_amount`
--

DROP TABLE IF EXISTS `t_wx_pay_get_amount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pay_get_amount` (
  `c_pk_id` bigint NOT NULL COMMENT '主键，也是微信商户号的 mchid ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_pay_order_pk_id` bigint NOT NULL COMMENT '对应支付记录',
  `c_wx_total` int NOT NULL COMMENT '总金额 total int 是 订单总金额，单位为分。',
  `c_wx_currency` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '货币类型 currency string[1,16] 否 CNY：人民币，境内商户号仅支持人民币; 示例值：CNY。',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_wx_pay_order_pk_id_UNIQUE` (`c_wx_pay_order_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信支付-收款金额表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pay_get_business_type`
--

DROP TABLE IF EXISTS `t_wx_pay_get_business_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pay_get_business_type` (
  `c_pk_id` bigint NOT NULL COMMENT '主键，也是微信商户号的 mchid ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '业务类型代码；全表唯一，也是请求是的路径参数；',
  `c_name` json NOT NULL COMMENT '业务类型的名称',
  `c_status` int NOT NULL COMMENT '状态；0-启用；1-禁用； disabled，enabled',
  `c_start_date` datetime DEFAULT NULL COMMENT '开启使用日期；null 时不较验；',
  `c_end_date` datetime DEFAULT NULL COMMENT '结束使用日期；null 时不较验；',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_code_UNIQUE` (`c_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信支付-收款时，支持收款的业务的类型维护；';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pay_get_notify`
--

DROP TABLE IF EXISTS `t_wx_pay_get_notify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pay_get_notify` (
  `c_pk_id` bigint NOT NULL COMMENT '主键，也是微信商户号的 mchid ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_pay_order_pk_id` bigint NOT NULL COMMENT '对应支付记录',
  `c_wx_wechatpay_signature` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信响应签名',
  `c_wx_wechatpay_timestam` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'HTTP头Wechatpay-Timestamp 中的应答时间戳。',
  `c_wx_wechatpay_nonce` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'HTTP头Wechatpay-Nonce 中的应答随机串。',
  `c_wx_wechatpay_serial` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '证书序列号',
  `c_wx_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '通知数据 object 是 通知资源数据',
  `c_dispose_status` int DEFAULT NULL COMMENT '处理方式；0-未处理；1-处理成功；2-重复通知不处理；3-校验签名异常；',
  `c_resource` json DEFAULT NULL COMMENT '解密明文',
  `c_signature` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '签名校验失败时，记录异常的签名；',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信支付-收款时，支付结果通知';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pay_get_order`
--

DROP TABLE IF EXISTS `t_wx_pay_get_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pay_get_order` (
  `c_pk_id` bigint NOT NULL COMMENT '主键，支付订单号',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_appid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用ID appid string[1,32] 是 body; 直连商户申请的公众号或移动应用appid。示例值：wxd678efh567hg6787',
  `c_wx_mchid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '直连商户号 mchid string[1,32] 是 body 直连商户的商户号，由微信支付生成并下发。 示例值：1230000109',
  `c_wx_description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品描述 description string[1,127] 是 body 商品描述; 示例值：Image形象店-深圳腾大-QQ公仔',
  `c_wx_time_expire` datetime NOT NULL DEFAULT '1977-01-01 00:00:00' COMMENT '交易结束时间',
  `c_wx_attach` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '附加数据 attach string[1,128] 否 body 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用;示例值：自定义数据',
  `c_wx_notify_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '通知地址 notify_url string[1,256] 是 body 通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。格式：URL，示例值：https://www.weixin.qq.com/wxpay/pay.php',
  `c_wx_goods_tag` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单优惠标记 goods_tag string[1,32] 否 body 订单优惠标记; 示例值：WXG',
  `c_pay_status` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '支付状态；',
  `c_wx_channel` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信支付平台-支付渠道；JSAPI, H5, APP, NATIVE, MINIPROGRAM;',
  `c_pay_group_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付关系组代码；绑定对应的商户号 mchid 和 应用ID appid',
  `c_business_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '支付的收款业务代码；不同业务定义不同接口；0-垃圾分类业务；',
  `c_business_no` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '业务订单号',
  `c_wx_res_status_code` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '统一下单时，微信平台响应状态码',
  `c_wx_prepay_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信生成的预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时；示例值：wx201410272009395522657a690389285100',
  `c_wx_prepay_id_date` datetime DEFAULT NULL COMMENT '预支付交易会话标识的更新时间',
  `c_pay_sign_date` datetime DEFAULT NULL COMMENT '调起支付签名生成时间；',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `UN_business` (`c_business_code`,`c_business_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信支付-收款记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pay_get_order_history`
--

DROP TABLE IF EXISTS `t_wx_pay_get_order_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pay_get_order_history` (
  `c_pk_id` bigint NOT NULL COMMENT '主键',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_pay_get_order_id` bigint NOT NULL COMMENT '支付订单号，可关联查询之前支付订单的其他信息。',
  `c_wx_appid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用ID appid string[1,32] 是 body; 直连商户申请的公众号或移动应用appid。示例值：wxd678efh567hg6787',
  `c_wx_mchid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '直连商户号 mchid string[1,32] 是 body 直连商户的商户号，由微信支付生成并下发。 示例值：1230000109',
  `c_wx_description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品描述 description string[1,127] 是 body 商品描述; 示例值：Image形象店-深圳腾大-QQ公仔',
  `c_wx_time_expire` datetime NOT NULL DEFAULT '1977-01-01 00:00:00' COMMENT '交易结束时间',
  `c_wx_attach` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '附加数据 attach string[1,128] 否 body 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用;示例值：自定义数据',
  `c_wx_notify_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '通知地址 notify_url string[1,256] 是 body 通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。格式：URL，示例值：https://www.weixin.qq.com/wxpay/pay.php',
  `c_wx_goods_tag` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单优惠标记 goods_tag string[1,32] 否 body 订单优惠标记; 示例值：WXG',
  `c_pay_status` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '支付状态；',
  `c_wx_channel` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信支付平台-支付渠道；JSAPI, H5, APP, NATIVE, MINIPROGRAM;',
  `c_pay_group_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付关系组代码；绑定对应的商户号 mchid 和 应用ID appid',
  `c_business_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '支付的收款业务代码；不同业务定义不同接口；0-垃圾分类业务；',
  `c_business_no` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '业务订单号',
  `c_wx_res_status_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '统一下单时，微信平台响应状态码',
  `c_wx_prepay_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信生成的预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时；示例值：wx201410272009395522657a690389285100',
  `c_wx_prepay_id_date` datetime DEFAULT NULL COMMENT '预支付交易会话标识的更新时间',
  `c_pay_sign_date` datetime DEFAULT NULL COMMENT '调起支付签名生成时间；',
  `c_history_date` datetime NOT NULL COMMENT '业务订单创建时间',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信支付-收款记录表-历史表，t_wx_pay_get_order 表做物理删除时，会将数据插入到这个表来记录历史。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pay_get_payer`
--

DROP TABLE IF EXISTS `t_wx_pay_get_payer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pay_get_payer` (
  `c_pk_id` bigint NOT NULL COMMENT '主键，也是微信商户号的 mchid ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_pay_order_pk_id` bigint NOT NULL COMMENT '对应支付记录',
  `c_wx_openid` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '收款时，支付用户标识 openid string[1,128] 是 用户在直连商户appid下的唯一标识。',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_wx_pay_order_pk_id_UNIQUE` (`c_wx_pay_order_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信支付-收款时，支付用户表，在 jsapi 使用支付时，支付记录才有对应的支付用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pay_group`
--

DROP TABLE IF EXISTS `t_wx_pay_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pay_group` (
  `c_pk_id` bigint NOT NULL COMMENT '主键 ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_appid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '渠道应用ID 小程序ID或其他 在微信支付平台的应用ID',
  `c_wx_mchid` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '渠道商户ID 在微信支付平台的商户号 mchid',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用渠道代码；全表唯一，也是请求是的路径参数',
  `c_name` json NOT NULL COMMENT '应用渠道的名称',
  `c_status` int NOT NULL COMMENT '状态；0-启用；1-禁用； disabled，enabled',
  `c_start_date` datetime DEFAULT NULL COMMENT '开启使用日期；null 时不较验',
  `c_end_date` datetime DEFAULT NULL COMMENT '结束使用日期；null 时不较验',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_code_UNIQUE` (`c_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='支付关系组；其对象，绑定对应的商户号 mchid 和 应用ID appid';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pb_func_key_config`
--

DROP TABLE IF EXISTS `t_wx_pb_func_key_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pb_func_key_config` (
  `c_pk_id` bigint NOT NULL COMMENT '主键',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_func_type_id` bigint NOT NULL COMMENT '功能类型id；',
  `c_func_type_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '功能类型代码',
  `c_func_name` json NOT NULL COMMENT '功能名称',
  `c_func_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '功能标识代码 key；全表唯一',
  `c_status` int NOT NULL COMMENT '状态：0-启用；1-禁用；',
  `c_redirect_original_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '重写向，源真实地址；从域名根路径下开始写',
  `c_redirect_proxy_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '重写向，真实地址；从域名根路径下开始写',
  `c_func_desc` json DEFAULT NULL COMMENT '功能说明',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `func_key_UNIQUE` (`c_func_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信平台，功能 key 配置；网页授权成功后，会根据这个 key 重定向到业务功能；';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pb_func_key_type`
--

DROP TABLE IF EXISTS `t_wx_pb_func_key_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pb_func_key_type` (
  `c_pk_id` bigint NOT NULL COMMENT '主键',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_func_type_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '功能类型代码；全表唯一',
  `c_func_type_name` json NOT NULL COMMENT '功能类型名称',
  `c_status` int NOT NULL COMMENT '状态：0-启用；1-禁用；',
  `c_func_type_desc` json DEFAULT NULL COMMENT '功能类型说明',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `type_code_UNIQUE` (`c_func_type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信平台，功能 key 类型';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_third_party`
--

DROP TABLE IF EXISTS `t_wx_third_party`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_third_party` (
  `c_pk_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键，也是微信第三方平台账号的 app id',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_group_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '集团代码',
  `c_company_id` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司ID',
  `c_company_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司代码',
  `c_wx_app_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信第三方平台账号的 app secret',
  `c_wx_token` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信平台配置的消息校验 token',
  `c_wx_aes_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '微信消息加解密 key',
  `c_wx_ticket` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '第三方平台令牌',
  `c_wx_ticket_update_date` datetime DEFAULT NULL COMMENT '第三方平台令牌更新时间',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='微信开放平台的第三方开发者';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-20 10:28:42
