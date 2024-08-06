CREATE DATABASE  IF NOT EXISTS `zk-sys` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `zk-sys`;
-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.211.55.11    Database: zk-sys
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
  `c_user_agent` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户访问媒介【操作系统、浏览器类型、终端情况等信息】',
  `c_title` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '功能名称',
  `c_request_uri` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求路径',
  `c_remote_addr` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '访问者IP地址',
  `c_method` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求方式',
  `c_params` text COLLATE utf8mb4_bin COMMENT '请求参数',
  `c_exception` text COLLATE utf8mb4_bin COMMENT '异常信息',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_company`
--

DROP TABLE IF EXISTS `t_sys_auth_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_company` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_auth_id` bigint NOT NULL COMMENT '权限ID ',
  `c_auth_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限代码;公司下唯一',
  `c_owner_type` bigint NOT NULL COMMENT '拥有方式；0-使用权；1-所有权； ',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_id_UNIQUE` (`c_company_id`,`c_auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='公司权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_defined`
--

DROP TABLE IF EXISTS `t_sys_auth_defined`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_defined` (
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
  `c_name` json NOT NULL COMMENT '权限名称',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限代码；全表唯一；',
  `c_system_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用系统代码',
  `c_status` int NOT NULL COMMENT '权限代码状态；0-正常；1-禁用；',
  `c_short_desc` json DEFAULT NULL COMMENT ' 简介',
  `c_source_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源代码；与来源ID标识唯一；',
  `c_source_id` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源ID标识，与来源代码唯一',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_code_UNIQUE` (`c_code`),
  UNIQUE KEY `c_source_UNIQUE` (`c_source_id`,`c_source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限定义表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_dept`
--

DROP TABLE IF EXISTS `t_sys_auth_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_dept` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_dept_id` bigint DEFAULT NULL COMMENT '部门ID ',
  `c_dept_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '部门代码',
  `c_auth_id` bigint NOT NULL COMMENT '权限ID ',
  `c_auth_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限代码',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_id_UNIQUE` (`c_dept_id`,`c_auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='部门权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_func_api`
--

DROP TABLE IF EXISTS `t_sys_auth_func_api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_func_api` (
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
  `c_auth_id` bigint NOT NULL COMMENT '权限 ',
  `c_func_api_id` bigint NOT NULL COMMENT '功能API接口ID ',
  `c_func_api_code` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '功能API接口 代码',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_id_UNIQUE` (`c_auth_id`,`c_func_api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限-功能API接口表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_menu`
--

DROP TABLE IF EXISTS `t_sys_auth_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_menu` (
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
  `c_auth_id` bigint NOT NULL COMMENT '权限 ',
  `c_menu_id` bigint NOT NULL COMMENT '菜单ID ',
  `c_menu_code` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '菜单代码',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_id_UNIQUE` (`c_auth_id`,`c_menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限-菜单资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_nav`
--

DROP TABLE IF EXISTS `t_sys_auth_nav`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_nav` (
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
  `c_auth_id` bigint NOT NULL COMMENT '权限 ',
  `c_nav_id` bigint NOT NULL COMMENT '导航栏目ID ',
  `c_nav_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '导航栏目代码',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_id_UNIQUE` (`c_auth_id`,`c_nav_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限-导航栏目表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_rank`
--

DROP TABLE IF EXISTS `t_sys_auth_rank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_rank` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_rank_id` bigint DEFAULT NULL COMMENT '职级ID ',
  `c_rank_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '职级代码',
  `c_auth_id` bigint NOT NULL COMMENT '权限ID ',
  `c_auth_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限代码',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_id_UNIQUE` (`c_rank_id`,`c_auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='职级权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_role`
--

DROP TABLE IF EXISTS `t_sys_auth_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_role` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_role_id` bigint DEFAULT NULL COMMENT '角色ID ',
  `c_role_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色代码',
  `c_auth_id` bigint NOT NULL COMMENT '权限ID ',
  `c_auth_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限代码',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_id_UNIQUE` (`c_role_id`,`c_auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_user`
--

DROP TABLE IF EXISTS `t_sys_auth_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_user` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_user_id` bigint DEFAULT NULL COMMENT '用户ID ',
  `c_auth_id` bigint NOT NULL COMMENT '权限ID ',
  `c_auth_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限代码',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_id_UNIQUE` (`c_user_id`,`c_auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_user_role`
--

DROP TABLE IF EXISTS `t_sys_auth_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_user_role` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_role_id` bigint NOT NULL COMMENT '角色ID ',
  `c_role_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色代码;公司下唯一',
  `c_user_id` bigint NOT NULL COMMENT '用户ID ',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_source_UNIQUE` (`c_role_id`,`c_user_id`),
  UNIQUE KEY `c_relation_UNIQUE` (`c_user_id`,`c_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_auth_user_type`
--

DROP TABLE IF EXISTS `t_sys_auth_user_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_auth_user_type` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_user_type_id` bigint DEFAULT NULL COMMENT '用户类型ID ',
  `c_user_type_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户类型代码',
  `c_auth_id` bigint NOT NULL COMMENT '权限ID ',
  `c_auth_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限代码',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_auth_id_UNIQUE` (`c_user_type_id`,`c_auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户类型权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_org_company`
--

DROP TABLE IF EXISTS `t_sys_org_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_org_company` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码；全表唯一',
  `c_name` json NOT NULL COMMENT '公司名称',
  `c_logo` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司 logo',
  `c_logo_original` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司 logo 原图',
  `c_fax_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司传真号',
  `c_tel_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司固定电话号',
  `c_phone_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司手机号',
  `c_mail` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司邮箱',
  `c_legal_person` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司法人',
  `c_cert_type` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司法人证件类型; 字典项-字典项ID；',
  `c_cert_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司法人证件号',
  `c_status` int NOT NULL COMMENT '公司状态；0-正常；1-禁用；2-审核中；',
  `c_address` json DEFAULT NULL COMMENT '公司地址',
  `c_found_date` datetime DEFAULT NULL COMMENT '公司成立日期',
  `c_register_date` datetime DEFAULT NULL COMMENT '公司注册日期',
  `c_register_address` json DEFAULT NULL COMMENT '公司注册地址',
  `c_register_authority` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司注册机构',
  `c_register_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司注册号',
  `c_short_desc` json DEFAULT NULL COMMENT ' 公司简介',
  `c_parent_id` bigint DEFAULT NULL COMMENT '上级公司ID ',
  `c_source_code` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源代码；与来源ID标识唯一；',
  `c_source_id` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源ID标识，与来源代码唯一',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_mail_UNIQUE` (`c_mail`),
  UNIQUE KEY `c_code_UNIQUE` (`c_code`),
  UNIQUE KEY `c_source_UNIQUE` (`c_source_id`,`c_source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='公司表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_org_dept`
--

DROP TABLE IF EXISTS `t_sys_org_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_org_dept` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '部门代码；公司下唯一',
  `c_name` json NOT NULL COMMENT '部门名称',
  `c_fax_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '部门传真号',
  `c_tel_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '部门固定电话号',
  `c_phone_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '部门手机号',
  `c_mail` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '部门邮箱',
  `c_status` int NOT NULL COMMENT '部门状态；0-正常；1-撤销；',
  `c_address` json DEFAULT NULL COMMENT '部门地址',
  `c_short_desc` json DEFAULT NULL COMMENT ' 部门简介',
  `c_parent_id` bigint DEFAULT NULL COMMENT '上级部门ID ',
  `c_source_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源代码；与来源ID标识唯一；',
  `c_source_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源ID标识，与来源代码唯一',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_dept_code_UNIQUE` (`c_code`,`c_company_code`),
  UNIQUE KEY `c_dept_id_UNIQUE` (`c_company_id`,`c_code`),
  UNIQUE KEY `c_source_UNIQUE` (`c_source_id`,`c_source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_org_rank`
--

DROP TABLE IF EXISTS `t_sys_org_rank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_org_rank` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '职级代码;公司下唯一',
  `c_name` json NOT NULL COMMENT '职级名称',
  `c_status` int NOT NULL COMMENT '职级状态；0-正常；1-禁用；',
  `c_short_desc` json DEFAULT NULL COMMENT ' 职级简介',
  `c_source_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源代码；与来源ID标识唯一；',
  `c_source_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源ID标识，与来源代码唯一',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_rank_code_UNIQUE` (`c_company_code`,`c_code`),
  UNIQUE KEY `c_rank_id_UNIQUE` (`c_company_id`,`c_code`),
  UNIQUE KEY `c_source_UNIQUE` (`c_source_id`,`c_source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='职级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_org_role`
--

DROP TABLE IF EXISTS `t_sys_org_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_org_role` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色代码;公司下唯一',
  `c_name` json NOT NULL COMMENT '角色名称',
  `c_type` int NOT NULL COMMENT '角色类型；0-公司级角色；1-部门级角色，指定为某个部门的角色；',
  `c_dept_id` bigint DEFAULT NULL COMMENT '部门ID ',
  `c_dept_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '部门代码',
  `c_status` int NOT NULL COMMENT '角色状态；0-正常；1-禁用；',
  `c_short_desc` json DEFAULT NULL COMMENT ' 角色简介',
  `c_source_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源代码；与来源ID标识唯一；',
  `c_source_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源ID标识，与来源代码唯一',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_role_code_UNIQUE` (`c_company_code`,`c_code`),
  UNIQUE KEY `c_role_id_UNIQUE` (`c_company_id`,`c_code`),
  UNIQUE KEY `c_source_UNIQUE` (`c_source_id`,`c_source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_org_user`
--

DROP TABLE IF EXISTS `t_sys_org_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_org_user` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_user_type_id` bigint DEFAULT NULL COMMENT '用户类型ID ',
  `c_user_type_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户类型代码',
  `c_rank_id` bigint DEFAULT NULL COMMENT '用户职级ID ',
  `c_rank_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户职级代码',
  `c_dept_id` bigint DEFAULT NULL COMMENT '用户部门ID ',
  `c_dept_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户部门代码',
  `c_account` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户账号；公司下唯一；',
  `c_password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户密码',
  `c_pwd_status` int NOT NULL COMMENT '密码状态；0-系统密码；1-用户密码；',
  `c_pwd_last_update_date` datetime DEFAULT NULL COMMENT '密码最后修改日期',
  `c_status` int NOT NULL COMMENT '用户状态; 0-正常; 1-禁用; 2-离职; 3-离职中; ',
  `c_family_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '姓',
  `c_second_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名',
  `c_nickname` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '昵称',
  `c_birthday` datetime DEFAULT NULL COMMENT '生日',
  `c_sex` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '性别；unknown-保密；male-男；female-女；third_gender-第三性别; 字典项',
  `c_address` json DEFAULT NULL COMMENT '地址',
  `c_tel_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '固定电话',
  `c_phone_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '手机；公司下唯一；',
  `c_mail` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '邮箱；公司下唯一；',
  `c_head_photo` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `c_head_photo_original` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '将你原图',
  `c_qq` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'QQ',
  `c_wechat` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信',
  `c_job_num` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户工号',
  `c_join_date` datetime DEFAULT NULL COMMENT '入职日期',
  `c_leave_date` datetime DEFAULT NULL COMMENT '离职日期',
  `c_apply_leave_date` datetime DEFAULT NULL COMMENT '申请离职日期',
  `c_source_code` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源代码，与来源ID标识唯一',
  `c_source_id` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源ID标识，与来源代码唯一',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_mail_id_UNIQUE` (`c_mail`,`c_company_id`),
  UNIQUE KEY `c_phone_num_id_UNIQUE` (`c_phone_num`,`c_company_id`),
  UNIQUE KEY `c_phone_num_code_UNIQUE` (`c_phone_num`,`c_company_code`),
  UNIQUE KEY `c_mail_code_UNIQUE` (`c_company_code`,`c_mail`),
  UNIQUE KEY `c_account_UNIQUE` (`c_account`,`c_company_id`),
  UNIQUE KEY `c_source_UNIQUE` (`c_source_id`,`c_source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_org_user_type`
--

DROP TABLE IF EXISTS `t_sys_org_user_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_org_user_type` (
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
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_id` bigint NOT NULL COMMENT '公司ID ',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类型代码;公司下唯一',
  `c_name` json NOT NULL COMMENT '类型名称',
  `c_status` int NOT NULL COMMENT '类型状态；0-正常；1-禁用；',
  `c_short_desc` json DEFAULT NULL COMMENT ' 类型简介',
  `c_source_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源代码；与来源ID标识唯一；',
  `c_source_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源ID标识，与来源代码唯一',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_user_type_code_UNIQUE` (`c_company_code`,`c_code`),
  UNIQUE KEY `c_user_type_id_UNIQUE` (`c_company_id`,`c_code`),
  UNIQUE KEY `c_source_UNIQUE` (`c_source_id`,`c_source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_res_application_system`
--

DROP TABLE IF EXISTS `t_sys_res_application_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_res_application_system` (
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
  `c_name` json NOT NULL COMMENT '应用系统名称',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用系统代码，唯一',
  `c_short_name` json DEFAULT NULL COMMENT '应用系统简称',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_code_UNIQUE` (`c_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='应用系统';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_res_dict`
--

DROP TABLE IF EXISTS `t_sys_res_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_res_dict` (
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
  `c_type_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典类型代码',
  `c_dict_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典代码；字典类型下唯一；',
  `c_dict_name` json NOT NULL COMMENT '字典名称',
  `c_dict_desc` json DEFAULT NULL COMMENT '字典说明',
  `c_parent_id` bigint DEFAULT NULL COMMENT '上级字典ID ',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `unique_code` (`c_type_code`,`c_dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_res_dict_type`
--

DROP TABLE IF EXISTS `t_sys_res_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_res_dict_type` (
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
  `c_type_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典类型代码; 全表唯一',
  `c_type_name` json NOT NULL COMMENT '字典类型名称',
  `c_type_desc` json DEFAULT NULL COMMENT '字典类型说明',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `type_code_UNIQUE` (`c_type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_res_func_api`
--

DROP TABLE IF EXISTS `t_sys_res_func_api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_res_func_api` (
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
  `c_name` json NOT NULL COMMENT 'API 名称',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'API 标识代码，全表唯一；同代码中权限注解代码',
  `c_system_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用系统代码',
  `c_use_context` json DEFAULT NULL COMMENT '应用场景说明',
  `c_req_content_type` varchar(512) COLLATE utf8mb4_bin NOT NULL COMMENT '请求内容类型',
  `c_req_desc` json DEFAULT NULL COMMENT '请求说明',
  `c_req_params_desc` json DEFAULT NULL COMMENT '请求参数说明',
  `c_res_content_type` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '响应内容类型',
  `c_res_data_desc` json DEFAULT NULL COMMENT '响应说明',
  `c_original_uri` varchar(2048) COLLATE utf8mb4_bin NOT NULL COMMENT '功能访问源地址',
  `c_agent_uri` varchar(2048) COLLATE utf8mb4_bin NOT NULL COMMENT '功能访问代理地址',
  `c_req_methods` int NOT NULL COMMENT '支持的请求方法；位与表示，0-支持、1-不支持：1位-GET、2位-POST、3位-DELETE、4位-PUT、5位-CONNECT、6位-HEAD、7位-OPTIONS、8位、TRACE；',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `index_code_UNIQUE` (`c_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='功能 API';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_res_func_api_req_channel`
--

DROP TABLE IF EXISTS `t_sys_res_func_api_req_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_res_func_api_req_channel` (
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
  `c_channel_id` bigint NOT NULL COMMENT '渠道ID',
  `c_func_api_id` bigint NOT NULL COMMENT '功能API ID',
  `c_channel_code` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '渠道代码，沉余字段，方便查询；',
  `c_system_code` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '接口所在的应用系统代码，沉余字段，方便查询；',
  `c_func_api_code` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '接口代码，沉余字段，方便查询；',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `unique_code` (`c_channel_code`,`c_func_api_code`,`c_system_code`) COMMENT '应用系统代码、接口代码、渠道代码 三者唯一',
  UNIQUE KEY `unique_index_pkid` (`c_channel_id`,`c_func_api_id`) COMMENT '接口ID、渠道ID 二者唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='请求渠道-功能资源';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_res_menu`
--

DROP TABLE IF EXISTS `t_sys_res_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_res_menu` (
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
  `c_name` json NOT NULL COMMENT '菜单名称',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜单代码，全表唯一。',
  `c_parent_id` bigint DEFAULT NULL COMMENT '父节点 id',
  `c_nav_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `c_func_module_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `c_func_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `c_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `c_is_index` int NOT NULL COMMENT '不能为空；是否为默认路由，0-不是；1-是',
  `c_exact` tinyint NOT NULL COMMENT '默认 false；当为true时，仅当路径与位置匹配时才匹配; 1、当 isFrame 0-不为路由容器，又有子路由时，要为 true 子路由才能显示出来；？要不要默认设置为true；2、当 isFrame 0-路由容器，此配置不起作用；值为 false；',
  `c_is_frame` int NOT NULL COMMENT '1-路由容器，否则为正常的一般路由；',
  `c_is_show` int NOT NULL COMMENT '是否显示；0-显示；1-不显示；',
  `c_icon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `c_sort` int NOT NULL,
  `c_permission` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_code_UNIQUE` (`c_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='菜单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_res_navigation`
--

DROP TABLE IF EXISTS `t_sys_res_navigation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_res_navigation` (
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
  `c_name` json NOT NULL COMMENT '栏目国际化名称',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '栏目代码，唯一',
  `c_func_module_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '不能为空，功能模块代码，在哪个功能模块中实现就写哪个功能模块的代码，也是功能模块目录',
  `c_func_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '不能为空，功能名称，导航栏的首页组件名；将根据这名称查找到对应功能组件；',
  `c_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '不能为空',
  `c_sort` int NOT NULL COMMENT '排序',
  `c_is_index` int NOT NULL COMMENT '是否是默认栏目，0-不是；1-是；',
  `c_is_show` int NOT NULL COMMENT '是否显示；0-不显示；1-显示；',
  `c_icon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `c_code_UNIQUE` (`c_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='导航栏目';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_res_request_channel`
--

DROP TABLE IF EXISTS `t_sys_res_request_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_res_request_channel` (
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
  `c_name` json NOT NULL COMMENT '渠道名称',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '渠道代码，唯一',
  `c_channel_desc` json DEFAULT NULL COMMENT '渠道说明',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='请求渠道';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_set_collection`
--

DROP TABLE IF EXISTS `t_sys_set_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_set_collection` (
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
  `c_name` json NOT NULL COMMENT '配置项组别名称',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '配置项组别代码；全表唯一；',
  `c_set_desc` json DEFAULT NULL COMMENT '配置项组别说明',
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '集团代码',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司代码',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `code_UNIQUE` (`c_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='配置项分组，集合';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_set_item`
--

DROP TABLE IF EXISTS `t_sys_set_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_set_item` (
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
  `c_collection_id` bigint NOT NULL COMMENT '配置项组别ID',
  `c_collection_code` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '配置项组别代码',
  `c_type` int NOT NULL DEFAULT '0' COMMENT '配置项类型：0-平台配置；1-通用配置；2-公司专属配置；',
  `c_name` json NOT NULL COMMENT '配置项名称',
  `c_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '配置项代码；组别下全表唯一；',
  `c_value_type` int NOT NULL DEFAULT '0' COMMENT '配置值类型：0-字符串；1-boolean 布尔值；2-整数；3-字符数组；4-数字数组；',
  `c_value` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '配置值：通用配置类型时，公司可以自定义配置值',
  `c_set_desc` json DEFAULT NULL COMMENT '配置项说明',
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '集团代码：仅公司专属配置类型时，有值',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司代码：仅公司专属配置类型时，有值',
  PRIMARY KEY (`c_pk_id`),
  UNIQUE KEY `unique_code` (`c_code`,`c_collection_code`),
  UNIQUE KEY `code_id` (`c_code`,`c_collection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='应用系统配置项条目';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_sys_set_item_company_value`
--

DROP TABLE IF EXISTS `t_sys_set_item_company_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sys_set_item_company_value` (
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
  `c_set_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '配置项代码',
  `c_value` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '配置值',
  `c_group_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '集团代码',
  `c_company_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司代码',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='通用型系统配置项条目，公司才有自定义配置值';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-20 10:29:01
