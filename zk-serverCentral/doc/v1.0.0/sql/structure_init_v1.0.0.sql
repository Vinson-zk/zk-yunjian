CREATE DATABASE  IF NOT EXISTS `zk-ser-cen` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `zk-ser-cen`;
-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.211.55.11    Database: zk-ser-cen
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
-- Table structure for table `t_sc_server_certificate`
--

DROP TABLE IF EXISTS `t_sc_server_certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sc_server_certificate` (
  `c_pk_id` bigint NOT NULL COMMENT '主键 ',
  `c_create_user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `c_update_user_id` bigint DEFAULT NULL COMMENT '修改用户',
  `c_create_date` timestamp NOT NULL COMMENT '创建时间',
  `c_update_date` timestamp NOT NULL COMMENT '修改时间',
  `c_del_flag` int NOT NULL COMMENT '删除标识；0-正常，1-删除；',
  `c_version` int NOT NULL COMMENT '数据版本',
  `c_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `c_p_desc` json DEFAULT NULL COMMENT '国际化说明与描述',
  `c_spare1` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 1',
  `c_spare2` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 2',
  `c_spare3` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备用字段 3',
  `c_spare_json` json DEFAULT NULL COMMENT '备用字段 json',
  `c_valid_start_date` timestamp NULL DEFAULT NULL COMMENT '证书有效期起始日期',
  `c_valid_end_date` timestamp NULL DEFAULT NULL COMMENT '证书有效期结束日期；',
  `c_status` int NOT NULL COMMENT '证书状态；0-正常，1-禁用；',
  `c_server_name` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '证书对应服务名称',
  `c_private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '证书私钥',
  `c_public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '证书公钥',
  PRIMARY KEY (`c_pk_id`) COMMENT '服务证书表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务中心证书';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-20 10:29:25
