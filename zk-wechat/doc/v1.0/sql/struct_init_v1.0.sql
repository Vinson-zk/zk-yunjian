-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
-- 本版本最终的结构 
-- Host: 192.168.1.118    Database: gf-wechat
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
-- Table structure for table `t_wx_cert_platform`
--

DROP TABLE IF EXISTS `t_wx_cert_platform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_cert_platform` (
  `c_pk_id` varchar(256) NOT NULL COMMENT '主键 ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_mchid` varchar(256) NOT NULL COMMENT '微信商户号的 mchid ',
  `c_wx_cert_path` varchar(256) NOT NULL COMMENT '平台证书存放路径； 不要以 “/” 开头。',
  `c_wx_cert_serial_no` varchar(256) NOT NULL COMMENT '平台证书的序列号',
  `c_wx_cert_effective_time` datetime NOT NULL COMMENT '平台证书生效时间',
  `c_wx_cert_expiration_time` datetime NOT NULL COMMENT '平台证书过期时间',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='平台证书维护表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_merchant`
--

DROP TABLE IF EXISTS `t_wx_merchant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_merchant` (
  `c_pk_id` varchar(256) NOT NULL COMMENT '主键，也是微信商户号的 mchid ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_status` int NOT NULL COMMENT '商户号接入状态；0-启用；1-禁用；enabled, disabled；',
  `c_wx_apiv3_aes_key` varchar(256) NOT NULL COMMENT '商户号 APIv3密钥 ',
  `c_wx_api_key` varchar(256) DEFAULT NULL COMMENT 'API 密钥',
  `c_wx_api_cert_path_pkcs12` varchar(512) DEFAULT NULL COMMENT '格式为 PKCS12 的 商户API证书存放路径；不要以 “/” 开头；',
  `c_wx_api_cert_path_pem` varchar(512) NOT NULL COMMENT '商户API证书导出的 pem 格式的证书文件 存放路径；不要以 “/” 开头；',
  `c_wx_api_cert_path_key_pem` varchar(512) NOT NULL COMMENT '商户API证书导出的 pem 格式的私钥文件存放路径；不要以 “/” 开头；',
  `c_wx_api_cert_serial_no` varchar(256) DEFAULT NULL COMMENT '商户API证书的序列号',
  `c_wx_api_cert_update_date` datetime DEFAULT NULL COMMENT '商户API证书更新时间',
  `c_wx_api_cert_expiration_time` datetime DEFAULT NULL COMMENT '商户API证书过期时间',
  `c_wx_api_cert_effective_time` datetime DEFAULT NULL COMMENT '商户API证书生效时间',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信商户号';
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
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_pay_order_pk_id` bigint NOT NULL COMMENT '对应支付记录',
  `c_wx_total` int NOT NULL COMMENT '总金额 total int 是 订单总金额，单位为分。',
  `c_wx_currency` varchar(64) NOT NULL COMMENT '货币类型 currency string[1,16] 否 CNY：人民币，境内商户号仅支持人民币; 示例值：CNY。',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_wx_pay_order_pk_id_UNIQUE` (`c_wx_pay_order_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信支付-收款金额表';
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
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_code` varchar(256) NOT NULL COMMENT '业务类型代码；全表唯一，也是请求是的路径参数；',
  `c_name` json NOT NULL COMMENT '业务类型的名称',
  `c_status` int NOT NULL COMMENT '状态；0-启用；1-禁用； disabled，enabled',
  `c_start_date` datetime DEFAULT NULL COMMENT '开启使用日期；null 时不较验；',
  `c_end_date` datetime DEFAULT NULL COMMENT '结束使用日期；null 时不较验；',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_code_UNIQUE` (`c_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信支付-收款时，支持收款的业务的类型维护；';
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
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_pay_order_pk_id` bigint NOT NULL COMMENT '对应支付记录',
  `c_wx_wechatpay_signature` text NOT NULL COMMENT '微信响应签名',
  `c_wx_wechatpay_timestam` varchar(128) NOT NULL COMMENT 'HTTP头Wechatpay-Timestamp 中的应答时间戳。',
  `c_wx_wechatpay_nonce` varchar(128) NOT NULL COMMENT 'HTTP头Wechatpay-Nonce 中的应答随机串。',
  `c_wx_wechatpay_serial` varchar(256) DEFAULT NULL COMMENT '证书序列号',
  `c_wx_body` text NOT NULL COMMENT '通知数据 object 是 通知资源数据',
  `c_dispose_status` int DEFAULT NULL COMMENT '处理方式；0-未处理；1-处理成功；2-重复通知不处理；3-校验签名异常；',
  `c_resource` json DEFAULT NULL COMMENT '解密明文',
  `c_signature` text COMMENT '签名校验失败时，记录异常的签名；',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信支付-收款时，支付结果通知';
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
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_appid` varchar(128) NOT NULL COMMENT '应用ID appid string[1,32] 是 body; 直连商户申请的公众号或移动应用appid。示例值：wxd678efh567hg6787',
  `c_wx_mchid` varchar(128) NOT NULL COMMENT '直连商户号 mchid string[1,32] 是 body 直连商户的商户号，由微信支付生成并下发。 示例值：1230000109',
  `c_wx_description` varchar(512) NOT NULL COMMENT '商品描述 description string[1,127] 是 body 商品描述; 示例值：Image形象店-深圳腾大-QQ公仔',
  `c_wx_time_expire` datetime NOT NULL DEFAULT '1977-01-01 00:00:00' COMMENT '交易结束时间',
  `c_wx_attach` varchar(512) DEFAULT NULL COMMENT '附加数据 attach string[1,128] 否 body 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用;示例值：自定义数据',
  `c_wx_notify_url` varchar(512) NOT NULL COMMENT '通知地址 notify_url string[1,256] 是 body 通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。格式：URL，示例值：https://www.weixin.qq.com/wxpay/pay.php',
  `c_wx_goods_tag` varchar(128) DEFAULT NULL COMMENT '订单优惠标记 goods_tag string[1,32] 否 body 订单优惠标记; 示例值：WXG',
  `c_pay_status` varchar(128) NOT NULL COMMENT '支付状态；',
  `c_wx_channel` varchar(128) NOT NULL COMMENT '微信支付平台-支付渠道；JSAPI, H5, APP, NATIVE, MINIPROGRAM;',
  `c_pay_group_code` varchar(128) DEFAULT NULL COMMENT '支付关系组代码；绑定对应的商户号 mchid 和 应用ID appid',
  `c_business_code` varchar(64) NOT NULL COMMENT '支付的收款业务代码；不同业务定义不同接口；0-垃圾分类业务；',
  `c_business_no` varchar(256) NOT NULL COMMENT '业务订单号',
  `c_wx_res_status_code` varchar(512) DEFAULT NULL COMMENT '统一下单时，微信平台响应状态码',
  `c_wx_prepay_id` varchar(256) DEFAULT NULL COMMENT '微信生成的预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时；示例值：wx201410272009395522657a690389285100',
  `c_wx_prepay_id_date` datetime DEFAULT NULL COMMENT '预支付交易会话标识的更新时间',
  `c_pay_sign_date` datetime DEFAULT NULL COMMENT '调起支付签名生成时间；',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `UN_business` (`c_business_code`,`c_business_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信支付-收款记录表';
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
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_pay_get_order_id` bigint NOT NULL COMMENT '支付订单号，可关联查询之前支付订单的其他信息。',
  `c_wx_appid` varchar(128) NOT NULL COMMENT '应用ID appid string[1,32] 是 body; 直连商户申请的公众号或移动应用appid。示例值：wxd678efh567hg6787',
  `c_wx_mchid` varchar(128) NOT NULL COMMENT '直连商户号 mchid string[1,32] 是 body 直连商户的商户号，由微信支付生成并下发。 示例值：1230000109',
  `c_wx_description` varchar(512) NOT NULL COMMENT '商品描述 description string[1,127] 是 body 商品描述; 示例值：Image形象店-深圳腾大-QQ公仔',
  `c_wx_time_expire` datetime NOT NULL DEFAULT '1977-01-01 00:00:00' COMMENT '交易结束时间',
  `c_wx_attach` varchar(512) DEFAULT NULL COMMENT '附加数据 attach string[1,128] 否 body 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用;示例值：自定义数据',
  `c_wx_notify_url` varchar(512) NOT NULL COMMENT '通知地址 notify_url string[1,256] 是 body 通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。格式：URL，示例值：https://www.weixin.qq.com/wxpay/pay.php',
  `c_wx_goods_tag` varchar(128) DEFAULT NULL COMMENT '订单优惠标记 goods_tag string[1,32] 否 body 订单优惠标记; 示例值：WXG',
  `c_pay_status` varchar(128) NOT NULL COMMENT '支付状态；',
  `c_wx_channel` varchar(128) NOT NULL COMMENT '微信支付平台-支付渠道；JSAPI, H5, APP, NATIVE, MINIPROGRAM;',
  `c_pay_group_code` varchar(128) DEFAULT NULL COMMENT '支付关系组代码；绑定对应的商户号 mchid 和 应用ID appid',
  `c_business_code` varchar(64) NOT NULL COMMENT '支付的收款业务代码；不同业务定义不同接口；0-垃圾分类业务；',
  `c_business_no` varchar(256) NOT NULL COMMENT '业务订单号',
  `c_wx_res_status_code` varchar(256) DEFAULT NULL COMMENT '统一下单时，微信平台响应状态码',
  `c_wx_prepay_id` varchar(256) DEFAULT NULL COMMENT '微信生成的预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时；示例值：wx201410272009395522657a690389285100',
  `c_wx_prepay_id_date` datetime DEFAULT NULL COMMENT '预支付交易会话标识的更新时间',
  `c_pay_sign_date` datetime DEFAULT NULL COMMENT '调起支付签名生成时间；',
  `c_history_date` datetime NOT NULL COMMENT '业务订单创建时间',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信支付-收款记录表-历史表，t_wx_pay_get_order 表做物理删除时，会将数据插入到这个表来记录历史。';
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
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_pay_order_pk_id` bigint NOT NULL COMMENT '对应支付记录',
  `c_wx_openid` varchar(512) NOT NULL COMMENT '收款时，支付用户标识 openid string[1,128] 是 用户在直连商户appid下的唯一标识。',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_wx_pay_order_pk_id_UNIQUE` (`c_wx_pay_order_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信支付-收款时，支付用户表，在 jsapi 使用支付时，支付记录才有对应的支付用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_pay_group`
--

DROP TABLE IF EXISTS `t_wx_pay_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_pay_group` (
  `c_pk_id` bigint NOT NULL COMMENT '主键，也是微信商户号的 mchid ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_appid` varchar(256) NOT NULL COMMENT '渠道应用ID 小程序ID或其他 在微信支付平台的应用ID',
  `c_wx_mchid` varchar(256) NOT NULL COMMENT '渠道商户ID 在微信支付平台的商户号 mchid',
  `c_code` varchar(256) NOT NULL COMMENT '应用渠道代码；全表唯一，也是请求是的路径参数',
  `c_name` json NOT NULL COMMENT '应用渠道的名称',
  `c_status` int NOT NULL COMMENT '状态；0-启用；1-禁用； disabled，enabled',
  `c_start_date` datetime DEFAULT NULL COMMENT '开启使用日期；null 时不较验',
  `c_end_date` datetime DEFAULT NULL COMMENT '结束使用日期；null 时不较验',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_code_UNIQUE` (`c_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付关系组；其对象，绑定对应的商户号 mchid 和 应用ID appid';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_wx_third_party`
--

DROP TABLE IF EXISTS `t_wx_third_party`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wx_third_party` (
  `c_pk_id` varchar(256) NOT NULL COMMENT '主键，也是微信第三方平台账号的 app id',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` datetime NOT NULL COMMENT '创建时间',
  `c_update_date` datetime NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(512) DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(512) DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(512) DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_wx_app_secret` varchar(256) NOT NULL COMMENT '微信第三方平台账号的 app secret',
  `c_wx_token` varchar(256) NOT NULL COMMENT '微信平台配置的消息校验 token',
  `c_wx_aes_key` varchar(256) NOT NULL COMMENT '微信消息加解密 key',
  `c_wx_ticket` varchar(256) DEFAULT NULL COMMENT '第三方平台令牌',
  `c_wx_ticket_update_date` datetime DEFAULT NULL COMMENT '第三方平台令牌更新时间',
  `c_wx_access_token` varchar(256) DEFAULT NULL COMMENT '第三方平台权限 token',
  `c_wx_access_token_expires_in` int DEFAULT NULL COMMENT '第三方平台权限 token 有效时长 单位秒',
  `c_wx_access_token_update_date` datetime DEFAULT NULL COMMENT '第三方平台权限 token 更新时间',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信开放平台的第三方开发者';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-08 16:06:29
