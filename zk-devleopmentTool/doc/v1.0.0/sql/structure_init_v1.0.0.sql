CREATE DATABASE  IF NOT EXISTS `zk-devleopment-tool` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `zk-devleopment-tool`;
-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.211.55.11    Database: zk-devleopment-tool
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
-- Table structure for table `t_dt_code_gen_module`
--

DROP TABLE IF EXISTS `t_dt_code_gen_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_dt_code_gen_module` (
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
  `c_db_driver` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据库驱动；默认：com.mysql.jdbc.Driver',
  `c_db_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据库连接 uri',
  `c_db_username` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据库用户名；',
  `c_db_pwd` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据库密码；',
  `c_db_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据库类型，mysql，oracle 注：这不是实例名；默认：mysql',
  `c_table_name_prefix` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '表名前缀',
  `c_col_name_prefix` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '字段名前缀',
  `c_is_remove_prefix` tinyint(1) NOT NULL COMMENT '是否去掉前缀；默认：true',
  `c_package_prefix` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'java 类包名前缀 包名=【类包名前缀 + 功能模块名 + 功能模块子名】',
  `c_module_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'java 功能模块名',
  `c_label_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'java 功能模块的标签名称；即显示 名',
  `c_module_prefix` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '模块前缀；用于生成类名等前缀。',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='功能模块；记录代码生成的模块信息，如数据库链接等';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_dt_code_gen_table_col_info`
--

DROP TABLE IF EXISTS `t_dt_code_gen_table_col_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_dt_code_gen_table_col_info` (
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
  `c_col_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '表: 字段名',
  `c_col_jdbc_type` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '表：JDBC类型；',
  `c_col_comments` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '表：描述，与JAVA类属性注释共用',
  `c_col_is_pk` tinyint(1) NOT NULL COMMENT '表：是否是主键；计算得出来的；false-不是；true-主键；默认 false;',
  `c_col_is_null` tinyint(1) NOT NULL COMMENT '表：是否可为空；false-不为空；true-可为空；默认 false',
  `c_col_sort` int NOT NULL COMMENT '表：排序字段, 默认为 999999',
  `c_attr_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类：属性名',
  `c_attr_type` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类：属性类型,默认 String',
  `c_attr_is_base` tinyint(1) NOT NULL COMMENT '判断是否是父类中的字段; false-不是; true-是; 默认：false',
  `c_attr_query_type` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '查询方式；null-不是查询字段；EQ-等于；NEQ-不等于；GT-大于；GTE-大于等于；LT-小于；LTE-小于等于；IN-在其中；NIN-不在其中；LIKE-模糊查询；NLIKE；ISNULL-为空；ISNOTNULL-不为空；默认：null',
  `c_attr_is_insert` tinyint(1) NOT NULL COMMENT '是否为插入字段; false-不插入；true-插入字段；默认 true',
  `c_attr_is_update` tinyint(1) NOT NULL COMMENT '是否编辑字段；false-不是编辑字段；true-编辑字段；默认 true',
  `c_label` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '属性标签',
  `c_table_id` bigint NOT NULL,
  `c_edit_strategy` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编辑策略',
  `c_search_strategy` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '查询策略',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='表字段信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_dt_code_gen_table_info`
--

DROP TABLE IF EXISTS `t_dt_code_gen_table_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_dt_code_gen_table_info` (
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
  `c_module_id` bigint NOT NULL COMMENT 'java 功能模块 id',
  `c_table_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '表名；',
  `c_table_comments` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '表描述',
  `c_pk_col_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '表主键字段名',
  `c_class_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'java 实体类名，首字母大写',
  `c_sub_module_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'java 功能子模块名, 默认没有；',
  `c_func_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `c_is_tree` tinyint(1) NOT NULL COMMENT '是否是树形结构; true-是树形结构；false-不是树形结构；默认: false；',
  `c_label` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '表标签',
  `c_icon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '功能图标',
  `c_nav_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所属导航栏目',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='表信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_dt_code_gen_test`
--

DROP TABLE IF EXISTS `t_dt_code_gen_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_dt_code_gen_test` (
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
  `c_value_json` json DEFAULT NULL COMMENT 'ZKJson',
  `c_value_string` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '字符串',
  `c_value_date` datetime NOT NULL COMMENT '日期格式；',
  `c_value_int` int DEFAULT NULL COMMENT '整型；',
  `c_value_float` float(7,3) NOT NULL COMMENT '浮点型',
  `c_value_bool` tinyint NOT NULL COMMENT '布尔型',
  `c_value_int_sel` int NOT NULL COMMENT '下拉',
  `c_value_str_icon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图标',
  `c_parent_id` bigint DEFAULT NULL COMMENT '父结点 ID',
  PRIMARY KEY (`c_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='生成测试';
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-20 10:30:35
