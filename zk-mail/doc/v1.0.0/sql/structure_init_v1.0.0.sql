CREATE DATABASE  IF NOT EXISTS `zk-mail` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `zk-mail`;
-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.211.55.11    Database: zk-mail
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
-- Table structure for table `t_mail_send_history`
--

DROP TABLE IF EXISTS `t_mail_send_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_mail_send_history` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL DEFAULT '0' COMMENT '公司ID',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司代码',
  `c_type_id` bigint NOT NULL COMMENT '邮件类型ID',
  `c_type_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '邮件类型代码',
  `c_mail_template_id` bigint NOT NULL COMMENT '发送的邮件模板ID',
  `c_params` json DEFAULT NULL COMMENT '参数 json',
  `c_send_address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '发件邮箱',
  `c_send_name` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '发送邮件名称',
  `c_subject` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '发送邮件主题',
  `c_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '邮件内容',
  `c_locale` enum('_default','zh_CN','en_US','zh_TW') COLLATE utf8mb4_bin NOT NULL DEFAULT '_default' COMMENT '邮件语言',
  `c_send_flag` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '发送标识，有时就记录，没有就为空',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='邮件发送历史';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_mail_template`
--

DROP TABLE IF EXISTS `t_mail_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_mail_template` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL DEFAULT '0' COMMENT '公司ID，不指定公司时，值存入0，为实现唯一索引',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '公司代码',
  `c_type_id` bigint NOT NULL COMMENT '邮件类型ID',
  `c_type_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '邮件类型代码',
  `c_locale` enum('_default','zh_CN','en_US','zh_TW') COLLATE utf8mb4_bin NOT NULL DEFAULT '_default' COMMENT '邮件语言，ENUM(‘_default’, zh_CN'', ''en_US'', ''zh_TW'')',
  `c_status` int NOT NULL COMMENT '状态：0-启用；1-禁用；',
  `c_send_address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '发件邮箱，默认使用 发送邮件服务器发送账号',
  `c_send_name` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '发送邮件名称，可以使用 ${替换参数}',
  `c_subject` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '发送邮件主题，可以使用 ${替换参数}',
  `c_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '邮件内容，可以使用 ${替换参数}',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `type_code_UNIQUE` (`c_type_code`,`c_locale`,`c_company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='发送邮件的模板';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_mail_type`
--

DROP TABLE IF EXISTS `t_mail_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_mail_type` (
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
  `c_type_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类型代码；全表唯一',
  `c_type_name` json NOT NULL COMMENT '类型名称',
  `c_status` int NOT NULL COMMENT '状态：0-启用；1-禁用；',
  `c_type_desc` json DEFAULT NULL COMMENT '类型说明',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `type_code_UNIQUE` (`c_type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='发送邮件的类型';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-20 10:29:45
